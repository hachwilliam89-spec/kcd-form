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
    public List<EnnemiEtatDTO> ennemis;

    public CombatEtatDTO(int vagueNumero, String etat, int forteressePvActuels,
                         int forteressePvMax, int score, int ennemisVivants,
                         int ennemisTotal, List<EnnemiEtatDTO> ennemis) {
        this.vagueNumero = vagueNumero;
        this.etat = etat;
        this.forteressePvActuels = forteressePvActuels;
        this.forteressePvMax = forteressePvMax;
        this.score = score;
        this.ennemisVivants = ennemisVivants;
        this.ennemisTotal = ennemisTotal;
        this.ennemis = ennemis;
    }
}