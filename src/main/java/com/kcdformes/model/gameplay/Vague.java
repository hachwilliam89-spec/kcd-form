package com.kcdformes.model.gameplay;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;

import java.util.ArrayList;
import java.util.List;

public class Vague {

    private int numero;
    private List<Escouade> escouades;
    private int indexEscouade;
    private List<Ennemi> ennemisSpawnes;
    private double coeffDifficulte;
    private int dureeSecondes;
    private int tempsEcoule;
    private boolean derniereVague;

    // Constantes de découpage
    private static final int TAILLE_MAX_ESCOUADE = 5;
    private static final int DELAI_ENTRE_ESCOUADES = 4;
    private static final int DELAI_AVANT_BELIERS = 6;
    private static final int DELAI_ENTRE_ENNEMIS = 1;
    private static final int DELAI_ENTRE_BELIERS = 2;

    public Vague(int numero, double coeffDifficulte, int dureeSecondes) {
        setNumero(numero);
        setCoeffDifficulte(coeffDifficulte);
        setDureeSecondes(dureeSecondes);
        this.escouades = new ArrayList<>();
        this.ennemisSpawnes = new ArrayList<>();
        this.indexEscouade = 0;
        this.tempsEcoule = 0;
        this.derniereVague = false;
    }


    // GÉNÉRATION AUTOMATIQUE DES ESCOUADES


    public void genererEscouades(int nbCavaliers, int nbInfanteries, int nbBeliers) {
        escouades.clear();
        ennemisSpawnes.clear();
        indexEscouade = 0;

        genererEscouadesParType(nbCavaliers, "Cavalier",
                () -> new Triangle("forme", 8, 6),
                DELAI_ENTRE_ENNEMIS);

        genererEscouadesParType(nbInfanteries, "Infanterie",
                () -> new Cercle("forme", 4),
                DELAI_ENTRE_ENNEMIS);

        genererEscouadesParType(nbBeliers, "Bélier",
                () -> new Rectangle("forme", 10, 6),
                DELAI_ENTRE_BELIERS);

    }

    private void genererEscouadesParType(int nombre, String nomBase,
                                         FormeFactory factory, int delaiEntre) {
        if (nombre <= 0) return;

        int restant = nombre;
        while (restant > 0) {
            int taille = Math.min(restant, TAILLE_MAX_ESCOUADE);

            int delaiAvant = nomBase.equals("Bélier") ? DELAI_AVANT_BELIERS : DELAI_ENTRE_ESCOUADES;

            Escouade escouade = new Escouade(delaiAvant, delaiEntre);

            for (int i = 0; i < taille; i++) {
                int numero = (nombre - restant) + i + 1;
                escouade.ajouterEnnemi(
                        new Ennemi(nomBase + " " + numero, factory.creer(), coeffDifficulte)
                );
            }

            escouades.add(escouade);
            restant -= taille;
        }
    }

    public void initialiserPremiereEscouade() {
        if (!escouades.isEmpty()) {
            escouades.get(0).setDelaiAvantSpawn(0);
        }
    }

    public void ajouterDepuisFactory(com.kcdformes.factory.EnnemiFactory factory,
                                     int nombre, double coeff) {
        if (nombre <= 0) return;

        int restant = nombre;
        int compteur = 1;
        while (restant > 0) {
            int taille = Math.min(restant, TAILLE_MAX_ESCOUADE);
            String type = factory.getType();
            int delaiAvant = "RECTANGLE".equals(type) ? DELAI_AVANT_BELIERS : DELAI_ENTRE_ESCOUADES;
            int delaiEntre = "RECTANGLE".equals(type) ? DELAI_ENTRE_BELIERS : DELAI_ENTRE_ENNEMIS;

            Escouade escouade = new Escouade(delaiAvant, delaiEntre);
            for (int i = 0; i < taille; i++) {
                escouade.ajouterEnnemi(factory.creer(type + " " + compteur, coeff));
                compteur++;
            }
            escouades.add(escouade);
            restant -= taille;
        }

    }

    @FunctionalInterface
    private interface FormeFactory {
        com.kcdformes.model.formes.Forme creer();
    }

    // =============================================
    // AJOUT MANUEL (pour les survivants de la vague précédente)
    // =============================================

