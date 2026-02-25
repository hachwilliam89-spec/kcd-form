package com.kcdformes;

import com.kcdformes.model.Triangle;
import com.kcdformes.model.Cercle;
import com.kcdformes.model.Rectangle;
import com.kcdformes.model.Tourelle;
import com.kcdformes.model.Joueur;
import com.kcdformes.model.Carte;
import com.kcdformes.model.Partie;
import com.kcdformes.model.Vague;
import com.kcdformes.model.Ennemi;
import com.kcdformes.model.EtatPartie;
import com.kcdformes.model.Difficulte;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // === FORMES ===
        System.out.println("=== INSTANCIATION DES FORMES ===\n");

        Triangle archer = new Triangle("Archer", 4, 3);
        Cercle catapulte = new Cercle("Catapulte", 3);
        Rectangle muraille = new Rectangle("Muraille", 4, 4);

        System.out.println(archer);
        System.out.println("  Aire: " + String.format("%.1f", archer.aire())
                + " | DPS: " + String.format("%.1f", archer.dps())
                + " | Cout: " + archer.cout() + " or");

        System.out.println(catapulte);
        System.out.println("  Aire: " + String.format("%.1f", catapulte.aire())
                + " | DPS: " + String.format("%.1f", catapulte.dps())
                + " | Cout: " + catapulte.cout() + " or");

        System.out.println(muraille);
        System.out.println("  Aire: " + String.format("%.1f", muraille.aire())
                + " | PV: " + muraille.getPv()
                + " | Cout: " + muraille.cout() + " or");

        // === TOURELLE ===
        System.out.println("TOURELLE (COMPOSITION + POLYMORPHISME)");

        Tourelle tour1 = new Tourelle("Tour A", 3);
        tour1.ajouterForme(archer);
        tour1.ajouterForme(catapulte);

        System.out.println(tour1);
        System.out.println("  Aire totale: " + String.format("%.1f", tour1.aireTotale()));
        System.out.println("  DPS total: " + String.format("%.1f", tour1.dpsTotal()));
        System.out.println("  Cout total: " + tour1.coutTotal() + " or");
        System.out.println("  Tirs: " + tour1.getNombreTirs());
        System.out.println("  AoE: " + tour1.hasAoE() + " | Rayon: " + String.format("%.1f", tour1.getRayonZone()));
        System.out.println("  PV: " + String.format("%.0f", tour1.getPV()));

        System.out.println("Ajout de la muraille (combo) ");

        tour1.ajouterForme(muraille);
        System.out.println(tour1);
        System.out.println("  DPS total: " + String.format("%.1f", tour1.dpsTotal()));
        System.out.println("  PV: " + String.format("%.0f", tour1.getPV()));
        System.out.println("  Cout total: " + tour1.coutTotal() + " or");

        // === VALIDATION ===
        System.out.println("TEST VALIDATION");

        try {
            tour1.ajouterForme(new Triangle("Extra", 2, 3));
        } catch (IllegalStateException e) {
            System.out.println("Max formes : " + e.getMessage());
        }

        try {
            tour1.ajouterForme(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Forme null : " + e.getMessage());
        }

        try {
            new Tourelle("", 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Nom vide : " + e.getMessage());
        }

        try {
            new Tourelle("Test", -1);
        } catch (IllegalArgumentException e) {
            System.out.println("Position negative : " + e.getMessage());
        }

        // === SIMULATION COMPLETE ===
        Difficulte diff = Difficulte.CHEVALIER;
        System.out.println("\n=== SIMULATION - " + diff.name() + " (" + diff.getNombreVagues() + " vagues) ===\n");

        Joueur joueur = new Joueur("Kim", 0, 5);
        Carte carte = new Carte("Plaine", 10, 10);
        carte.setChemin(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        carte.setEmplacementsTourelles(List.of(2, 5, 8));

        Partie partie = new Partie(diff, joueur, carte);
        System.out.println("Budget : " + joueur.getBudget() + " or");

        // Placement des tourelles
        Tourelle t1 = new Tourelle("Archer Alpha", 0);
        t1.ajouterForme(new Triangle("Arc", 4, 3));
        t1.ajouterForme(new Triangle("Arc2", 4, 3));
        carte.placerTourelle(t1, 2);

        Tourelle t2 = new Tourelle("Catapulte Beta", 0);
        t2.ajouterForme(new Cercle("Boulet", 3));
        t2.ajouterForme(new Cercle("Boulet2", 3));
        carte.placerTourelle(t2, 5);

        Tourelle t3 = new Tourelle("Mixte Gamma", 0);
        t3.ajouterForme(new Triangle("Arc", 4, 3));
        t3.ajouterForme(new Cercle("Boulet", 3));
        t3.ajouterForme(new Rectangle("Mur", 4, 4));
        carte.placerTourelle(t3, 8);

        System.out.println("Tourelles placees :");
        System.out.println("  " + t1);
        System.out.println("  " + t2);
        System.out.println("  " + t3);

        int dureeVague = diff.getDureeVagueSecondes();

        // Vague 1 : 30 ennemis
        Vague v1 = new Vague(1, 1.0, 1.0, dureeVague);
        for (int i = 0; i < 15; i++) {
            v1.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3), v1.getCoeffDifficulte()));
        }
        for (int i = 0; i < 10; i++) {
            v1.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2), v1.getCoeffDifficulte()));
        }
        for (int i = 0; i < 5; i++) {
            v1.ajouterEnnemi(new Ennemi("Belier " + (i + 1), new Rectangle("forme", 6, 3), v1.getCoeffDifficulte()));
        }
        partie.ajouterVague(v1);

        // Vague 2 : 60 ennemis
        Vague v2 = new Vague(2, 1.0, 1.2, dureeVague);
        for (int i = 0; i < 30; i++) {
            v2.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3), v2.getCoeffDifficulte()));
        }
        for (int i = 0; i < 20; i++) {
            v2.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2), v2.getCoeffDifficulte()));
        }
        for (int i = 0; i < 10; i++) {
            v2.ajouterEnnemi(new Ennemi("Belier " + (i + 1), new Rectangle("forme", 6, 3), v2.getCoeffDifficulte()));
        }
        partie.ajouterVague(v2);

        // Vague 3 : 80 ennemis
        Vague v3 = new Vague(3, 1.0, 1.3, dureeVague);
        for (int i = 0; i < 40; i++) {
            v3.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3), v3.getCoeffDifficulte()));
        }
        for (int i = 0; i < 25; i++) {
            v3.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2), v3.getCoeffDifficulte()));
        }
        for (int i = 0; i < 15; i++) {
            v3.ajouterEnnemi(new Ennemi("Belier " + (i + 1), new Rectangle("forme", 6, 3), v3.getCoeffDifficulte()));
        }
        partie.ajouterVague(v3);

        // Vague 4 : 100 ennemis
        Vague v4 = new Vague(4, 1.0, 1.5, dureeVague);
        for (int i = 0; i < 40; i++) {
            v4.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3), v4.getCoeffDifficulte()));
        }
        for (int i = 0; i < 40; i++) {
            v4.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2), v4.getCoeffDifficulte()));
        }
        for (int i = 0; i < 20; i++) {
            v4.ajouterEnnemi(new Ennemi("Belier " + (i + 1), new Rectangle("forme", 6, 3), v4.getCoeffDifficulte()));
        }
        partie.ajouterVague(v4);

        // Vague 5 : 100 ennemis (VAGUE FINALE)
        Vague v5 = new Vague(5, 1.0, 2.0, dureeVague);
        v5.setDerniereVague(true);
        for (int i = 0; i < 60; i++) {
            v5.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3), v5.getCoeffDifficulte()));
        }
        for (int i = 0; i < 40; i++) {
            v5.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2), v5.getCoeffDifficulte()));
        }
        for (int i = 0; i < 20; i++) {
            v5.ajouterEnnemi(new Ennemi("Belier " + (i + 1), new Rectangle("forme", 6, 3), v5.getCoeffDifficulte()));
        }
        partie.ajouterVague(v5);

        int totalEnnemis = 20 + 40 + 60 + 80 + 100;
        System.out.println("Total ennemis : " + totalEnnemis);

        // Lancer
        partie.demarrer();
        System.out.println("\n" + partie.getForteresse());

        for (int vague = 1; vague <= diff.getNombreVagues(); vague++) {
            Vague vagueActuelle = partie.getVagues().get(vague - 1);

            if (vagueActuelle.isDerniereVague()) {
                System.out.println("\n====== VAGUE FINALE " + vague + " : " + vagueActuelle.getNombreEnnemis() + " ennemis — Eliminez-les tous ! ======");
            } else {
                System.out.println("\n====== VAGUE " + vague + " : " + vagueActuelle.getNombreEnnemis() + " ennemis (timer: " + dureeVague + "s) ======");
            }

            for (int tour = 1; tour <= 500; tour++) {
                partie.update();

                int vivants = vagueActuelle.getNombreVivants();

                if (tour == 1 || tour % 10 == 0 || vagueActuelle.estTerminee() || partie.getEtat() != EtatPartie.EN_COURS) {
                    String timerInfo = vagueActuelle.isDerniereVague()
                            ? "FINALE"
                            : "timer=" + vagueActuelle.getTempsEcoule() + "/" + dureeVague;
                    System.out.println("  Tour " + tour + " : vivants=" + vivants
                            + " | score=" + joueur.getScore()
                            + " | " + timerInfo
                            + " | " + partie.getForteresse());
                }

                if (partie.getEtat() != EtatPartie.EN_COURS) {
                    System.out.println("\n  >>> " + partie.getEtat() + " <<<");
                    break;
                }

                if (vagueActuelle.estTerminee()) {
                    if (vagueActuelle.isDerniereVague()) {
                        System.out.println("  VAGUE FINALE TERMINEE — Tous les ennemis elimines !");
                    } else {
                        System.out.println("  Timer expire ! Vague " + vague + " terminee — " + vivants + " survivants passent a la suivante");
                        partie.lancerVagueSuivante();
                    }
                    break;
                }
            }

            if (partie.getEtat() != EtatPartie.EN_COURS) break;
        }

        System.out.println("\n=== RESULTAT FINAL ===");
        System.out.println("  Etat : " + partie.getEtat());
        System.out.println("  Score : " + joueur.getScore());
        System.out.println("  " + partie.getForteresse());
    }
}