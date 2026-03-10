package com.kcdformes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "joueurs")
public class JoueurEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int budget;
    private int score;
    private int vies;

    // Constructeurs
    public JoueurEntity() {}

    public JoueurEntity(String nom, int budget, int vies) {
        this.nom = nom;
        this.budget = budget;
        this.vies = vies;
        this.score = 0;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getVies() { return vies; }
    public void setVies(int vies) { this.vies = vies; }
}