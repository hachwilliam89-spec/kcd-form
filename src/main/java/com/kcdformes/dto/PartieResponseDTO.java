package com.kcdformes.dto;

import com.kcdformes.model.gameplay.Difficulte;
import com.kcdformes.model.gameplay.EtatPartie;

public class PartieResponseDTO {
    private Long id;
    private Difficulte difficulte;
    private EtatPartie etat;
    private int vagueActuelle;
    private Long joueurId;
    private String joueurNom;

    public PartieResponseDTO(Long id, Difficulte difficulte, EtatPartie etat, int vagueActuelle, Long joueurId, String joueurNom) {
        this.id = id;
        this.difficulte = difficulte;
        this.etat = etat;
        this.vagueActuelle = vagueActuelle;
        this.joueurId = joueurId;
        this.joueurNom = joueurNom;
    }

    public Long getId() { return id; }
    public Difficulte getDifficulte() { return difficulte; }
    public EtatPartie getEtat() { return etat; }
    public int getVagueActuelle() { return vagueActuelle; }
    public Long getJoueurId() { return joueurId; }
    public String getJoueurNom() { return joueurNom; }
}