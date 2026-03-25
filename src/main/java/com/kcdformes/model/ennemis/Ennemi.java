package com.kcdformes.model.ennemis;

import com.kcdformes.model.formes.Forme;
import com.kcdformes.model.gameplay.ComposantCombat;

public class Ennemi implements ComposantCombat {

    private String nom;
    private Forme forme;
    private int pvMax;
    private int pvActuels;
    private double vitesse;
    private int position;
    private double degatsRempart;
    private int degatsForteresse;
    private int pointsScore;
    private boolean scoreComptabilise = false;

    public Ennemi(String nom, Forme forme, double coeffDifficulte) {
        setNom(nom);
        if (forme == null) {
            throw new IllegalArgumentException("La forme ne peut pas être null.");
        }
        if (coeffDifficulte <= 0) {
            throw new IllegalArgumentException("Le coefficient de difficulté doit être positif.");
        }
        this.forme = forme;
        this.pvMax = (int)(calculerPV() * coeffDifficulte);
        this.pvActuels = pvMax;
        this.vitesse = forme.getVitesseBase();
        this.position = 0;
        this.degatsRempart = forme.getDegatsRempartBase();
        this.degatsForteresse = calculerDegatsForteresse();
        this.pointsScore = (int)(calculerPoints() * coeffDifficulte);
    }

    public Ennemi(String nom, Forme forme) {
        this(nom, forme, 1.0);
    }

    // CALCULS BASÉS SUR LA FORME

    public int calculerPV() {
        return (int)(forme.aire() * 25);
    }

    public int calculerDegatsForteresse() {
        return (int)(forme.aire() * 2 * forme.getMultiplicateurMuraille());
    }

    public int calculerPoints() {
        return (int)(forme.aire() * forme.getMultiplicateurMuraille());
    }

    // COMPOSITE — Implémentation Leaf

    @Override
    public int getPvActuels() { return pvActuels; }

    @Override
    public int getPvMax() { return pvMax; }

    @Override
    public boolean estVivant() { return pvActuels > 0; }

    @Override
    public void subirDegats(double degats) {
        pvActuels -= (int) degats;
        if (pvActuels < 0) pvActuels = 0;
    }

    @Override
    public int getNombreVivants() { return estVivant() ? 1 : 0; }

    @Override
    public int getNombreUnites() { return 1; }

    // GAMEPLAY

    public void avancer() {
        position += Math.max(1, (int) vitesse);
    }

    public double getForceAttaque() {
        return (double) pvActuels / pvMax;
    }

    public double degatsReels() {
        return degatsRempart * getForceAttaque();
    }

    // GETTERS

    public String getNom() { return nom; }
    public Forme getForme() { return forme; }
    public double getVitesse() { return vitesse; }
    public int getPosition() { return position; }
    public double getDegatsRempart() { return degatsRempart; }
    public int getDegatsForteresse() { return degatsForteresse; }
    public int getPointsScore() { return pointsScore; }
    public boolean isScoreComptabilise() { return scoreComptabilise; }
    public void setScoreComptabilise(boolean scoreComptabilise) { this.scoreComptabilise = scoreComptabilise; }

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
                + ", degatsForteresse=" + degatsForteresse
                + ", position=" + position + "]";
    }
}