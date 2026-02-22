package com.kcdformes.model;

import java.util.ArrayList;
import java.util.List;

public class Tourelle {

    private String nom;
    private List<Forme> formes;
    private int position;
    private int orientation; // 0=haut, 90=droite, 180=bas, 270=gauche

    public void setOrientation(int orientation) {
        if (orientation != 0 && orientation != 90 && orientation != 180 && orientation != 270) {
            throw new IllegalArgumentException("L'orientation doit être 0, 90, 180 ou 270.");
        }
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public Tourelle(String nom, int position) {
        setNom(nom);
        this.formes = new ArrayList<>();
        setPosition(position);
        this.orientation = 0;
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

    public boolean estZone() {
        for (Forme forme : formes) {
            if (forme instanceof Cercle) {
                return true;
            }
        }
        return false;
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

    public double degatsContre(Ennemi ennemi) {
        double dps = 0;
        for (Forme forme : formes) {
            double bonus = 1.0;

            if (forme instanceof Triangle) {
                // Archer : malus ×0.75 vs Bélier
                if (ennemi.getForme() instanceof Rectangle) {
                    bonus = 0.75;
                }
            } else if (forme instanceof Cercle) {
                // Catapulte : bonus ×1.25 vs Bélier
                if (ennemi.getForme() instanceof Rectangle) {
                    bonus = 1.25;
                }
            }

            dps += forme.dps() * bonus;
        }
        return dps;
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
                + ", orientation=" + orientation + "°"
                + ", formes=" + formes.size()
                + ", DPS=" + dpsTotal()
                + ", cout=" + coutTotal() + " or]";
    }
}