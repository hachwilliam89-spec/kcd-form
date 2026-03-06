package com.kcdformes.model.ennemis;

import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Forme;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;

public class Ennemi {

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

    // Constructeur avec coefficient de difficulté
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
        this.vitesse = calculerVitesse();
        this.position = 0;
        this.degatsRempart = calculerDegatsRempart();
        this.degatsForteresse = calculerDegatsForteresse();
        this.pointsScore = (int)(calculerPoints() * coeffDifficulte);
    }

    // Constructeur par défaut (coeff = 1.0)
    public Ennemi(String nom, Forme forme) {
        this(nom, forme, 1.0);
    }

    // CALCULS BASÉS SUR LA FORME

    public int calculerPV() {
        return (int)(forme.aire() * 25);
    }

    public double calculerVitesse() {
        if (forme instanceof Triangle) return 3.0;
        if (forme instanceof Cercle) return 1.0;
        if (forme instanceof Rectangle) return 1.0;
        return 1.0;
    }

    public double calculerDegatsRempart() {
        if (forme instanceof Triangle) return 1.0;
        if (forme instanceof Rectangle) return 2.0;
        if (forme instanceof Cercle) return 1.0;
        return 1.0;
    }

    /**
     * Dégâts fixes infligés à la forteresse.
     * Indépendant des PV et du coeff de difficulté.
     */
    public int calculerDegatsForteresse() {
        if (forme instanceof Triangle) return 10;
        if (forme instanceof Cercle) return 15;
        if (forme instanceof Rectangle) return 40;
        return 10;
    }

    /**
     * Points de score attribués quand l'ennemi est éliminé.
     * Plus l'ennemi est dur à tuer, plus il rapporte.
     */
    public int calculerPoints() {
        if (forme instanceof Triangle) return (int)(forme.aire() / 2);
        if (forme instanceof Cercle) return (int)(forme.aire() / 1.5);
        if (forme instanceof Rectangle) return (int)(forme.aire() * 1.5);
        return (int) forme.aire();
    }

    // GAMEPLAY

    public void avancer() {
        position += Math.max(1, (int) vitesse);
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

    public double getDegatsRempart() {
        return degatsRempart;
    }

    public int getDegatsForteresse() {
        return degatsForteresse;
    }

    public int getPointsScore() {
        return pointsScore;
    }

    public boolean isScoreComptabilise() {
        return scoreComptabilise;
    }

    public void setScoreComptabilise(boolean scoreComptabilise) {
        this.scoreComptabilise = scoreComptabilise;
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
                + ", degatsForteresse=" + degatsForteresse
                + ", position=" + position + "]";
    }
}