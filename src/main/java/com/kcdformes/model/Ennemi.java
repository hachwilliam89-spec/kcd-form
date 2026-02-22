package com.kcdformes.model;

public class Ennemi {

    private String nom;
    private Forme forme;
    private int pvMax;
    private int pvActuels;
    private double vitesse;
    private int position;
    private int recompense;
    private double degatsRempart;

    public Ennemi(String nom, Forme forme) {
        setNom(nom);
        this.forme = forme;
        this.pvMax = calculerPV();
        this.pvActuels = pvMax;
        this.vitesse = calculerVitesse();
        this.position = 0;
        this.recompense = calculerRecompense();
        this.degatsRempart = calculerDegatsRempart();
    }

    // CALCULS BASÉS SUR LA FORME

    public int calculerPV() {
        return (int)(forme.aire() * 10);
    }

    public double calculerVitesse() {
        if (forme instanceof Triangle) return 3.5;
        if (forme instanceof Cercle) return 1.0;
        if (forme instanceof Rectangle) return 0.7;
        return 1.0;
    }

    public double calculerDegatsRempart() {
        if (forme instanceof Triangle) return 1.0;
        if (forme instanceof Rectangle) return 2.0;
        if (forme instanceof Cercle) return 1.0;
        return 1.0;
    }

    public int calculerRecompense() {
        if (forme instanceof Triangle) return (int)(forme.aire() / 2);
        if (forme instanceof Cercle) return (int)(forme.aire() / 1.5);
        if (forme instanceof Rectangle) return (int)(forme.aire() * 1.5);
        return (int) forme.aire();
    }

    // GAMEPLAY

    public void avancer() {
        position++;
    }

    public void subirDegats(double degats) {
        pvActuels -= (int) degats;
        if (pvActuels < 0) {
            pvActuels = 0;
        }
    }

    public boolean estVivant() {
        return pvActuels > 0;
    }

    public double getForceAttaque() {
        if (forme instanceof Cercle) {
            return (double) pvActuels / pvMax;
        }
        return 1.0;
    }

    public double degatsReels() {
        return degatsRempart * getForceAttaque();
    }

    // GETTERS

    public String getNom() {
        return nom;
    }

    public Forme getForme() {
        return forme;
    }

    public int getPvMax() {
        return pvMax;
    }

    public int getPvActuels() {
        return pvActuels;
    }

    public double getVitesse() {
        return vitesse;
    }

    public int getPosition() {
        return position;
    }

    public int getRecompense() {
        return recompense;
    }

    public double getDegatsRempart() {
        return degatsRempart;
    }

    // SETTERS

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.nom = nom;
    }

    public void setPosition(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("La position doit être positive.");
        }
        this.position = position;
    }

    @Override
    public String toString() {
        return "Ennemi " + nom + " [forme=" + forme.getNom()
                + ", PV=" + pvActuels + "/" + pvMax
                + ", vitesse=" + vitesse
                + ", position=" + position + "]";
    }
}