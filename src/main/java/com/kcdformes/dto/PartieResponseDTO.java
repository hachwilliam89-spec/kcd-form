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
    private int scoreFinal;
    private int etoiles;
    private int ennemisElimines;
    private int ennemisTotal;
    private int forteressePvRestants;
    private int forteressePvMax;
    private int orDepense;

    public PartieResponseDTO(Long id, Difficulte difficulte, EtatPartie etat, int vagueActuelle,
                             Long joueurId, String joueurNom,
                             int scoreFinal, int etoiles,
                             int ennemisElimines, int ennemisTotal,
                             int forteressePvRestants, int forteressePvMax,
                             int orDepense) {
        this.id = id;
        this.difficulte = difficulte;
        this.etat = etat;
        this.vagueActuelle = vagueActuelle;
        this.joueurId = joueurId;
        this.joueurNom = joueurNom;
        this.scoreFinal = scoreFinal;
        this.etoiles = etoiles;
        this.ennemisElimines = ennemisElimines;
        this.ennemisTotal = ennemisTotal;
        this.forteressePvRestants = forteressePvRestants;
        this.forteressePvMax = forteressePvMax;
        this.orDepense = orDepense;
    }

    public Long getId() { return id; }
    public Difficulte getDifficulte() { return difficulte; }
    public EtatPartie getEtat() { return etat; }
    public int getVagueActuelle() { return vagueActuelle; }
    public Long getJoueurId() { return joueurId; }
    public String getJoueurNom() { return joueurNom; }
    public int getScoreFinal() { return scoreFinal; }
    public int getEtoiles() { return etoiles; }
    public int getEnnemisElimines() { return ennemisElimines; }
    public int getEnnemisTotal() { return ennemisTotal; }
    public int getForteressePvRestants() { return forteressePvRestants; }
    public int getForteressePvMax() { return forteressePvMax; }
    public int getOrDepense() { return orDepense; }
}