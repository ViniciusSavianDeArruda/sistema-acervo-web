package com.example.museu.repository;

import com.example.museu.model.Colecao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColecaoRepository extends JpaRepository<Colecao, Integer> {

    List<Colecao> findAllByAtivoTrue();

    long countByAtivoTrue();
}
