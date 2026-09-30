package com.nutriexpress.delivery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tratamento global de erros: traduz exceções lançadas em qualquer Controller
 * (ou no Service) para respostas HTTP claras, em vez de erros 500 não tratados.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Dados inválidos no corpo da requisição (@Valid) -> 400 com o mapa campo -> mensagem
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> erros.put(erro.getField(), erro.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    // Prato inexistente -> 404 com mensagem clara
    @ExceptionHandler(PratoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarNaoEncontrado(PratoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensagem", ex.getMessage()));
    }

    // Nome de prato repetido (regra de negócio) -> 409 Conflict
    @ExceptionHandler(PratoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> tratarDuplicado(PratoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensagem", ex.getMessage()));
    }
}