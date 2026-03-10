package com.kcdformes.repository;

import com.kcdformes.entity.MurailleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MurailleRepository extends JpaRepository<MurailleEntity, Long> {
    List<MurailleEntity> findByPartieId(Long partieId);
}