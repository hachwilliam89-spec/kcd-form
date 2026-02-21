package com.kcdformes.model;

import java.util.ArrayList;
import java.util.List;

public class Tourelle {

    private String nom;
    private List<Forme> formes;
    private int position;

    public Tourelle(String nom, int position) {
        setNom(nom);
        this.formes = new ArrayList<>();
        setPosition(position);
    }

    // GESTION DES FORMES

    public boolean ajouterForme(Forme forme) {
        if (forme == null) {
            throw new IllegalArgumentException("La forme ne peut pas être null.");
        }
        return formes.add(forme);
    }

    public boolean supprimerForme(Forme forme) {
        return formes.remove(forme);
    }

    // CALCULS (POLY)

    public double aireTotale() {
        double total = 0;
        for (Forme forme : formes) {
            total += forme.aire();
        }
        return total;
    }

    public double perimetreTotale() {
        double total = 0;
        for (Forme forme : formes) {
            total += forme.perimetre();
        }
        return total;
    }

    public double dpsTotal() {
        double total = 0;
        for (Forme forme : formes) {
            total += forme.dps();
        }
        return total;
    }

    public int coutTotal() {
        int total = 0;
        for (Forme forme : formes) {
            total += forme.cout();
        }
        return total;
    }

    public int getNombreFormes() {
        return formes.size();
    }

    // GETTERS

    public String getNom() {
        return nom;
    }

    public int getPosition() {
        return position;
    }

    public List<Forme> getFormes() {
        return new ArrayList<>(formes);
    }

    // SETTERS

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.nom = nom;
    }

    public void setPosition(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("La position doit être positive. Reçu : " + position);
        }
        this.position = position;
    }

    // TO STRING

    @Override
    public String toString() {
        return "Tourelle " + nom + " [position=" + position
                + ", formes=" + formes.size()
                + ", DPS=" + dpsTotal()
                + ", cout=" + coutTotal() + " or]";
    }
}