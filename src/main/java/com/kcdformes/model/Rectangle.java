package com.kcdformes.model;

public class Rectangle extends Forme {

    private final double largeur;
    private final double longueur;

    public Rectangle(String nom, double largeur, double longueur) {
        super(nom, "gris");
        if (largeur <= 0) {
            throw new IllegalArgumentException("La largeur doit être strictement positive. Reçu : " + largeur);
        }
        if (longueur <= 0) {
            throw new IllegalArgumentException("La longueur doit être strictement positive. Reçu : " + longueur);
        }
        this.largeur = largeur;
        this.longueur = longueur;
    }

    @Override
    public double aire() {
        return largeur * longueur;
    }

    @Override
    public double perimetre() {
        return 2 * (largeur + longueur);
    }

    @Override
    public double dps() {
        return 0;
    }

    @Override
    public int cout() {
        return (int)(aire() * 2.2);
    }

    // Getters
    public double getLargeur() { return largeur; }
    public double getLongueur() { return longueur; }
    public int getPv() { return (int)(aire() * 10); }

    @Override
    public String toString() {
        return "Rectangle " + getNom() + " [couleur=" + getCouleur()
                + ", largeur=" + largeur + ", longueur=" + longueur
                + ", PV=" + getPv() + ", cout=" + cout() + " or]";
    }
}