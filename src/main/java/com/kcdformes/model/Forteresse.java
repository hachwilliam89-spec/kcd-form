package com.kcdformes.model;

public class Forteresse {

    private String nom;
    private int pvMax;
    private int pvActuels;
    private int defense;
    private double dps;
    private int portee;

    public Forteresse(String nom, int pvMax, int defense, double dps, int portee) {
        setNom(nom);
        setPvMax(pvMax);
        this.pvActuels = pvMax;
        setDefense(defense);
        setDps(dps);
        setPortee(portee);
    }

    // GAMEPLAY

    /**
     * Un ennemi attaque la forteresse avec ses dégâts fixes.
     * La defense réduit les dégâts mais ne les annule jamais complètement.
     */
    public void subirAttaque(Ennemi ennemi) {
        if (ennemi == null || !ennemi.estVivant()) {
            return;
        }

        int degats = Math.max(1, ennemi.getDegatsForteresse() - defense);
        pvActuels -= degats;
        if (pvActuels < 0) {
            pvActuels = 0;
        }
    }

    public double tirerSur(Ennemi ennemi) {
        if (ennemi == null || !ennemi.estVivant()) {
            return 0;
        }
        return dps;
    }

    public boolean estEnPortee(int positionEnnemi, int tailleChemin) {
        return positionEnnemi >= tailleChemin - portee;
    }

    public boolean estDetruite() {
        return pvActuels <= 0;
    }

    public double getPourcentageVie() {
        return (double) pvActuels / pvMax * 100;
    }

    // GETTERS

    public String getNom() {
        return nom;
    }

    public int getPvMax() {
        return pvMax;
    }

    public int getPvActuels() {
        return pvActuels;
    }

    public int getDefense() {
        return defense;
    }

    public double getDps() {
        return dps;
    }

    public int getPortee() {
        return portee;
    }

    // SETTERS

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.nom = nom;
    }

    public void setPvMax(int pvMax) {
        if (pvMax <= 0) {
            throw new IllegalArgumentException("Les PV max doivent être positifs.");
        }
        this.pvMax = pvMax;
    }

    public void setDefense(int defense) {
        if (defense < 0) {
            throw new IllegalArgumentException("La défense ne peut pas être négative.");
        }
        this.defense = defense;
    }

    public void setDps(double dps) {
        if (dps < 0) {
            throw new IllegalArgumentException("Le DPS ne peut pas être négatif.");
        }
        this.dps = dps;
    }

    public void setPortee(int portee) {
        if (portee <= 0) {
            throw new IllegalArgumentException("La portée doit être positive.");
        }
        this.portee = portee;
    }

    @Override
    public String toString() {
        return "Forteresse \"" + nom + "\" [PV=" + pvActuels + "/" + pvMax
                + ", defense=" + defense
                + ", DPS=" + String.format("%.1f", dps)
                + ", portee=" + portee
                + ", etat=" + String.format("%.0f", getPourcentageVie()) + "%]";
    }
}