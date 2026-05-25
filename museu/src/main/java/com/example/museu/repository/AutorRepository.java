package com.example.museu.repository;

import com.example.museu.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Integer> {
    Optional<Autor> findByNomeIgnoreCase(String nome);
}
