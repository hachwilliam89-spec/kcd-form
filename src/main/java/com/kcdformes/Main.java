package com.kcdformes;

import com.kcdformes.model.Triangle;
import com.kcdformes.model.Cercle;
import com.kcdformes.model.Rectangle;
import com.kcdformes.model.Tourelle;

public class Main {

    public static void main(String[] args) {

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

        // Tourelle avec Triangle + Cercle
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

        // Ajout muraille → combo
        System.out.println(" Ajout de la muraille (combo)");

        tour1.ajouterForme(muraille);
        System.out.println(tour1);
        System.out.println("  DPS total: " + String.format("%.1f", tour1.dpsTotal()));
        System.out.println("  PV: " + String.format("%.0f", tour1.getPV()));
        System.out.println("  Cout total: " + tour1.coutTotal() + " or");

        // Test validation
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
            System.out.println("Position neg : " + e.getMessage());
        }

        System.out.println("\nInstanciation reussie");
    }
}