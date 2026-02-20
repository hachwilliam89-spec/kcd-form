package com.kcdformes.model;

public class Cercle extends Forme {

    double rayon;

    public Cercle(String nom, String couleur, double rayon) {
        super(nom, couleur);
        this.rayon = rayon;
    }

    @Override
    public double aire() {
        return Math.PI * rayon * rayon;
    }

    @Override
    public double perimetre() {
        return 2 * Math.PI * rayon;
    }
}