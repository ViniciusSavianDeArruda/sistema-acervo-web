package com.example.museu.repository;

import com.example.museu.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByTipo(Integer tipo);

    @Query("SELECT i FROM Item i WHERE i.tipo <> 5 AND i.ativo = true ORDER BY i.titulo")
    List<Item> findBibliograficos();

    @Query("SELECT i FROM Item i WHERE i.ativo = false ORDER BY i.titulo")
    List<Item> findBibliograficosEHistoricosExcluidos();

    @Query("SELECT i FROM Item i WHERE i.tipo = 5 AND i.ativo = true ORDER BY i.titulo")
    List<Item> findHistoricos();

    @Query("SELECT COUNT(i) FROM Item i WHERE i.tipo = :tipo AND i.ativo = true")
    long countByTipo(Integer tipo);

    @Query("SELECT COUNT(i) FROM Item i WHERE i.tipo <> 5 AND i.ativo = true")
    long countBibliograficos();
}
