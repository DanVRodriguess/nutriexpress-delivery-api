package com.nutriexpress.delivery.dto;

import java.math.BigDecimal;

/**
 * Dados devolvidos ao cliente da API.
 *
 * Justificativa da escolha dos campos: todos os atributos do prato são relevantes
 * para quem consome a API de um app de delivery (preço, informações nutricionais
 * e porção), e a entidade não possui campos internos ou sensíveis a esconder.
 * Mesmo assim, o DTO é mantido separado da entidade Prato para que o contrato
 * da API não fique preso à estrutura da tabela: o banco pode mudar sem quebrar
 * quem usa a API.
 */
public record PratoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal valor,
        String categoria,
        Integer calorias,
        Double quantidade,
        String unidadeMedida
) {
}