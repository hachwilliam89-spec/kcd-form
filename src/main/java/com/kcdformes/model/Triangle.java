package com.kcdformes.model;

public class Triangle extends Forme {

    private double base;
    private double hauteur;

    public Triangle(String nom, double base, double hauteur) {
        super(nom, "rouge");
        setBase(base);
        setHauteur(hauteur);
    }

    @Override
    public double aire() {
        return (base * hauteur) / 2;
    }

    @Override
    public double perimetre() {
        double coteOblique = Math.sqrt(Math.pow(base / 2, 2) + Math.pow(hauteur, 2));
        return base + 2 * coteOblique;
    }

    // Getters
    public double getBase() {
        return base;
    }

    public double getHauteur() {
        return hauteur;
    }

    // Setters avec validation
    public void setBase(double base) {
        if (base <= 0) {
            throw new IllegalArgumentException("La base doit être strictement positive. Reçu : " + base);
        }
        this.base = base;
    }

    public void setHauteur(double hauteur) {
        if (hauteur <= 0) {
            throw new IllegalArgumentException("La hauteur doit être strictement positive. Reçu : " + hauteur);
        }
        this.hauteur = hauteur;
    }

    @Override
    public String toString() {
        return "Triangle " + getNom() + " [couleur=" + getCouleur()
                + ", base=" + base + ", hauteur=" + hauteur + "]";
    }
}