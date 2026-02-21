package com.kcdformes.model;

public abstract class Forme {

    private String nom;
    private String couleur;

    public Forme(String nom, String couleur) {
        setNom(nom);
        setCouleur(couleur);
    }

    // Méthodes abstraites
    public abstract double aire();
    public abstract double perimetre();

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

    public void setCouleur(String couleur) {
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