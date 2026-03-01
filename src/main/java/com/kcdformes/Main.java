package com.kcdformes;

import com.kcdformes.model.formes.Triangle;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.defense.Tourelle;
import com.kcdformes.model.joueur.Joueur;
import com.kcdformes.model.gameplay.Carte;
import com.kcdformes.model.gameplay.Partie;
import com.kcdformes.model.gameplay.Vague;
import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.gameplay.EtatPartie;
import com.kcdformes.model.gameplay.Difficulte;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // FORMES
        System.out.println("INSTANCIATION DES FORMES");

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

        // TOURELLE
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

        // VALIDATION
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

        // SIMULATION PARTIE
        Difficulte diff = Difficulte.CHEVALIER;
        System.out.println("\nSIMULATION - " + diff.name() + " (" + diff.getNombreVagues() + " vagues)");

        Joueur joueur = new Joueur("Kim", 0, 5);
        Carte carte = new Carte("Plaine", 10, 10);
        carte.setChemin(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        carte.setEmplacementsTourelles(List.of(2, 5, 8));

        Partie partie = new Partie(diff, joueur, carte);
        System.out.println("Budget : " + joueur.getBudget() + " or");

        // Placement des tourelles

        Tourelle t1 = new Tourelle("Archer A", 0);
        t1.ajouterForme(new Triangle("Arc", 3, 2));
        t1.ajouterForme(new Triangle("Arc2", 3, 2));
        carte.placerTourelle(t1, 2);

        Tourelle t2 = new Tourelle("Catapulte B", 0);
        t2.ajouterForme(new Cercle("Boulet", 2));
        t2.ajouterForme(new Cercle("Boulet2", 2));
        carte.placerTourelle(t2, 5);

        Tourelle t3 = new Tourelle("Mixte C", 0);
        t3.ajouterForme(new Triangle("Arc", 3, 2));
        t3.ajouterForme(new Cercle("Boulet", 2));
        t3.ajouterForme(new Rectangle("Mur", 3, 3));
        carte.placerTourelle(t3, 8);

        System.out.println("Tourelles positionnés :");
        System.out.println("  " + t1);
        System.out.println("  " + t2);
        System.out.println("  " + t3);

        int dureeVague = diff.getDureeVagueSecondes();


        // VAGUES AVEC ESCOUADES


        // Vague 1 :
        Vague v1 = new Vague(1, 1.0, dureeVague);
        v1.genererEscouades(5, 3, 0);
        partie.ajouterVague(v1);

        // Vague 2 :
        Vague v2 = new Vague(2, 1.5, dureeVague);
        v2.genererEscouades(6, 4, 2);
        partie.ajouterVague(v2);

        // Vague 3 :
        Vague v3 = new Vague(3, 2.0, dureeVague);
        v3.genererEscouades(7, 6, 3);
        partie.ajouterVague(v3);

        // Vague 4 :
        Vague v4 = new Vague(4, 2.8, dureeVague);
        v4.genererEscouades(9, 8, 5);
        partie.ajouterVague(v4);

        // Vague 5 :
        Vague v5 = new Vague(5, 3.5, dureeVague);
        v5.setDerniereVague(true);
        v5.genererEscouades(12, 10, 6);
        partie.ajouterVague(v5);

        int totalEnnemis = 8 + 12 + 16 + 22 + 28;
        System.out.println("Total ennemis : " + totalEnnemis);

        // Afficher les escouades de chaque vague
        for (Vague v : partie.getVagues()) {
            System.out.println("\n" + v);
            for (int i = 0; i < v.getEscouades().size(); i++) {
                System.out.println("  " + (i + 1) + ". " + v.getEscouades().get(i));
            }
        }

        // Lancer
        partie.demarrer();
        System.out.println("\n" + partie.getForteresse());

        for (int vague = 1; vague <= diff.getNombreVagues(); vague++) {
            Vague vagueActuelle = partie.getVagues().get(vague - 1);

            if (vagueActuelle.isDerniereVague()) {
                System.out.println("\nVAGUE FINALE " + vague + " : " + vagueActuelle.getNombreEnnemis() + " ennemis — Eliminez-les tous !");
            } else {
                System.out.println("\nVAGUE " + vague + " : " + vagueActuelle.getNombreEnnemis() + " ennemis (timer: " + dureeVague + "s)");
            }

            for (int tour = 1; tour <= 500; tour++) {
                partie.update();

                int vivants = vagueActuelle.getNombreVivants();
                int spawnes = vagueActuelle.getNombreSpawnes();

                if (tour == 1 || tour % 10 == 0 || vagueActuelle.estTerminee() || partie.getEtat() != EtatPartie.EN_COURS) {
                    String timerInfo = vagueActuelle.isDerniereVague()
                            ? "FINALE"
                            : "timer=" + vagueActuelle.getTempsEcoule() + "/" + dureeVague;
                    System.out.println("  Tour " + tour + " : spawnes=" + spawnes + "/" + vagueActuelle.getNombreEnnemis()
                            + " | vivants=" + vivants
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

        System.out.println("RESULTAT FINAL");
        System.out.println("  Etat : " + partie.getEtat());
        System.out.println("  Score : " + joueur.getScore());
        System.out.println("  " + partie.getForteresse());
    }
}