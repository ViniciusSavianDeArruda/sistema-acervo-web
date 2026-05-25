package com.example.museu.repository;

import com.example.museu.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {

    List<Log> findAllByOrderByDataHoraDesc();

    @Modifying
    @Transactional
    @Query("UPDATE Log l SET l.item = null WHERE l.item.id = :itemId")
    void limparReferenciaItem(@Param("itemId") Integer itemId);
}
