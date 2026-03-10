package com.kcdformes.model.formes;

public class Cercle extends Forme {

    private final double rayon;
    private final double coeff;
    private final double cadence;

    public Cercle(String nom, double rayon) {
        super(nom, "bleu");
        if (rayon <= 0) {
            throw new IllegalArgumentException("Le rayon doit être strictement positif. Reçu : " + rayon);
        }
        this.rayon = rayon;
        this.coeff = 1.5;
        this.cadence = 0.4;
    }

    @Override
    public double aire() {
        return Math.PI * rayon * rayon;
    }

    @Override
    public double perimetre() {
        return 2* Math.PI * rayon;
    }

    @Override
    public double dps() {
        return aire() * coeff * cadence;
    }

    @Override
    public int cout() {
        return (int)(aire() * 2.65);
    }

    @Override
    public double getMultiplicateurContre(Forme formeEnnemi) {
        return formeEnnemi.getMultiplicateurRecuParCercle();
    }

    @Override
    public double getMultiplicateurRecuParTriangle() { return 1.0; }

    @Override
    public double getMultiplicateurRecuParCercle() { return 1.0; }

    @Override
    public double getMultiplicateurRecuParRectangle() { return 1.75; }

    @Override
    public int getTirs() { return 0; }

    @Override
    public boolean isAoE() { return true; }

    @Override
    public double getRayonAoE() { return rayon; }

    @Override
    public double getPvDefense() { return 0; }

    @Override
    public double getVitesseBase() { return 1.0; }

    @Override
    public double getDegatsRempartBase() { return 3.0; }

    @Override
    public double getMultiplicateurMuraille() { return 1.0; }

    // Getters
    public double getRayon() { return rayon; }
    public double getCoeff() { return coeff; }
    public double getCadence() { return cadence; }

    @Override
    public String toString() {
        return "Cercle " + getNom() + " [couleur=" + getCouleur()
                + ", rayon=" + rayon
                + ", DPS=" + dps() + ", cout=" + cout() + " or]";
    }
}