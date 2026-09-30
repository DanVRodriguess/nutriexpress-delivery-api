package com.nutriexpress.delivery.exception;

/**
 * quando se procura um prato por um id que não existe.
 * Será traduzida para HTTP 404 (Not Found) pelo GlobalExceptionHandler.
 */
public class PratoNaoEncontradoException extends RuntimeException {

    public PratoNaoEncontradoException(Long id) {
        super("Prato não encontrado com o id: " + id);
    }
}