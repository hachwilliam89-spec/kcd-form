package com.kcdformes.model.formes;

public class Triangle extends Forme {

    private final double base;
    private final double hauteur;
    private final double coeff;
    private final double cadence;

    public Triangle(String nom, double base, double hauteur) {
        super(nom, "rouge");
        if (base <= 0) {
            throw new IllegalArgumentException("La base doit être strictement positive. Reçu : " + base);
        }
        if (hauteur <= 0) {
            throw new IllegalArgumentException("La hauteur doit être strictement positive. Reçu : " + hauteur);
        }
        this.base = base;
        this.hauteur = hauteur;
        this.coeff = 0.8;
        this.cadence = 3.0;
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

    @Override
    public double dps() {
        return aire() * coeff * cadence;
    }

    @Override
    public int cout() {
        return (int)(aire() * 9);
    }

    @Override
    public double getMultiplicateurContre(Forme formeEnnemi) {
        return formeEnnemi.getMultiplicateurRecuParTriangle();
    }

    @Override
    public double getMultiplicateurRecuParTriangle() { return 0.5; }

    @Override
    public double getMultiplicateurRecuParCercle() { return 1.25; }

    @Override
    public double getMultiplicateurRecuParRectangle() { return 0.75; }

    @Override
    public int getTirs() { return 1; }

    @Override
    public boolean isAoE() { return false; }

    @Override
    public double getRayonAoE() { return 0; }

    @Override
    public double getPvDefense() { return 0; }

    @Override
    public double getVitesseBase() { return 3.0; }

    @Override
    public double getDegatsRempartBase() { return 5.0; }

    @Override
    public double getMultiplicateurMuraille() { return 1.0; }

    // Getters
    public double getBase() { return base; }
    public double getHauteur() { return hauteur; }
    public double getCoeff() { return coeff; }
    public double getCadence() { return cadence; }

    @Override
    public String toString() {
        return "Triangle " + getNom() + " [couleur=" + getCouleur()
                + ", base=" + base + ", hauteur=" + hauteur
                + ", DPS=" + dps() + ", cout=" + cout() + " or]";
    }
}