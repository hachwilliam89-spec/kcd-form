package com.kcdformes.model;

import java.util.ArrayList;
import java.util.List;

public class Partie {

    private int difficulte;
    private int vagueActuelle;
    private EtatPartie etat;
    private Joueur joueur;
    private Carte carte;
    private Forteresse forteresse;
    private List<Vague> vagues;

    public Partie(int difficulte, Joueur joueur, Carte carte) {
        setDifficulte(difficulte);
        if (joueur == null) {
            throw new IllegalArgumentException("Le joueur ne peut pas être null.");
        }
        if (carte == null) {
            throw new IllegalArgumentException("La carte ne peut pas être null.");
        }
        this.vagueActuelle = 0;
        this.etat = EtatPartie.EN_PAUSE;
        this.joueur = joueur;
        this.carte = carte;
        this.vagues = new ArrayList<>();
        appliquerBudgetInitial();
    }

    private void appliquerBudgetInitial() {
        switch (difficulte) {
            case 1 -> {
                joueur.setBudget(500);
                this.forteresse = new Forteresse("Citadelle", 960, 15, 25, 2);  // defense 15 → facile
            }
            case 2 -> {
                joueur.setBudget(400);
                this.forteresse = new Forteresse("Citadelle", 960, 10, 20, 2);  // defense 10 → moyen
            }
            case 3 -> {
                joueur.setBudget(300);
                this.forteresse = new Forteresse("Citadelle", 960, 5, 15, 1);   // defense 5 → difficile
            }
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
        vague.spawnSuivant();

        int tailleChemin = carte.getChemin().size();
        List<Ennemi> ennemisActifs = vague.getEnnemisActifs();

        //PHASE 1 : TIRS DES TOURELLES
        for (Tourelle t : carte.getTourelles()) {
            List<Ennemi> enPortee = new ArrayList<>();
            for (Ennemi e : ennemisActifs) {
                if (e.estVivant() && Math.abs(t.getPosition() - e.getPosition()) <= t.getPortee()) {
                    enPortee.add(e);
                }
            }

            if (enPortee.isEmpty()) continue;

            if (t.hasAoE()) {
                // Catapulte : tape tous les ennemis en portée
                for (Ennemi e : enPortee) {
                    e.subirDegats(t.degatsContre(e));
                }
            } else {
                // Archer : tape uniquement le plus avancé
                Ennemi cible = enPortee.get(0);
                for (Ennemi e : enPortee) {
                    if (e.getPosition() > cible.getPosition()) {
                        cible = e;
                    }
                }
                cible.subirDegats(t.degatsContre(cible));
            }
        }
            //PHASE 2 : TIR DE LA FORTERESSE
                 for (Ennemi e : ennemisActifs) {
                   if (e.estVivant() && forteresse.estEnPortee(e.getPosition(), tailleChemin)) {
                       e.subirDegats(forteresse.tirerSur(e));
                   }
        }

            //PHASE  : SCORE DES MORTS
        for (Ennemi e : ennemisActifs) {
            if (!e.estVivant() && !e.isRecompenseRecuperee()) {
                joueur.ajouterScore(e.getRecompense());
                e.setRecompenseRecuperee(true);
            }
        }



        //PHASE 3 : AVANCER LES VIVANTS
        for (Ennemi e : ennemisActifs) {
            if (!e.estVivant()) continue;

            e.avancer();

            if (e.getPosition() >= tailleChemin) {
                forteresse.subirAttaque(e);
                e.subirDegats(9999);
            }
        }


        // PHASE 4 : VÉRIFIER FIN
        verifierFinPartie();
    }

    public void verifierFinPartie() {
        if (forteresse.estDetruite()) {
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

    public Forteresse getForteresse() {
        return forteresse;
    }

    public List<Vague> getVagues() {
        return new ArrayList<>(vagues);
    }

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
                + ", joueur=" + joueur.getNom()
                + ", forteresse=" + forteresse.getPvActuels() + "/" + forteresse.getPvMax() + "]";
    }
}