    public void ajouterEnnemis(List<Ennemi> survivants) {
        if (survivants == null || survivants.isEmpty()) return;

        Escouade escouadeSurvivants = new Escouade(0, 1);
        for (Ennemi e : survivants) {
            escouadeSurvivants.ajouterEnnemi(e);
        }

        escouades.add(0, escouadeSurvivants);
    }

    // =============================================
    // SPAWN
    // =============================================

    public Ennemi spawnSuivant() {
        if (indexEscouade >= escouades.size()) {
            return null;
        }

        Escouade escouadeCourante = escouades.get(indexEscouade);
        Ennemi ennemi = escouadeCourante.spawnSuivant();

        if (ennemi != null) {
            ennemisSpawnes.add(ennemi);
        }

        if (escouadeCourante.estTerminee()) {
            indexEscouade++;
        }

        return ennemi;
    }

    // TIMER

    public void tick() {
        tempsEcoule++;
    }

    public boolean estTerminee() {
        if (derniereVague) {
            // Dernière vague : finie quand tous morts et tous spawnés
            return getNombreVivants() == 0 && toutSpawne();
        }
        // Vagues intermédiaires : finie par timer OU tous les ennemis spawnés sont morts
        if (getNombreVivants() == 0 && toutSpawne()) {
            return true;
        }
        return tempsEcoule >= dureeSecondes;
    }

    private boolean toutSpawne() {
        return indexEscouade >= escouades.size();
    }

    // =============================================
    // STATISTIQUES
    // =============================================

    public int getNombreEnnemis() {
        int total = 0;
        for (Escouade e : escouades) {
            total += e.getNombreEnnemis();
        }
        return total;
    }

    public int getNombreVivants() {
        int count = 0;
        for (Escouade esc : escouades) {
            for (Ennemi e : esc.getEnnemis()) {
                if (e.estVivant()) count++;
            }
        }
        return count;
    }

    public List<Ennemi> getEnnemisSurvivants() {
        List<Ennemi> survivants = new ArrayList<>();
        for (Escouade esc : escouades) {
            for (Ennemi e : esc.getEnnemis()) {
                if (e.estVivant()) survivants.add(e);
            }
        }
        return survivants;
    }

    public List<Ennemi> getEnnemisActifs() {
        return new ArrayList<>(ennemisSpawnes);
    }

    public int getNombreSpawnes() {
        return ennemisSpawnes.size();
    }

    public int calculerPointsScore() {
        int total = 0;
        for (Escouade esc : escouades) {
            for (Ennemi e : esc.getEnnemis()) {
                if (!e.estVivant()) total += e.getPointsScore();
            }
        }
        return total;
    }

    // GETTERS

    public int getNumero() {
        return numero;
    }

    public List<Escouade> getEscouades() {
        return new ArrayList<>(escouades);
    }

    public double getCoeffDifficulte() {
        return coeffDifficulte;
    }

    public int getDureeSecondes() {
        return dureeSecondes;
    }

    public int getTempsEcoule() {
        return tempsEcoule;
    }

    public boolean isDerniereVague() {
        return derniereVague;
    }

    // SETTERS

    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Le numéro de vague doit être positif.");
        }
        this.numero = numero;
    }

    public void setCoeffDifficulte(double coeffDifficulte) {
        if (coeffDifficulte <= 0) {
            throw new IllegalArgumentException("Le coefficient de difficulté doit être positif.");
        }
        this.coeffDifficulte = coeffDifficulte;
    }

    public void setDureeSecondes(int dureeSecondes) {
        if (dureeSecondes <= 0) {
            throw new IllegalArgumentException("La durée doit être positive.");
        }
        this.dureeSecondes = dureeSecondes;
    }

    public void setDerniereVague(boolean derniereVague) {
        this.derniereVague = derniereVague;
    }

    @Override
    public String toString() {
        String timerInfo = derniereVague ? "FINALE" : "temps=" + tempsEcoule + "/" + dureeSecondes;
        return "Vague " + numero + " [escouades=" + escouades.size()
                + ", ennemis=" + getNombreEnnemis()
                + ", vivants=" + getNombreVivants()
                + ", " + timerInfo
                + ", coeff=" + coeffDifficulte + "]";
    }
}