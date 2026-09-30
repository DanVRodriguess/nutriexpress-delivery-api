package com.nutriexpress.delivery.controller;

import com.nutriexpress.delivery.dto.PratoRequestDTO;
import com.nutriexpress.delivery.dto.PratoResponseDTO;
import com.nutriexpress.delivery.service.PratoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Camada web: recebe a requisição, valida com @Valid, delega ao PratoService
 * e devolve um ResponseEntity com o status HTTP adequado e bonitão.
 * Não contém regra de negócio nem acessa o banco: só conversa com o Service, usando DTOs tlgd.
 */
@RestController
@RequestMapping("/pratos")
public class PratoController {

    private final PratoService pratoService;

    public PratoController(PratoService pratoService) {
        this.pratoService = pratoService;
    }

    // GET /pratos -> 200 OK
    @GetMapping
    public ResponseEntity<List<PratoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pratoService.listarTodos());
    }

    // GET /pratos?categoria=vegano -> 200 OK
    // Este método só é usado quando a URL contém o parâmetro "categoria"
    @GetMapping(params = "categoria")
    public ResponseEntity<List<PratoResponseDTO>> listarPorCategoria(
            @RequestParam("categoria") String categoria) {
        return ResponseEntity.ok(pratoService.listarPorCategoria(categoria));
    }

    // GET /pratos/{id} -> 200 OK (ou 404 se o id não existir tlgd)
    @GetMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pratoService.buscarPorId(id));
    }

    // POST /pratos -> 201 Created, com o prato criado no corpo e o endereço dele no cabeçalho Location
    @PostMapping
    public ResponseEntity<PratoResponseDTO> criar(@Valid @RequestBody PratoRequestDTO dto) {
        PratoResponseDTO criado = pratoService.criar(dto);
        URI localizacao = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(criado);
    }

    // PUT /pratos/{id} -> 200 OK (ou 404 se o id não existir tlgd)
    @PutMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody PratoRequestDTO dto) {
        return ResponseEntity.ok(pratoService.atualizar(id, dto));
    }

    // DELETE /pratos/{id} -> 204 No Content (ou 404 se o id não existir tlgd)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
        pratoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}