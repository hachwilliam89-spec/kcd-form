package com.kcdformes.model;

public class Cercle extends Forme {

    private double rayon;
    private double coeff;
    private double cadence;

    public Cercle(String nom, double rayon) {
        super(nom, "bleu");
        setRayon(rayon);
        this.coeff = 0.7;
        this.cadence = 1.0;
    }

    @Override
    public double aire() {
        return Math.PI * rayon * rayon;
    }

    @Override
    public double perimetre() {
        return 2 * Math.PI * rayon;
    }

    @Override
    public double dps() {
        return aire() * coeff * cadence;
    }

    @Override
    public int cout() {
        return (int)(aire() * 2.5);
    }

    // Getter
    public double getRayon() { return rayon; }
    public double getCoeff() { return coeff; }
    public double getCadence() { return cadence; }

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
                + ", rayon=" + rayon
                + ", DPS=" + dps() + ", cout=" + cout() + " or]";
    }
}