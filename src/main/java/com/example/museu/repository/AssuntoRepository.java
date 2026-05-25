package com.example.museu.repository;

import com.example.museu.model.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {
    Optional<Assunto> findByNomeIgnoreCase(String nome);
}
