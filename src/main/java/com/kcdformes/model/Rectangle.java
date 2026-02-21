package com.kcdformes.model;

public class Rectangle extends Forme {

    private double largeur;
    private double longueur;

    public Rectangle(String nom, double largeur, double longueur) {
        super(nom, "gris");
        setLargeur(largeur);
        setLongueur(longueur);
    }

    @Override
    public double aire() {
        return largeur * longueur;
    }

    @Override
    public double perimetre() {
        return 2 * (largeur + longueur);
    }

    // Getters
    public double getLargeur() {
        return largeur;
    }

    public double getLongueur() {
        return longueur;
    }

    // Setters avec validation
    public void setLargeur(double largeur) {
        if (largeur <= 0) {
            throw new IllegalArgumentException("La largeur doit être strictement positive. Reçu : " + largeur);
        }
        this.largeur = largeur;
    }

    public void setLongueur(double longueur) {
        if (longueur <= 0) {
            throw new IllegalArgumentException("La longueur doit être strictement positive. Reçu : " + longueur);
        }
        this.longueur = longueur;
    }

    @Override
    public String toString() {
        return "Rectangle " + getNom() + " [couleur=" + getCouleur()
                + ", largeur=" + largeur + ", longueur=" + longueur + "]";
    }
}