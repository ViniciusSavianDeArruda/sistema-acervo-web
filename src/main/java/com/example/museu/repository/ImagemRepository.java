package com.example.museu.repository;

import com.example.museu.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagemRepository extends JpaRepository<Imagem, Integer> {
    List<Imagem> findByItemId(Integer itemId);
}
