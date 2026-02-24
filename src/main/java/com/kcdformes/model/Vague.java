package com.kcdformes.model;

import java.util.ArrayList;
import java.util.List;

public class Vague {

    private int numero;
    private List<Ennemi> ennemis;
    private double delaiSpawn;
    private double coeffDifficulte;
    private int indexSpawn;

    public Vague(int numero, double delaiSpawn, double coeffDifficulte) {
        setNumero(numero);
        this.ennemis = new ArrayList<>();
        setDelaiSpawn(delaiSpawn);
        setCoeffDifficulte(coeffDifficulte);
        this.indexSpawn = 0;
    }

    // GESTION DES ENNEMIS

    public void ajouterEnnemi(Ennemi ennemi) {
        if (ennemi == null) {
            throw new IllegalArgumentException("L'ennemi ne peut pas être null.");
        }
        ennemis.add(ennemi);
    }

    public Ennemi spawnSuivant() {
        if (indexSpawn >= ennemis.size()) {
            return null;
        }
        return ennemis.get(indexSpawn++);
    }

    public boolean estTerminee() {
        return indexSpawn >= ennemis.size() && getNombreVivants() == 0;
    }

    public int getNombreEnnemis() {
        return ennemis.size();
    }

    public int getNombreVivants() {
        int count = 0;
        for (Ennemi ennemi : ennemis) {
            if (ennemi.estVivant()) {
                count++;
            }
        }
        return count;
    }

    // GETTERS

    public int getNumero() {
        return numero;
    }

    public List<Ennemi> getEnnemis() {
        return new ArrayList<>(ennemis);
    }

    /**
     * Retourne uniquement les ennemis déjà spawn (actifs sur le terrain).
     */
    public List<Ennemi> getEnnemisActifs() {
        List<Ennemi> actifs = new ArrayList<>();
        for (int i = 0; i < indexSpawn; i++) {
            actifs.add(ennemis.get(i));
        }
        return actifs;
    }

    public double getDelaiSpawn() {
        return delaiSpawn;
    }

    public double getCoeffDifficulte() {
        return coeffDifficulte;
    }

    // SETTERS

    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Le numéro de vague doit être positif.");
        }
        this.numero = numero;
    }

    public void setDelaiSpawn(double delaiSpawn) {
        if (delaiSpawn < 0) {
            throw new IllegalArgumentException("Le délai de spawn doit être positif.");
        }
        this.delaiSpawn = delaiSpawn;
    }

    public void setCoeffDifficulte(double coeffDifficulte) {
        if (coeffDifficulte <= 0) {
            throw new IllegalArgumentException("Le coefficient de difficulté doit être positif.");
        }
        this.coeffDifficulte = coeffDifficulte;
    }

    @Override
    public String toString() {
        return "Vague " + numero + " [ennemis=" + ennemis.size()
                + ", vivants=" + getNombreVivants()
                + ", coeff=" + coeffDifficulte + "]";
    }
}