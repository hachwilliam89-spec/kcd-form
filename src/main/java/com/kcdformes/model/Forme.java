package com.kcdformes.model;

public abstract class Forme {

    String nom;
    String couleur;

    public Forme(String nom, String couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }

    public abstract double aire();
    public abstract double perimetre();
}