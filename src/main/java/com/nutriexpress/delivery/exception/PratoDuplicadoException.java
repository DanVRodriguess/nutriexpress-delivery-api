package com.nutriexpress.delivery.exception;

/**
 * acontece quando se tenta cadastrar ou renomear um prato para um nome que já existe.
 * vai ser traduzida para HTTP 409 (Conflict) pelo GlobalExceptionHandler.
 */
public class PratoDuplicadoException extends RuntimeException {

    public PratoDuplicadoException(String nome) {
        super("Já existe um prato cadastrado com o nome: " + nome);
    }
}