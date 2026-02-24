package com.kcdformes.model;

public class Joueur {

    private String nom;
    private int budget;
    private int score;
    private int vies;

    public Joueur(String nom, int budget, int vies) {
        setNom(nom);
        setBudget(budget);
        this.score = 0;
        setVies(vies);
    }

    // GAMEPLAY

    public boolean depenser(int montant) {
        if (montant > budget) {
            return false;
        }
        budget -= montant;
        return true;
    }

    public void gagner(int montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant gagné doit être positif.");
        }
        budget += montant;
        score += montant;
    }

    public void ajouterScore(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Les points doivent être positifs.");
        }
        score += points;
    }

    public void perdreVie() {
        if (vies > 0) {
            vies--;
        }
    }

    public boolean estElimine() {
        return vies <= 0;
    }

    // GETTERS

    public String getNom() {
        return nom;
    }

    public int getBudget() {
        return budget;
    }

    public int getScore() {
        return score;
    }

    public int getVies() {
        return vies;
    }

    // SETTERS

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.nom = nom;
    }

    public void setBudget(int budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("Le budget ne peut pas être négatif.");
        }
        this.budget = budget;
    }

    public void setVies(int vies) {
        if (vies < 0) {
            throw new IllegalArgumentException("Les vies ne peuvent pas être négatives.");
        }
        this.vies = vies;
    }

    @Override
    public String toString() {
        return "Joueur " + nom + " [budget=" + budget
                + ", score=" + score
                + ", vies=" + vies + "]";
    }
}