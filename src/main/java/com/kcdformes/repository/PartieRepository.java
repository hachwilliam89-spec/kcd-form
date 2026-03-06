package com.kcdformes.repository;

import com.kcdformes.entity.PartieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartieRepository extends JpaRepository<PartieEntity, Long> {}