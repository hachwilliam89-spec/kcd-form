package com.kcdformes.model;

import java.util.ArrayList;
import java.util.List;

public class Vague {

    private int numero;
    private List<Ennemi> ennemis;
    private double delaiSpawn;
    private double coeffDifficulte;
    private int indexSpawn;
    private int dureeSecondes;
    private int tempsEcoule;
    private boolean derniereVague;

    public Vague(int numero, double delaiSpawn, double coeffDifficulte, int dureeSecondes) {
        setNumero(numero);
        this.ennemis = new ArrayList<>();
        setDelaiSpawn(delaiSpawn);
        setCoeffDifficulte(coeffDifficulte);
        setDureeSecondes(dureeSecondes);
        this.indexSpawn = 0;
        this.tempsEcoule = 0;
        this.derniereVague = false;
    }

    // GESTION DES ENNEMIS

    public void ajouterEnnemi(Ennemi ennemi) {
        if (ennemi == null) {
            throw new IllegalArgumentException("L'ennemi ne peut pas être null.");
        }
        ennemis.add(ennemi);
    }

    public void ajouterEnnemis(List<Ennemi> survivants) {
        if (survivants == null) {
            throw new IllegalArgumentException("La liste ne peut pas être null.");
        }
        ennemis.addAll(survivants);
    }

    public Ennemi spawnSuivant() {
        if (indexSpawn >= ennemis.size()) {
            return null;
        }
        return ennemis.get(indexSpawn++);
    }

    // TIMER

    public void tick() {
        tempsEcoule++;
    }

    public boolean estTerminee() {
        if (derniereVague) {
            return getNombreVivants() == 0;
        }
        return tempsEcoule >= dureeSecondes;
    }

    // STATISTIQUES

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

    public List<Ennemi> getEnnemisSurvivants() {
        return ennemis.stream()
                .filter(Ennemi::estVivant)
                .toList();
    }

    public int calculerRecompense() {
        return ennemis.stream()
                .filter(e -> !e.estVivant())
                .mapToInt(Ennemi::getRecompense)
                .sum();
    }

    // GETTERS

    public int getNumero() {
        return numero;
    }

    public List<Ennemi> getEnnemis() {
        return new ArrayList<>(ennemis);
    }

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

    public int getDureeSecondes() {
        return dureeSecondes;
    }

    public int getTempsEcoule() {
        return tempsEcoule;
    }

    public boolean isDerniereVague() {
        return derniereVague;
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

    public void setDureeSecondes(int dureeSecondes) {
        if (dureeSecondes <= 0) {
            throw new IllegalArgumentException("La durée doit être positive.");
        }
        this.dureeSecondes = dureeSecondes;
    }

    public void setDerniereVague(boolean derniereVague) {
        this.derniereVague = derniereVague;
    }

    @Override
    public String toString() {
        String timerInfo = derniereVague ? "FINALE" : "temps=" + tempsEcoule + "/" + dureeSecondes;
        return "Vague " + numero + " [ennemis=" + ennemis.size()
                + ", vivants=" + getNombreVivants()
                + ", " + timerInfo
                + ", coeff=" + coeffDifficulte + "]";
    }
}