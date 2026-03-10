package com.kcdformes.repository;

import com.kcdformes.entity.FormeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormeRepository extends JpaRepository<FormeEntity, Long> {
}