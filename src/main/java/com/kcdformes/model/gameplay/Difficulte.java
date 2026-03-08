package com.kcdformes.model.gameplay;

public enum Difficulte {

    ECUYER(1, 1000, 5, 45, 800, 40, 40, 2),
    CHEVALIER(2, 700, 5, 45, 640, 30, 30, 2),
    SEIGNEUR(3, 400, 5, 30, 480, 20, 20, 1);

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