package com.kcdformes.model;

public enum Difficulte {

    ECUYER(1, 500, 5, 60, 960, 25, 30, 2),
    CHEVALIER(2, 400, 5, 60, 960, 20, 25, 2),
    SEIGNEUR(3, 300, 5, 45, 640, 15, 20, 1);

    private final int niveau;
    private final int budgetInitial;
    private final int nombreVagues;
    private final int dureeVagueSecondes;
    private final int pvForteresse;
    private final int defenseForteresse;
    private final int degatsForteresse;
    private final int porteeForteresse;

    Difficulte(int niveau, int budgetInitial, int nombreVagues, int dureeVagueSecondes,
               int pvForteresse, int defenseForteresse, int degatsForteresse, int porteeForteresse) {
        this.niveau = niveau;
        this.budgetInitial = budgetInitial;
        this.nombreVagues = nombreVagues;
        this.dureeVagueSecondes = dureeVagueSecondes;
        this.pvForteresse = pvForteresse;
        this.defenseForteresse = defenseForteresse;
        this.degatsForteresse = degatsForteresse;
        this.porteeForteresse = porteeForteresse;
    }

    // GETTERS

    public int getNiveau() { return niveau; }
    public int getBudgetInitial() { return budgetInitial; }
    public int getNombreVagues() { return nombreVagues; }
    public int getDureeVagueSecondes() { return dureeVagueSecondes; }
    public int getPvForteresse() { return pvForteresse; }
    public int getDefenseForteresse() { return defenseForteresse; }
    public int getDegatsForteresse() { return degatsForteresse; }
    public int getPorteeForteresse() { return porteeForteresse; }
}