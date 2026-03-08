package com.kcdformes.dto;

import java.util.List;

public class CombatEtatDTO {
    public int vagueNumero;
    public String etat;
    public int forteressePvActuels;
    public int forteressePvMax;
    public int score;
    public int ennemisVivants;
    public int ennemisTotal;
    public int tempsEcoule;
    public int dureeSecondes;
    public boolean derniereVague;
    public List<EnnemiEtatDTO> ennemis;
    public List<MurailleEtatDTO> murailles;

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
}