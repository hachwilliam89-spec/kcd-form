package com.kcdformes.model.gameplay;

import com.kcdformes.model.defense.Tourelle;
import com.kcdformes.model.formes.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Carte {

    private String nom;
    private int largeur;
    private int hauteur;
    private List<Integer> chemin;
    private List<Integer> emplacementsTourelles;  // hors chemin
    private List<Integer> emplacementsMurailles;  // sur le chemin
    private List<Tourelle> tourelles;
    private Map<Integer, Rectangle> murailles;


    public Carte(String nom, int largeur, int hauteur) {
        setNom(nom);
        setLargeur(largeur);
        setHauteur(hauteur);
        this.chemin = new ArrayList<>();
        this.emplacementsTourelles = new ArrayList<>();
        this.emplacementsMurailles = new ArrayList<>();
        this.tourelles = new ArrayList<>();
        this.murailles = new HashMap<>();
    }

    // GESTION DU CHEMIN

    public void setChemin(List<Integer> chemin) {
        if (chemin == null || chemin.isEmpty()) {
            throw new IllegalArgumentException("Le chemin ne peut pas être vide.");
        }
        this.chemin = new ArrayList<>(chemin);
    }

    public void setEmplacementsTourelles(List<Integer> emplacements) {
        if (emplacements == null) {
            throw new IllegalArgumentException("Les emplacements ne peuvent pas être null.");
        }
        this.emplacementsTourelles = new ArrayList<>(emplacements);
    }

    public void setEmplacementsMurailles(List<Integer> emplacements) {
        if (emplacements == null) {
            throw new IllegalArgumentException("Les emplacements ne peuvent pas être null.");
        }
        this.emplacementsMurailles = new ArrayList<>(emplacements);
    }

    // GESTION DES TOURELLES

    public boolean placerTourelle(Tourelle tourelle, int emplacement) {
        if (tourelle == null) {
            throw new IllegalArgumentException("La tourelle ne peut pas être null.");
        }
        if (!emplacementsTourelles.contains(emplacement)) {
            return false;
        }
        for (Tourelle t : tourelles) {
            if (t.getPosition() == emplacement) {
                return false;
            }
        }
        tourelle.setPosition(emplacement);
        return tourelles.add(tourelle);
    }

    public boolean supprimerTourelle(int emplacement) {
        return tourelles.removeIf(t -> t.getPosition() == emplacement);
    }

    // GESTION DES MURAILLES

    public boolean placerMuraille(Rectangle muraille, int emplacement) {
        if (muraille == null) {
            throw new IllegalArgumentException("La muraille ne peut pas être null.");
        }
        if (!emplacementsMurailles.contains(emplacement)) {
            return false;
        }
        if (murailles.containsKey(emplacement)) {
            return false;
        }
        murailles.put(emplacement, muraille);
        return true;
    }

    public List<Rectangle> getMuraillesSurChemin(int positionEnnemi) {
        List<Rectangle> surChemin = new ArrayList<>();
        if (murailles.containsKey(positionEnnemi)) {
            surChemin.add(murailles.get(positionEnnemi));
        }
        return surChemin;
    }

    // PORTEE DES TOURELLES

    public List<Tourelle> getTourellesEnPortee(int positionEnnemi) {
        List<Tourelle> enPortee = new ArrayList<>();
        for (Tourelle t : tourelles) {
            if (Math.abs(t.getPosition() - positionEnnemi) <= t.getPortee()) {
                enPortee.add(t);
            }
        }
        return enPortee;
    }

    // GETTERS

    public String getNom() {
        return nom;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public List<Integer> getChemin() {
        return new ArrayList<>(chemin);
    }

    public List<Integer> getEmplacementsTourelles() {
        return new ArrayList<>(emplacementsTourelles);
    }

    public List<Integer> getEmplacementsMurailles() {
        return new ArrayList<>(emplacementsMurailles);
    }

    public List<Tourelle> getTourelles() {
        return new ArrayList<>(tourelles);
    }

    public Map<Integer, Rectangle> getMurailles() {
        return new HashMap<>(murailles);
    }

    // SETTERS

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.nom = nom;
    }

    public void setLargeur(int largeur) {
        if (largeur <= 0) {
            throw new IllegalArgumentException("La largeur doit être positive.");
        }
        this.largeur = largeur;
    }

    public void setHauteur(int hauteur) {
        if (hauteur <= 0) {
            throw new IllegalArgumentException("La hauteur doit être positive.");
        }
        this.hauteur = hauteur;
    }

    @Override
    public String toString() {
        return "Carte " + nom + " [" + largeur + "x" + hauteur
                + ", tourelles=" + tourelles.size()
                + ", murailles=" + murailles.size()
                + ", chemin=" + chemin.size() + " cases]";
    }
}