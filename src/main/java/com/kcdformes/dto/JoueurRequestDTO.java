package com.kcdformes.dto;

public class JoueurRequestDTO {
    private String nom;
    private int budget;
    private int vies;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }
    public int getVies() { return vies; }
    public void setVies(int vies) { this.vies = vies; }
}