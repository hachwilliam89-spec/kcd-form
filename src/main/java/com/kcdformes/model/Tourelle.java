package com.kcdformes.model;

import java.util.ArrayList;
import java.util.List;

public class Tourelle {

    private String nom;
    private List<Forme> formes;
    private int position;
    private static final int MAX_FORMES = 3;

    // CONSTRUCTEUR

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
        if (formes.size() >= MAX_FORMES) {
            throw new IllegalStateException("Max " + MAX_FORMES + " formes atteint.");
        }
        return formes.add(forme);
    }

    public boolean supprimerForme(Forme forme) {
        return formes.remove(forme);
    }

    // COMPTEURS PAR TYPE

    public int compterTriangles() {
        int count = 0;
        for (Forme f : formes) {
            if (f instanceof Triangle) count++;
        }
        return count;
    }

    public int compterRectangles() {
        int count = 0;
        for (Forme f : formes) {
            if (f instanceof Rectangle) count++;
        }
        return count;
    }

    public int compterCercles() {
        int count = 0;
        for (Forme f : formes) {
            if (f instanceof Cercle) count++;
        }
        return count;
    }

    // STATS DE GAMEPLAY

    /**
     * Nombre de tirs = nombre de triangles.
     * 🔺 = 1 tir, 🔺🔺 = 2 tirs, 🔺🔺🔺 = 3 tirs
     */
    public int getNombreTirs() {
        return compterTriangles();
    }

    /**
     * La tourelle a des dégâts en zone si elle contient au moins un cercle.
     */
    public boolean hasAoE() {
        return compterCercles() > 0;
    }

    /**
     * Rayon de la zone AoE = somme des rayons des cercles.
     * 🔴 = rayon de base, 🔴🔴 = plus grand, 🔴🔴🔴 = énorme
     */
    public double getRayonZone() {
        double rayon = 0;
        for (Forme f : formes) {
            if (f instanceof Cercle) {
                rayon += ((Cercle) f).getRayon();
            }
        }
        return rayon;
    }

    /**
     * Points de vie = somme des périmètres des rectangles × 10.
     * Le rectangle est un mur : il a des PV mais ne fait pas de dégâts.
     * 🟦 = PV de base, 🟦🟦 = plus de PV, 🟦🟦🟦 = PV énormes
     */
    public double getPV() {
        double pv = 0;
        for (Forme f : formes) {
            if (f instanceof Rectangle) {
                pv += f.perimetre() * 10;
            }
        }
        return pv;
    }

    // CALCULS POLYMORPHIQUES

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

    // INFOS

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
        StringBuilder sb = new StringBuilder();
        sb.append("Tourelle \"").append(nom).append("\"");
        sb.append(" [pos=").append(position);
        sb.append(", formes=").append(formes.size()).append("/").append(MAX_FORMES);

        if (getNombreTirs() > 0) {
            sb.append(", tirs=").append(getNombreTirs());
        }
        if (hasAoE()) {
            sb.append(", AoE rayon=").append(String.format("%.1f", getRayonZone()));
        }
        if (getPV() > 0) {
            sb.append(", PV=").append(String.format("%.0f", getPV()));
        }

        sb.append(", DPS=").append(String.format("%.1f", dpsTotal()));
        sb.append(", coût=").append(coutTotal()).append(" or]");
        return sb.toString();
    }
}