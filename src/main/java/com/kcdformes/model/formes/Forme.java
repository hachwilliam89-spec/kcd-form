package com.kcdformes.model.formes;

public abstract class Forme {

    private String nom;
    private String couleur;

    public Forme(String nom, String couleur) {
        setNom(nom);
        setCouleur(couleur);
    }

    // Méthodes abstraites géométrie
    public abstract double aire();
    public abstract double perimetre();

    // Méthodes abstraites gameplay
    public abstract double dps();
    public abstract int cout();

    /** Multiplicateur de dégâts quand cette forme (tourelle) attaque un ennemi */
    public abstract double getMultiplicateurContre(Forme formeEnnemi);

    /** Réponse au double dispatch — dégâts reçus d'un Triangle (archer) */
    public abstract double getMultiplicateurRecuParTriangle();

    /** Réponse au double dispatch — dégâts reçus d'un Cercle (catapulte) */
    public abstract double getMultiplicateurRecuParCercle();

    /** Réponse au double dispatch — dégâts reçus d'un Rectangle (muraille) */
    public abstract double getMultiplicateurRecuParRectangle();

    /** Nombre de tirs que cette forme apporte à une tourelle */
    public abstract int getTirs();

    /** Est-ce que cette forme donne des dégâts de zone */
    public abstract boolean isAoE();

    /** Rayon de zone (0 si pas AoE) */
    public abstract double getRayonAoE();

    /** PV que cette forme apporte à une tourelle */
    public abstract double getPvDefense();

    /** Vitesse quand cette forme est un ennemi */
    public abstract double getVitesseBase();

    /** Dégâts contre les murailles quand cette forme est un ennemi */
    public abstract double getDegatsRempartBase();

    /** Multiplicateur de dégâts contre les murailles (bélier = 2.0, autres = 1.0) */
    public abstract double getMultiplicateurMuraille();

    // Getters
    public String getNom() {
        return nom;
    }

    public String getCouleur() {
        return couleur;
    }

    // Setters avec validation
    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.nom = nom;
    }

    private void setCouleur(String couleur) {
        if (couleur == null || couleur.isBlank()) {
            throw new IllegalArgumentException("La couleur ne peut pas être vide.");
        }
        this.couleur = couleur;
    }

    @Override
    public String toString() {
        return getNom() + " [couleur=" + couleur + "]";
    }
}