package com.kcdformes.dto;

import com.kcdformes.model.gameplay.EtatPartie;

public class VagueResponseDTO {
    private Long partieId;
    private int vagueActuelle;
    private int vagueMax;
    private EtatPartie etatPartie;
    private String message;

    public VagueResponseDTO(Long partieId, int vagueActuelle, int vagueMax, EtatPartie etatPartie, String message) {
        this.partieId = partieId;
        this.vagueActuelle = vagueActuelle;
        this.vagueMax = vagueMax;
        this.etatPartie = etatPartie;
        this.message = message;
    }

    public Long getPartieId() { return partieId; }
    public int getVagueActuelle() { return vagueActuelle; }
    public int getVagueMax() { return vagueMax; }
    public EtatPartie getEtatPartie() { return etatPartie; }
    public String getMessage() { return message; }
}