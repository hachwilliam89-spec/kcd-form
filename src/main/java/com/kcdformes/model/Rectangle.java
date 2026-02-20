package com.kcdformes.model;

public class Rectangle extends Forme {

    double largeur;
    double longueur;

    public Rectangle(String nom, String couleur, double largeur, double longueur) {
        super(nom, couleur);
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
}