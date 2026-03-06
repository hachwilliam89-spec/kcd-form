package com.kcdformes.dto;

public class EnnemiEtatDTO {
    public String nom;
    public int position;
    public int pvActuels;
    public int pvMax;
    public boolean vivant;
    public String type;

    public EnnemiEtatDTO(String nom, int position, int pvActuels,
                         int pvMax, boolean vivant, String type) {
        this.nom = nom;
        this.position = position;
        this.pvActuels = pvActuels;
        this.pvMax = pvMax;
        this.vivant = vivant;
        this.type = type;
    }
}