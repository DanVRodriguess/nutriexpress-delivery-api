package com.nutriexpress.delivery.service;

import com.nutriexpress.delivery.dto.PratoRequestDTO;
import com.nutriexpress.delivery.dto.PratoResponseDTO;
import com.nutriexpress.delivery.exception.PratoDuplicadoException;
import com.nutriexpress.delivery.exception.PratoNaoEncontradoException;
import com.nutriexpress.delivery.model.Prato;
import com.nutriexpress.delivery.repository.PratoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * Camada de serviço: concentra as regras de negócio e a conversão entre entidade e DTO.
 * Recebe e devolve apenas DTOs; a entidade Prato nunca sai desta camada.
 */
@Service
public class PratoService {

    private final PratoRepository pratoRepository;

    // Injeção de dependência via construtor: o Spring entrega o repositório pronto
    public PratoService(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    public PratoResponseDTO criar(PratoRequestDTO dto) {
        validarNomeUnico(dto.nome(), null);
        Prato salvo = pratoRepository.save(toEntity(dto));
        return toDTO(salvo);
    }

    public List<PratoResponseDTO> listarTodos() {
        return pratoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public PratoResponseDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    public List<PratoResponseDTO> listarPorCategoria(String categoria) {
        // As categorias são gravadas em minúsculas, então a busca também é feita em minúsculas
        String categoriaNormalizada = categoria.trim().toLowerCase(Locale.ROOT);
        return pratoRepository.findByCategoria(categoriaNormalizada).stream()
                .map(this::toDTO)
                .toList();
    }

    public PratoResponseDTO atualizar(Long id, PratoRequestDTO dto) {
        Prato prato = buscarEntidade(id);
        validarNomeUnico(dto.nome(), id);
        copiarCampos(dto, prato);
        return toDTO(pratoRepository.save(prato));
    }

    public void remover(Long id) {
        if (!pratoRepository.existsById(id)) {
            throw new PratoNaoEncontradoException(id);
        }
        pratoRepository.deleteById(id);
    }

    /**
     * REGRA DE NEGÓCIO PRÓPRIA: não podem existir dois pratos com o mesmo nome
     *  tlgd isso vai ser feito (ignorando maiúsculas/minúsculas e espaços nas pontas).
     *
     * Motivo: o cardápio de um delivery identifica os pratos pelo nome; nomes repetidos com certeza
     * confundiriam o cliente e dificultariam saber qual prato foi pedido tlgd.
     *
     * Na criação (idDoPratoEmEdicao == null) qualquer prato com o nome conta como duplicado.
     * Na atualização, o próprio prato é ignorado, para que possa manter o seu nome.
     *
     * Observação: esta checagem não elimina a condição de corrida entre duas requisições
     * simultâneas; em produção, ela seria complementada por uma restrição UNIQUE no banco.
     */
    private void validarNomeUnico(String nome, Long idDoPratoEmEdicao) {
        String nomeLimpo = nome.trim();
        boolean jaExiste = (idDoPratoEmEdicao == null)
                ? pratoRepository.existsByNomeIgnoreCase(nomeLimpo)
                : pratoRepository.existsByNomeIgnoreCaseAndIdNot(nomeLimpo, idDoPratoEmEdicao);
        if (jaExiste) {
            throw new PratoDuplicadoException(nomeLimpo);
        }
    }

    // Busca a entidade ou lança PratoNaoEncontradoException (traduzida para 404)
    private Prato buscarEntidade(Long id) {
        return pratoRepository.findById(id)
                .orElseThrow(() -> new PratoNaoEncontradoException(id));
    }

    // DTO de entrada -> entidade nova
    private Prato toEntity(PratoRequestDTO dto) {
        Prato prato = new Prato();
        copiarCampos(dto, prato);
        return prato;
    }

    // Copia os dados do DTO para a entidade (usado na criação e na atualização)
    private void copiarCampos(PratoRequestDTO dto, Prato prato) {
        prato.setNome(dto.nome().trim());
        prato.setDescricao(dto.descricao());
        prato.setValor(dto.valor());
        prato.setCategoria(dto.categoria());
        prato.setCalorias(dto.calorias());
        prato.setQuantidade(dto.quantidade());
        prato.setUnidadeMedida(dto.unidadeMedida());
    }

    // Entidade -> DTO de resposta
    private PratoResponseDTO toDTO(Prato prato) {
        return new PratoResponseDTO(
                prato.getId(),
                prato.getNome(),
                prato.getDescricao(),
                prato.getValor(),
                prato.getCategoria(),
                prato.getCalorias(),
                prato.getQuantidade(),
                prato.getUnidadeMedida()
        );
    }
}
