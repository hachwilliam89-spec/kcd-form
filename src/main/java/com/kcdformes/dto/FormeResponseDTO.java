package com.kcdformes.dto;

public class FormeResponseDTO {
    private Long id;
    private String type;
    private String nom;
    private String couleur;
    private double valeur1;
    private double valeur2;
    private double aire;
    private double perimetre;
    private double dps;
    private int cout;

    public FormeResponseDTO(Long id, String type, String nom, String couleur,
                            double valeur1, double valeur2, double aire,
                            double perimetre, double dps, int cout) {
        this.id = id;
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
    public String getNom() { return nom; }
    public String getCouleur() { return couleur; }
    public double getValeur1() { return valeur1; }
    public double getValeur2() { return valeur2; }
    public double getAire() { return aire; }
    public double getPerimetre() { return perimetre; }
    public double getDps() { return dps; }
    public int getCout() { return cout; }
}