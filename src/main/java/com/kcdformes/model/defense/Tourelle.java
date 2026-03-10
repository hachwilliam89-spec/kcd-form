package com.kcdformes.model.defense;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Forme;

import java.util.ArrayList;
import java.util.List;

public class Tourelle {

    private String nom;
    private List<Forme> formes;
    private int position;
    private int portee;
    private static final int MAX_FORMES = 3;
    private static final int PORTEE_DEFAUT = 3;

    // CONSTRUCTEUR

    public Tourelle(String nom, int position) {
        setNom(nom);
        this.formes = new ArrayList<>();
        setPosition(position);
        this.portee = PORTEE_DEFAUT;
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

    // STATS DE GAMEPLAY — POLYMORPHISME PUR

    public int getNombreTirs() {
        int total = 0;
        for (Forme f : formes) {
            total += f.getTirs();
        }
        return total;
    }

    public boolean hasAoE() {
        for (Forme f : formes) {
            if (f.isAoE()) return true;
        }
        return false;
    }

    public double getRayonZone() {
        double rayon = 0;
        for (Forme f : formes) {
            rayon += f.getRayonAoE();
        }
        return rayon;
    }

    public double getPV() {
        double pv = 0;
        for (Forme f : formes) {
            pv += f.getPvDefense();
        }
        return pv;
    }

    // CALCULS POLYMORPHIQUES

    public double degatsContre(Ennemi ennemi) {
        if (ennemi == null || !ennemi.estVivant()) {
            return 0;
        }

        double totalDegats = 0;
        Forme formeEnnemi = ennemi.getForme();

        for (Forme f : formes) {
            double degats = f.dps();
            degats *= f.getMultiplicateurContre(formeEnnemi);
            totalDegats += degats;
        }

        return totalDegats;
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

    public double aireTotale() {
        double total = 0;
        for (Forme forme : formes) {
            total += forme.aire();
        }
        return total;
    }

    public double perimetreTotal() {
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

    public int getPortee() {
        return portee;
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

    public void setPortee(int portee) {
        if (portee <= 0) {
            throw new IllegalArgumentException("La portée doit être positive. Reçu : " + portee);
        }
        this.portee = portee;
    }

    // TO STRING

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tourelle \"").append(nom).append("\"");
        sb.append(" [pos=").append(position);
        sb.append(", portee=").append(portee);
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