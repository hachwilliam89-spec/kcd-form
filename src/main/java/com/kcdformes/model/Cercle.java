package com.kcdformes.model;

public class Cercle extends Forme {

    private double rayon;

    public Cercle(String nom, double rayon) {
        super(nom, "bleu");
        setRayon(rayon);
    }

    @Override
    public double aire() {
        return Math.PI * rayon * rayon;
    }

    @Override
    public double perimetre() {
        return 2 * Math.PI * rayon;
    }

    // Getter
    public double getRayon() {
        return rayon;
    }

    // Setter avec validation
    public void setRayon(double rayon) {
        if (rayon <= 0) {
            throw new IllegalArgumentException("Le rayon doit être strictement positif. Reçu : " + rayon);
        }
        this.rayon = rayon;
    }

    @Override
    public String toString() {
        return "Cercle " + getNom() + " [couleur=" + getCouleur()
                + ", rayon=" + rayon + "]";
    }
}