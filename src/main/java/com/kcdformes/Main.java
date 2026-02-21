package com.kcdformes;

import com.kcdformes.model.Triangle;
import com.kcdformes.model.Cercle;
import com.kcdformes.model.Rectangle;

public class Main {

    public static void main(String[] args) {

        System.out.println("Instanciation des formes\n");

        // Triangle
        Triangle archer = new Triangle("Archer", 10, 8);
        System.out.println("TRIANGLE (Archer)");
        System.out.println(archer);
        System.out.println("Aire     : " + archer.aire());
        System.out.println("Perimetre: " + archer.perimetre());
        System.out.println();

        // Cercle
        Cercle catapulte = new Cercle("Catapulte", 6);
        System.out.println("CERCLE (Catapulte)");
        System.out.println(catapulte);
        System.out.println("Aire     : " + catapulte.aire());
        System.out.println("Perimetre: " + catapulte.perimetre());
        System.out.println();

        // Rectangle
        Rectangle muraille = new Rectangle("Muraille", 12, 6);
        System.out.println("RECTANGLE (Muraille)");
        System.out.println(muraille);
        System.out.println("Aire     : " + muraille.aire());
        System.out.println("Perimetre: " + muraille.perimetre());
        System.out.println();

        System.out.println("Instanciation reussie");
    }
}