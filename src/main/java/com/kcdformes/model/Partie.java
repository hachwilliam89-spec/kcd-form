package com.kcdformes.model;

import java.util.ArrayList;
import java.util.List;

public class Partie {

    private int difficulte;
    private int vagueActuelle;
    private EtatPartie etat;
    private Joueur joueur;
    private Carte carte;
    private List<Vague> vagues;

    public Partie(int difficulte, Joueur joueur, Carte carte) {
        setDifficulte(difficulte);
        this.vagueActuelle = 0;
        this.etat = EtatPartie.EN_PAUSE;
        this.joueur = joueur;
        this.carte = carte;
        this.vagues = new ArrayList<>();
        appliquerBudgetInitial();
    }

    // BUDGET PAR DIFFICULTE

    private void appliquerBudgetInitial() {
        switch (difficulte) {
            case 1 -> joueur.setBudget(500);
            case 2 -> joueur.setBudget(400);
            case 3 -> joueur.setBudget(300);
        }
    }

    public int getNombreAssauts() {
        return switch (difficulte) {
            case 1 -> 5;
            case 2 -> 8;
            case 3 -> 12;
            default -> 5;
        };
    }

    // GAMEPLAY

    public void demarrer() {
        this.etat = EtatPartie.EN_COURS;
        this.vagueActuelle = 1;
    }

    public void lancerVagueSuivante() {
        if (vagueActuelle < vagues.size()) {
            vagueActuelle++;
        }
    }

    public void ajouterVague(Vague vague) {
        if (vague == null) {
            throw new IllegalArgumentException("La vague ne peut pas être null.");
        }
        vagues.add(vague);
    }

    public void update() {
        if (etat != EtatPartie.EN_COURS) {
            return;
        }

        Vague vague = vagues.get(vagueActuelle - 1);

        Ennemi ennemi = vague.spawnSuivant();

        for (Ennemi e : vague.getEnnemis()) {
            if (!e.estVivant()) continue;

            List<Tourelle> tourellesEnPortee = carte.getTourellesEnPortee(e.getPosition());
            for (Tourelle t : tourellesEnPortee) {
                e.subirDegats(t.degatsContre(e));
            }

            if (e.estVivant()) {
                e.avancer();
            } else {
                joueur.gagner(e.getRecompense());
            }

            if (e.getPosition() >= carte.getChemin().size()) {
                joueur.perdreVie();
            }
        }

        verifierFinPartie();
    }

    public void verifierFinPartie() {
        if (joueur.estElimine()) {
            etat = EtatPartie.PERDU;
        } else if (vagueActuelle >= vagues.size()
                && vagues.get(vagues.size() - 1).estTerminee()) {
            etat = EtatPartie.GAGNE;
        }
    }

    // GETTERS

    public int getDifficulte() {
        return difficulte;
    }

    public int getVagueActuelle() {
        return vagueActuelle;
    }

    public EtatPartie getEtat() {
        return etat;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public Carte getCarte() {
        return carte;
    }

    public List<Vague> getVagues() {
        return new ArrayList<>(vagues);
    }

    // SETTERS

    public void setDifficulte(int difficulte) {
        if (difficulte < 1 || difficulte > 3) {
            throw new IllegalArgumentException("La difficulté doit être entre 1 et 3.");
        }
        this.difficulte = difficulte;
    }

    @Override
    public String toString() {
        return "Partie [difficulte=" + difficulte
                + ", vague=" + vagueActuelle + "/" + vagues.size()
                + ", etat=" + etat
                + ", joueur=" + joueur.getNom() + "]";
    }
}