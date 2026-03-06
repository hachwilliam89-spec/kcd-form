package com.kcdformes.dto;

public class JoueurResponseDTO {
    private Long id;
    private String nom;
    private int budget;
    private int score;
    private int vies;

    public JoueurResponseDTO(Long id, String nom, int budget, int score, int vies) {
        this.id = id;
        this.nom = nom;
        this.budget = budget;
        this.score = score;
        this.vies = vies;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public int getBudget() { return budget; }
    public int getScore() { return score; }
    public int getVies() { return vies; }
}