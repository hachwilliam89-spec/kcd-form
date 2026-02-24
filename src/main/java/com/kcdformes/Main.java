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
        System.out.println("\n=== TOURELLE (COMPOSITION + POLYMORPHISME) ===\n");

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

        System.out.println("\n--- Ajout de la muraille (combo) ---\n");

        tour1.ajouterForme(muraille);
        System.out.println(tour1);
        System.out.println("  DPS total: " + String.format("%.1f", tour1.dpsTotal()));
        System.out.println("  PV: " + String.format("%.0f", tour1.getPV()));
        System.out.println("  Cout total: " + tour1.coutTotal() + " or");

        // === VALIDATION ===
        System.out.println("\n=== TEST VALIDATION ===\n");

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
        System.out.println("\n=== SIMULATION COMPLETE - 5 VAGUES / 800 ENNEMIS ===\n");

        Joueur joueur = new Joueur("Kim", 0, 5);
        Carte carte = new Carte("Plaine", 10, 10);
        carte.setChemin(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        carte.setEmplacementsTourelles(List.of(2, 5, 8));

        Partie partie = new Partie(1, joueur, carte);
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

        // Vague 1 : 50 cavaliers (rush rapide)
        Vague v1 = new Vague(1, 1.0, 1.0);
        for (int i = 0; i < 50; i++) {
            v1.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3)));
        }
        partie.ajouterVague(v1);

        // Vague 2 : 100 infanteries (horde)
        Vague v2 = new Vague(2, 1.0, 1.2);
        for (int i = 0; i < 100; i++) {
            v2.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2)));
        }
        partie.ajouterVague(v2);

        // Vague 3 : 150 mix
        Vague v3 = new Vague(3, 1.0, 1.3);
        for (int i = 0; i < 50; i++) {
            v3.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3)));
        }
        for (int i = 0; i < 60; i++) {
            v3.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2)));
        }
        for (int i = 0; i < 40; i++) {
            v3.ajouterEnnemi(new Ennemi("Belier " + (i + 1), new Rectangle("forme", 6, 3)));
        }
        partie.ajouterVague(v3);

        // Vague 4 : 200 offensive lourde
        Vague v4 = new Vague(4, 1.0, 1.5);
        for (int i = 0; i < 60; i++) {
            v4.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3)));
        }
        for (int i = 0; i < 80; i++) {
            v4.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2)));
        }
        for (int i = 0; i < 60; i++) {
            v4.ajouterEnnemi(new Ennemi("Belier " + (i + 1), new Rectangle("forme", 6, 3)));
        }
        partie.ajouterVague(v4);

        // Vague 5 : 300 assaut final
        Vague v5 = new Vague(5, 1.0, 2.0);
        for (int i = 0; i < 100; i++) {
            v5.ajouterEnnemi(new Ennemi("Cavalier " + (i + 1), new Triangle("forme", 4, 3)));
        }
        for (int i = 0; i < 100; i++) {
            v5.ajouterEnnemi(new Ennemi("Infanterie " + (i + 1), new Cercle("forme", 2)));
        }
        for (int i = 0; i < 100; i++) {
            v5.ajouterEnnemi(new Ennemi("Belier " + (i + 1), new Rectangle("forme", 6, 3)));
        }
        partie.ajouterVague(v5);

        // Lancer
        partie.demarrer();
        System.out.println("\n" + partie.getForteresse());

        for (int vague = 1; vague <= 5; vague++) {
            Vague vagueActuelle = partie.getVagues().get(vague - 1);
            System.out.println("\n====== VAGUE " + vague + " : " + vagueActuelle.getNombreEnnemis() + " ennemis ======");

            for (int tour = 1; tour <= 350; tour++) {
                partie.update();

                int vivants = vagueActuelle.getNombreVivants();

                // Afficher tous les 10 tours + premier + dernier
                if (tour == 1 || tour % 10 == 0 || vivants == 0 || partie.getEtat() != EtatPartie.EN_COURS) {
                    System.out.println("  Tour " + tour + " : vivants=" + vivants
                            + " | budget=" + joueur.getBudget()
                            + " | " + partie.getForteresse());
                }

                if (partie.getEtat() != EtatPartie.EN_COURS) {
                    System.out.println("\n  >>> " + partie.getEtat() + " <<<");
                    break;
                }

                if (vivants == 0) {
                    System.out.println("  Vague " + vague + " terminee !");
                    partie.lancerVagueSuivante();
                    break;
                }
            }

            if (partie.getEtat() != EtatPartie.EN_COURS) break;
        }

        System.out.println("\n=== RESULTAT FINAL ===");
        System.out.println("  Etat : " + partie.getEtat());
        System.out.println("  Score : " + joueur.getScore());
        System.out.println("  Budget final : " + joueur.getBudget() + " or");
        System.out.println("  " + partie.getForteresse());
    }
}