package com.kcdformes.model.gameplay;

import com.kcdformes.model.ennemis.Ennemi;

import java.util.ArrayList;
import java.util.List;

public class Escouade implements ComposantCombat {

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

    // COMPOSITE — Délégation aux enfants (Ennemi)

    @Override
    public int getPvActuels() {
        int total = 0;
        for (Ennemi e : ennemis) total += e.getPvActuels();
        return total;
    }

    @Override
    public int getPvMax() {
        int total = 0;
        for (Ennemi e : ennemis) total += e.getPvMax();
        return total;
    }

    @Override
    public boolean estVivant() {
        for (Ennemi e : ennemis) {
            if (e.estVivant()) return true;
        }
        return false;
    }

    @Override
    public void subirDegats(double degats) {
        List<Ennemi> vivants = getEnnemisVivants();
        if (vivants.isEmpty()) return;
        double degatsParEnnemi = degats / vivants.size();
        for (Ennemi e : vivants) e.subirDegats(degatsParEnnemi);
    }

    @Override
    public int getNombreVivants() {
        int count = 0;
        for (Ennemi e : ennemis) {
            if (e.estVivant()) count++;
        }
        return count;
    }

    @Override
    public int getNombreUnites() {
        return ennemis.size();
    }

    public List<Ennemi> getEnnemisVivants() {
        List<Ennemi> vivants = new ArrayList<>();
        for (Ennemi e : ennemis) {
            if (e.estVivant()) vivants.add(e);
        }
        return vivants;
    }

    // SPAWN

    public Ennemi spawnSuivant() {
        if (!commenced) {
            ticksAttente++;
            if (ticksAttente >= delaiAvantSpawn) {
                commenced = true;
                ticksAttente = 0;
                return spawnEnnemi();
            }
            return null;
        }

        if (estTerminee()) {
            return null;
        }

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
                + ", vivants=" + getNombreVivants()
                + ", PV=" + getPvActuels() + "/" + getPvMax()
                + ", delaiAvant=" + delaiAvantSpawn
                + ", delaiEntre=" + delaiEntreEnnemis
                + ", spawned=" + indexSpawn + "/" + ennemis.size() + "]";
    }
}