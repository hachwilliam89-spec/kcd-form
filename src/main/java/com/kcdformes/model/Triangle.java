package com.kcdformes.model;

public class Triangle extends Forme {

    double base;
    double hauteur;

    public Triangle(String nom, String couleur, double base, double hauteur) {
        super(nom, couleur);
        this.base = base;
        this.hauteur = hauteur;
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
}