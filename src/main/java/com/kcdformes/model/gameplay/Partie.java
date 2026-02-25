package com.kcdformes.model.gameplay;

import com.kcdformes.model.defense.Forteresse;
import com.kcdformes.model.defense.Tourelle;
import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.joueur.Joueur;

import java.util.ArrayList;
import java.util.List;

public class Partie {

    private Difficulte difficulte;
    private int vagueActuelle;
    private EtatPartie etat;
    private Joueur joueur;
    private Carte carte;
    private Forteresse forteresse;
    private List<Vague> vagues;

    public Partie(Difficulte difficulte, Joueur joueur, Carte carte) {
        if (difficulte == null) {
            throw new IllegalArgumentException("La difficulté ne peut pas être null.");
        }
        if (joueur == null) {
            throw new IllegalArgumentException("Le joueur ne peut pas être null.");
        }
        if (carte == null) {
            throw new IllegalArgumentException("La carte ne peut pas être null.");
        }
        this.difficulte = difficulte;
        this.vagueActuelle = 0;
        this.etat = EtatPartie.EN_PAUSE;
        this.joueur = joueur;
        this.carte = carte;
        this.vagues = new ArrayList<>();
        appliquerBudgetInitial();
    }

    public void demarrer() {
        this.etat = EtatPartie.EN_COURS;
    }

    private void appliquerBudgetInitial() {
        joueur.setBudget(difficulte.getBudgetInitial());
        this.forteresse = new Forteresse("Citadelle",
                difficulte.getPvForteresse(), difficulte.getDefenseForteresse(),
                difficulte.getDegatsForteresse(), difficulte.getPorteeForteresse());
    }

    public int getNombreAssauts() {
        return difficulte.getNombreVagues();
    }

    public void lancerVagueSuivante() {
        Vague vagueCourante = vagues.get(this.vagueActuelle);

        List<Ennemi> survivants = vagueCourante.getEnnemisSurvivants();

        this.vagueActuelle++;

        if (this.vagueActuelle < vagues.size()) {
            Vague prochaine = vagues.get(this.vagueActuelle);
            prochaine.ajouterEnnemis(survivants);
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

        Vague vague = vagues.get(vagueActuelle);
        vague.spawnSuivant();

        int tailleChemin = carte.getChemin().size();
        List<Ennemi> ennemisActifs = vague.getEnnemisActifs();

        // PHASE 1 : TIRS DES TOURELLES
        for (Tourelle t : carte.getTourelles()) {
            List<Ennemi> enPortee = new ArrayList<>();
            for (Ennemi e : ennemisActifs) {
                if (e.estVivant() && Math.abs(t.getPosition() - e.getPosition()) <= t.getPortee()) {
                    enPortee.add(e);
                }
            }

            if (enPortee.isEmpty()) continue;

            if (t.hasAoE()) {
                for (Ennemi e : enPortee) {
                    e.subirDegats(t.degatsContre(e));
                }
            } else {
                Ennemi cible = enPortee.get(0);
                for (Ennemi e : enPortee) {
                    if (e.getPosition() > cible.getPosition()) {
                        cible = e;
                    }
                }
                cible.subirDegats(t.degatsContre(cible));
            }
        }

        // PHASE 2 : TIR DE LA FORTERESSE
        for (Ennemi e : ennemisActifs) {
            if (e.estVivant() && forteresse.estEnPortee(e.getPosition(), tailleChemin)) {
                e.subirDegats(forteresse.tirerSur(e));
            }
        }

        // PHASE 3 : SCORE DES MORTS (score uniquement, pas d'or)
        for (Ennemi e : ennemisActifs) {
            if (!e.estVivant() && !e.isRecompenseRecuperee()) {
                joueur.ajouterScore(e.getRecompense());
                e.setRecompenseRecuperee(true);
            }
        }

        // PHASE 4 : AVANCER LES VIVANTS
        for (Ennemi e : ennemisActifs) {
            if (!e.estVivant()) continue;

            e.avancer();

            if (e.getPosition() >= tailleChemin) {
                forteresse.subirAttaque(e);
                e.subirDegats(9999);
            }
        }

        // PHASE 5 : TIMER
        vague.tick();

        // PHASE 6 : VÉRIFIER FIN
        verifierFinPartie();
    }

    public void verifierFinPartie() {
        if (forteresse.estDetruite()) {
            etat = EtatPartie.PERDU;
        } else if (vagueActuelle >= vagues.size() - 1
                && vagues.get(vagues.size() - 1).estTerminee()) {
            etat = EtatPartie.GAGNE;
        }
    }

    // GETTERS

    public Difficulte getDifficulte() {
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

    @Override
    public String toString() {
        return "Partie [difficulte=" + difficulte.name()
                + ", vague=" + vagueActuelle + "/" + vagues.size()
                + ", etat=" + etat
                + ", joueur=" + joueur.getNom()
                + ", forteresse=" + forteresse.getPvActuels() + "/" + forteresse.getPvMax() + "]";
    }
}