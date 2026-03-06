package com.kcdformes.repository;

import com.kcdformes.entity.TourelleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourelleRepository extends JpaRepository<TourelleEntity, Long> {
    List<TourelleEntity> findByPartieId(Long partieId);
}