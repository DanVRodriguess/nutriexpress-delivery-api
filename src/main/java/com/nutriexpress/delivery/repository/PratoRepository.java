package com.nutriexpress.delivery.repository;

import com.nutriexpress.delivery.model.Prato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PratoRepository extends JpaRepository<Prato, Long> {

    // O Spring Data JPA gera a consulta a partir do nome do método:
    // findBy + Categoria => SELECT ... FROM pratos WHERE categoria = ?
    List<Prato> findByCategoria(String categoria);

    // Usado na criação: existe algum prato com este nome (ignorando maiúsculas/minúsculas)?
    boolean existsByNomeIgnoreCase(String nome);

    // Usado na atualização: existe OUTRO prato (id diferente) com este nome?
    // Assim um prato pode ser atualizado mantendo o próprio nome.
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}