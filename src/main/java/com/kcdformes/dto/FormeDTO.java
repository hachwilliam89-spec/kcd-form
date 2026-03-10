package com.kcdformes.dto;

public class FormeDTO {
    private String type; // "TRIANGLE", "CERCLE", "RECTANGLE"
    private double valeur1; // base pour Triangle, rayon pour Cercle, largeur pour Rectangle
    private double valeur2; // hauteur pour Triangle, 0 pour Cercle, longueur pour Rectangle
    private String couleur;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getValeur1() { return valeur1; }
    public void setValeur1(double valeur1) { this.valeur1 = valeur1; }
    public double getValeur2() { return valeur2; }
    public void setValeur2(double valeur2) { this.valeur2 = valeur2; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
}