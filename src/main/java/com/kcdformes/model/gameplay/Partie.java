package com.kcdformes.model.gameplay;

import com.kcdformes.model.defense.Forteresse;
import com.kcdformes.model.defense.Tourelle;
import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.joueur.Joueur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Partie {

    private Difficulte difficulte;
    private int vagueActuelle;
    private EtatPartie etat;
    private Joueur joueur;
    private Carte carte;
    private Forteresse forteresse;
    private List<Vague> vagues;
    private Map<Integer, MurailleEnJeu> murailles;

    public Partie(Difficulte difficulte, Joueur joueur, Carte carte) {
        if (difficulte == null) throw new IllegalArgumentException("La difficulté ne peut pas être null.");
        if (joueur == null) throw new IllegalArgumentException("Le joueur ne peut pas être null.");
        if (carte == null) throw new IllegalArgumentException("La carte ne peut pas être null.");
        this.difficulte = difficulte;
        this.vagueActuelle = 0;
        this.etat = EtatPartie.EN_PAUSE;
        this.joueur = joueur;
        this.carte = carte;
        this.vagues = new ArrayList<>();
        this.murailles = new HashMap<>();
        appliquerBudgetInitial();
    }

    public void demarrer() {
        this.etat = EtatPartie.EN_COURS;
    }

    public void reprendre() {
        if (etat == EtatPartie.ENTRE_VAGUES) {
            lancerVagueSuivante();
            this.etat = EtatPartie.EN_COURS;
        }
    }

    private void appliquerBudgetInitial() {
        joueur.setBudget(difficulte.getBudgetInitial());
        this.forteresse = new Forteresse(
                "Citadelle",
                difficulte.getPvForteresse(),
                difficulte.getDefenseForteresse(),
                difficulte.getDegatsForteresse(),
                difficulte.getPorteeForteresse()
        );
    }

    // MURAILLES

    public void ajouterMuraille(int position, double largeur, double longueur, int pvMax) {
        murailles.put(position, new MurailleEnJeu(position, largeur, longueur, pvMax));
    }

    public Map<Integer, MurailleEnJeu> getMurailles() {
        return murailles;
    }

    // VAGUES

    public int getNombreAssauts() {
        return difficulte.getNombreVagues();
    }

    public boolean estDerniereVague() {
        return vagueActuelle >= vagues.size() - 1;
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
        if (vague == null) throw new IllegalArgumentException("La vague ne peut pas être null.");
        vagues.add(vague);
    }

    // GAME LOOP

    public void update() {
        if (etat != EtatPartie.EN_COURS) return;

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

        // PHASE 3 : SCORE DES MORTS
        for (Ennemi e : ennemisActifs) {
            if (!e.estVivant() && !e.isScoreComptabilise()) {
                joueur.ajouterScore(e.getPointsScore());
                e.setScoreComptabilise(true);
            }
        }

        // PHASE 4 : AVANCER LES VIVANTS (avec blocage muraille)

        for (Ennemi e : ennemisActifs) {
            if (!e.estVivant()) continue;

            int prochainePosition = e.getPosition() + Math.max(1, (int) e.getVitesse());

            // Ennemi déjà à la forteresse : il attaque chaque tick
            if (e.getPosition() >= tailleChemin - 1) {
                forteresse.subirAttaque(e);
                continue;
            }

            // Vérifier s'il y a une muraille sur le trajet
            MurailleEnJeu murailleBloquante = null;
            for (int pos = e.getPosition() + 1; pos <= prochainePosition; pos++) {
                MurailleEnJeu m = murailles.get(pos);
                if (m != null && !m.estDetruite()) {
                    murailleBloquante = m;
                    break;
                }
            }

            if (murailleBloquante != null) {
                e.setPosition(murailleBloquante.getPosition() - 1);
                double degats = e.getDegatsRempart() * e.getForme().getMultiplicateurMuraille();
                murailleBloquante.subirDegats(degats);
            } else {
                e.avancer();
                // S'il vient d'arriver à la forteresse, première attaque
                if (e.getPosition() >= tailleChemin - 1) {
                    if (e.getPosition() < tailleChemin - 1) {
                        e.avancer();
                    }
                    forteresse.subirAttaque(e);
                }
            }
        }
        // PHASE 5 : TIMER (sauf dernière vague)
        vague.tick();

        // PHASE 6 : VÉRIFIER FIN DE VAGUE
        if (vague.estTerminee()) {
            if (estDerniereVague()) {
                // Dernière vague terminée → vérifier victoire
                verifierFinPartie();
            } else {
                // Vague intermédiaire terminée → pause pour fortification
                etat = EtatPartie.ENTRE_VAGUES;
            }
            return;
        }

        // PHASE 7 : VÉRIFIER DÉFAITE
        verifierFinPartie();
    }

    public void verifierFinPartie() {
        if (forteresse.estDetruite()) {
            etat = EtatPartie.PERDU;
        } else if (estDerniereVague()
                && vagues.get(vagueActuelle).estTerminee()) {
            etat = EtatPartie.GAGNE;
        }
    }

    // GETTERS

    public Difficulte getDifficulte() { return difficulte; }
    public int getVagueActuelle() { return vagueActuelle; }
    public EtatPartie getEtat() { return etat; }
    public Joueur getJoueur() { return joueur; }
    public Carte getCarte() { return carte; }
    public Forteresse getForteresse() { return forteresse; }
    public List<Vague> getVagues() { return new ArrayList<>(vagues); }

    @Override
    public String toString() {
        return "Partie [difficulte=" + difficulte.name()
                + ", vague=" + vagueActuelle + "/" + vagues.size()
                + ", etat=" + etat
                + ", joueur=" + joueur.getNom()
                + ", murailles=" + murailles.size()
                + ", forteresse=" + forteresse.getPvActuels() + "/" + forteresse.getPvMax() + "]";
    }

    // CLASSE INTERNE

    public static class MurailleEnJeu {
        private int position;
        private double largeur;
        private double longueur;
        private int pvMax;
        private int pvActuels;

        public MurailleEnJeu(int position, double largeur, double longueur, int pvMax) {
            this.position = position;
            this.largeur = largeur;
            this.longueur = longueur;
            this.pvMax = pvMax;
            this.pvActuels = pvMax;
        }

        public void subirDegats(double degats) {
            pvActuels -= (int) degats;
            if (pvActuels < 0) pvActuels = 0;
        }

        public boolean estDetruite() {
            return pvActuels <= 0;
        }

        public int getPosition() { return position; }
        public int getPvMax() { return pvMax; }
        public int getPvActuels() { return pvActuels; }
    }
}