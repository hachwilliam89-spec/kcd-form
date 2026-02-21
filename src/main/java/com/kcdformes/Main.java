package com.kcdformes;

import com.kcdformes.model.Triangle;
import com.kcdformes.model.Cercle;
import com.kcdformes.model.Rectangle;
import com.kcdformes.model.Tourelle;

public class Main {

    public static void main(String[] args) {

        System.out.println("Instanciation des formes \n");

        // Formes
        Triangle archer = new Triangle("Archer", 4, 3);
        Cercle catapulte = new Cercle("Catapulte", 3);
        Rectangle muraille = new Rectangle("Muraille", 4, 4);

        System.out.println(archer);
        System.out.println("  Aire: " + archer.aire() + " | DPS: " + archer.dps() + " | Cout: " + archer.cout() + " or");
        System.out.println(catapulte);
        System.out.println("  Aire: " + catapulte.aire() + " | DPS: " + catapulte.dps() + " | Cout: " + catapulte.cout() + " or");
        System.out.println(muraille);
        System.out.println("  Aire: " + muraille.aire() + " | PV: " + muraille.getPv() + " | Cout: " + muraille.cout() + " or");

        // Tourelle (Compo + Poly)
        System.out.println("\n TOURELLE \n");

        Tourelle tour1 = new Tourelle("Tour A", 3);
        tour1.ajouterForme(archer);
        tour1.ajouterForme(catapulte);

        System.out.println(tour1);
        System.out.println("  Aire totale: " + tour1.aireTotale());
        System.out.println("  DPS total: " + tour1.dpsTotal());
        System.out.println("  Cout total: " + tour1.coutTotal() + " or");
        System.out.println("  Nombre de formes: " + tour1.getNombreFormes());

        // Ajout muraille
        tour1.ajouterForme(muraille);
        System.out.println("\nApres ajout muraille :");
        System.out.println(tour1);
        System.out.println("  DPS total: " + tour1.dpsTotal());
        System.out.println("  Cout total: " + tour1.coutTotal() + " or");

        System.out.println("\n Instanciation reussie");
    }
}