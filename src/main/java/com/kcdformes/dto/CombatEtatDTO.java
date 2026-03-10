package com.kcdformes.dto;

import java.util.List;

public class CombatEtatDTO {
    private int vagueNumero;
    private String etat;
    private int forteressePvActuels;
    private int forteressePvMax;
    private int score;
    private int ennemisVivants;
    private int ennemisTotal;
    private int tempsEcoule;
    private int dureeSecondes;
    private boolean derniereVague;
    private List<EnnemiEtatDTO> ennemis;
    private List<MurailleEtatDTO> murailles;

    public CombatEtatDTO(int vagueNumero, String etat, int forteressePvActuels,
                         int forteressePvMax, int score, int ennemisVivants,
                         int ennemisTotal, int tempsEcoule, int dureeSecondes,
                         boolean derniereVague,
                         List<EnnemiEtatDTO> ennemis, List<MurailleEtatDTO> murailles) {
        this.vagueNumero = vagueNumero;
        this.etat = etat;
        this.forteressePvActuels = forteressePvActuels;
        this.forteressePvMax = forteressePvMax;
        this.score = score;
        this.ennemisVivants = ennemisVivants;
        this.ennemisTotal = ennemisTotal;
        this.tempsEcoule = tempsEcoule;
        this.dureeSecondes = dureeSecondes;
        this.derniereVague = derniereVague;
        this.ennemis = ennemis;
        this.murailles = murailles;
    }

    public int getVagueNumero() { return vagueNumero; }
    public String getEtat() { return etat; }
    public int getForteressePvActuels() { return forteressePvActuels; }
    public int getForteressePvMax() { return forteressePvMax; }
    public int getScore() { return score; }
    public int getEnnemisVivants() { return ennemisVivants; }
    public int getEnnemisTotal() { return ennemisTotal; }
    public int getTempsEcoule() { return tempsEcoule; }
    public int getDureeSecondes() { return dureeSecondes; }
    public boolean isDerniereVague() { return derniereVague; }
    public List<EnnemiEtatDTO> getEnnemis() { return ennemis; }
    public List<MurailleEtatDTO> getMurailles() { return murailles; }
}