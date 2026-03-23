package com.kcdformes.repository;

import com.kcdformes.entity.JoueurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JoueurRepository extends JpaRepository<JoueurEntity, Long> {
    boolean existsByNom(String nom);
}