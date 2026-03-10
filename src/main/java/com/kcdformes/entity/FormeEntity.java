package com.kcdformes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "formes")
public class FormeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // TRIANGLE, CERCLE, RECTANGLE
    private String nom;
    private String couleur;
    private double valeur1; // base ou rayon ou largeur
    private double valeur2; // hauteur ou 0 ou longueur
    private double aire;
    private double perimetre;
    private double dps;
    private int cout;

    public FormeEntity() {}

    public FormeEntity(String type, String nom, String couleur, double valeur1, double valeur2,
                       double aire, double perimetre, double dps, int cout) {
        this.type = type;
        this.nom = nom;
        this.couleur = couleur;
        this.valeur1 = valeur1;
        this.valeur2 = valeur2;
        this.aire = aire;
        this.perimetre = perimetre;
        this.dps = dps;
        this.cout = cout;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public double getValeur1() { return valeur1; }
    public void setValeur1(double valeur1) { this.valeur1 = valeur1; }
    public double getValeur2() { return valeur2; }
    public void setValeur2(double valeur2) { this.valeur2 = valeur2; }
    public double getAire() { return aire; }
    public void setAire(double aire) { this.aire = aire; }
    public double getPerimetre() { return perimetre; }
    public void setPerimetre(double perimetre) { this.perimetre = perimetre; }
    public double getDps() { return dps; }
    public void setDps(double dps) { this.dps = dps; }
    public int getCout() { return cout; }
    public void setCout(int cout) { this.cout = cout; }
}