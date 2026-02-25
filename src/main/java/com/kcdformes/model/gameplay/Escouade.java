package com.kcdformes.model.gameplay;

import com.kcdformes.model.ennemis.Ennemi;

import java.util.ArrayList;
import java.util.List;

public class Escouade {

    private List<Ennemi> ennemis;
    private int delaiAvantSpawn;
    private int delaiEntreEnnemis;
    private int indexSpawn;
    private int ticksAttente;
    private boolean commenced;

    public Escouade(int delaiAvantSpawn, int delaiEntreEnnemis) {
        setDelaiAvantSpawn(delaiAvantSpawn);
        setDelaiEntreEnnemis(delaiEntreEnnemis);
        this.ennemis = new ArrayList<>();
        this.indexSpawn = 0;
        this.ticksAttente = 0;
        this.commenced = false;
    }

    // GESTION DES ENNEMIS

    public void ajouterEnnemi(Ennemi ennemi) {
        if (ennemi == null) {
            throw new IllegalArgumentException("L'ennemi ne peut pas être null.");
        }
        ennemis.add(ennemi);
    }

    /**
     * Tente de spawn le prochain ennemi.
     * Retourne l'ennemi spawné, ou null si en attente (délai) ou terminée.
     */
    public Ennemi spawnSuivant() {
        // Attente avant le premier spawn de l'escouade
        if (!commenced) {
            ticksAttente++;
            if (ticksAttente >= delaiAvantSpawn) {
                commenced = true;
                ticksAttente = 0;
                return spawnEnnemi();
            }
            return null;
        }

        // Escouade terminée
        if (estTerminee()) {
            return null;
        }

        // Délai entre chaque ennemi
        ticksAttente++;
        if (ticksAttente >= delaiEntreEnnemis) {
            ticksAttente = 0;
            return spawnEnnemi();
        }

        return null;
    }

    private Ennemi spawnEnnemi() {
        if (indexSpawn >= ennemis.size()) {
            return null;
        }
        return ennemis.get(indexSpawn++);
    }

    // ÉTAT

    public boolean estTerminee() {
        return indexSpawn >= ennemis.size();
    }

    public boolean estCommencee() {
        return commenced;
    }

    // GETTERS

    public List<Ennemi> getEnnemis() {
        return new ArrayList<>(ennemis);
    }

    public int getNombreEnnemis() {
        return ennemis.size();
    }

    public int getDelaiAvantSpawn() {
        return delaiAvantSpawn;
    }

    public int getDelaiEntreEnnemis() {
        return delaiEntreEnnemis;
    }

    // SETTERS

    public void setDelaiAvantSpawn(int delaiAvantSpawn) {
        if (delaiAvantSpawn < 0) {
            throw new IllegalArgumentException("Le délai avant spawn doit être positif ou zéro.");
        }
        this.delaiAvantSpawn = delaiAvantSpawn;
    }

    public void setDelaiEntreEnnemis(int delaiEntreEnnemis) {
        if (delaiEntreEnnemis < 0) {
            throw new IllegalArgumentException("Le délai entre ennemis doit être positif ou zéro.");
        }
        this.delaiEntreEnnemis = delaiEntreEnnemis;
    }

    @Override
    public String toString() {
        return "Escouade [" + ennemis.size() + " ennemis"
                + ", delaiAvant=" + delaiAvantSpawn
                + ", delaiEntre=" + delaiEntreEnnemis
                + ", spawned=" + indexSpawn + "/" + ennemis.size() + "]";
    }
}