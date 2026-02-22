package com.kcdformes;

import com.kcdformes.model.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println(" KCD FORMES ");

        // Joueur
        Joueur joueur = new Joueur("Kim", 0, 5);

        // Carte
        Carte carte = new Carte("Niveau 1", 10, 10);
        carte.setChemin(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        carte.setEmplacementsTourelles(List.of(2, 5, 8));
        carte.setEmplacementsMurailles(List.of(3, 6));

        // Partie (le budget est appliqué selon la difficulté)
        Partie partie = new Partie(1, joueur, carte);
        System.out.println(joueur);

        // PHASE DE CONSTRUCTION
        System.out.println(" PHASE DE CONSTRUCTION ");

        // Tour A : Archer + Catapulte
        Tourelle tour1 = new Tourelle("Tour A", 0);

        Triangle archer1 = new Triangle("Archer", 4, 3);
        if (joueur.depenser(archer1.cout())) {
            tour1.ajouterForme(archer1);
            System.out.println("  Archer ajouté à Tour A ! -" + archer1.cout() + " or");
        }

        Cercle catapulte1 = new Cercle("Catapulte", 3);
        if (joueur.depenser(catapulte1.cout())) {
            tour1.ajouterForme(catapulte1);
            System.out.println("  Catapulte ajoutée à Tour A ! -" + catapulte1.cout() + " or");
        }

        carte.placerTourelle(tour1, 2);
        tour1.setOrientation(90);

        // Tour B : Archer seul
        Tourelle tour2 = new Tourelle("Tour B", 0);

        Triangle archer2 = new Triangle("Archer2", 4, 3);
        if (joueur.depenser(archer2.cout())) {
            tour2.ajouterForme(archer2);
            System.out.println("  Archer ajouté à Tour B ! -" + archer2.cout() + " or");
        }

        carte.placerTourelle(tour2, 5);
        tour2.setOrientation(270);

        // Muraille sur le chemin
        Rectangle muraille = new Rectangle("Muraille", 4, 4);
        if (joueur.depenser(muraille.cout())) {
            carte.placerMuraille(muraille, 3);
            System.out.println("  Muraille placée en position 3 ! -" + muraille.cout() + " or");
        }

        System.out.println("\n" + tour1);
        System.out.println(tour2);
        System.out.println("Budget restant: " + joueur.getBudget() + " or");

        // PHASE DE COMBAT
        System.out.println(" PHASE DE COMBAT ");

        Vague vague1 = new Vague(1, 1.0, 1.0);
        vague1.ajouterEnnemi(new Ennemi("Cavalerie", new Triangle("Tri", 4, 3)));
        vague1.ajouterEnnemi(new Ennemi("Infanterie", new Cercle("Cer", 3)));
        vague1.ajouterEnnemi(new Ennemi("Belier", new Rectangle("Rec", 4, 4)));

        System.out.println("Vague 1 : " + vague1.getNombreEnnemis() + " ennemis");
        for (Ennemi e : vague1.getEnnemis()) {
            System.out.println("  " + e);
        }

        partie.ajouterVague(vague1);
        partie.demarrer();

        // SIMULATION
        System.out.println(" SIMULATION ");

        for (Ennemi e : vague1.getEnnemis()) {
            System.out.println(e.getNom() + " avance...");

            for (int tour = 0; tour < 10; tour++) {
                e.avancer();

                // Tourelles tirent
                List<Tourelle> enPortee = carte.getTourellesEnPortee(e.getPosition());
                for (Tourelle t : enPortee) {
                    if (t.estZone()) {
                        // Catapulte : dégâts à tous les ennemis dans la zone (simulé ici sur l'ennemi courant)
                        e.subirDegats(t.degatsContre(e));
                        System.out.println("  " + t.getNom() + " (ZONE) tire sur " + e.getNom()
                                + " → PV: " + e.getPvActuels() + "/" + e.getPvMax());
                    } else {
                        // Archer : mono-cible
                        e.subirDegats(t.degatsContre(e));
                        System.out.println("  " + t.getNom() + " (MONO) tire sur " + e.getNom()
                                + " → PV: " + e.getPvActuels() + "/" + e.getPvMax());
                    }
                }

                if (!e.estVivant()) {
                    joueur.gagner(e.getRecompense());
                    System.out.println("  " + e.getNom() + " éliminé ! +" + e.getRecompense() + " or");
                    break;
                }
            }

            if (e.estVivant()) {
                joueur.perdreVie();
                System.out.println("  " + e.getNom() + " a atteint la forteresse ! Vies: " + joueur.getVies());
            }
        }

        // RESULTAT
        partie.verifierFinPartie();
        System.out.println(" RESULTAT ");
        System.out.println(partie);
        System.out.println(joueur);
        System.out.println("Score final: " + joueur.getScore());
    }
}