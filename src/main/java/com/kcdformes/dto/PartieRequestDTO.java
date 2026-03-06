package com.kcdformes.dto;

import com.kcdformes.model.gameplay.Difficulte;

public class PartieRequestDTO {
    private Long joueurId;
    private Difficulte difficulte;

    public Long getJoueurId() { return joueurId; }
    public void setJoueurId(Long joueurId) { this.joueurId = joueurId; }
    public Difficulte getDifficulte() { return difficulte; }
    public void setDifficulte(Difficulte difficulte) { this.difficulte = difficulte; }
}