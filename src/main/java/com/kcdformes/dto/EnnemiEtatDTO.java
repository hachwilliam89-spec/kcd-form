package com.kcdformes.dto;

public class EnnemiEtatDTO {
    private int id;
    private String nom;
    private int position;
    private int pvActuels;
    private int pvMax;
    private boolean vivant;
    private String type;

    public EnnemiEtatDTO(int id, String nom, int position, int pvActuels,
                         int pvMax, boolean vivant, String type) {
        this.id = id;
        this.nom = nom;
        this.position = position;
        this.pvActuels = pvActuels;
        this.pvMax = pvMax;
        this.vivant = vivant;
        this.type = type;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getPosition() { return position; }
    public int getPvActuels() { return pvActuels; }
    public int getPvMax() { return pvMax; }
    public boolean isVivant() { return vivant; }
    public String getType() { return type; }
}