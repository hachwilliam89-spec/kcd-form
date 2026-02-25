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

    // =============================================
    // GÉNÉRATION AUTOMATIQUE DES ESCOUADES
    // =============================================

    /**
     * Génère automatiquement les escouades à partir du nombre de chaque type.
     * Ordre : cavaliers → infanteries → béliers
     * Découpage en groupes de TAILLE_MAX_ESCOUADE max.
     */
    public void genererEscouades(int nbCavaliers, int nbInfanteries, int nbBeliers) {
        escouades.clear();
        ennemisSpawnes.clear();
        indexEscouade = 0;

        // Cavaliers : spawn rapide, premiers arrivés
        genererEscouadesParType(nbCavaliers, "Cavalier",
                () -> new Triangle("forme", 8, 6),
                DELAI_ENTRE_ENNEMIS);

        // Infanteries : milieu de vague
        genererEscouadesParType(nbInfanteries, "Infanterie",
                () -> new Cercle("forme", 4),
                DELAI_ENTRE_ENNEMIS);

        // Béliers : fin de vague, plus espacés
        genererEscouadesParType(nbBeliers, "Bélier",
                () -> new Rectangle("forme", 10, 6),
                DELAI_ENTRE_BELIERS);

        // Première escouade spawn immédiatement
        if (!escouades.isEmpty()) {
            escouades.get(0).setDelaiAvantSpawn(0);
        }
    }

    private void genererEscouadesParType(int nombre, String nomBase,
                                         FormeFactory factory, int delaiEntre) {
        if (nombre <= 0) return;

        int restant = nombre;
        while (restant > 0) {
            int taille = Math.min(restant, TAILLE_MAX_ESCOUADE);

            // Délai plus long avant les béliers
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

    /**
     * Interface fonctionnelle pour créer les formes des ennemis.
     */
    @FunctionalInterface
    private interface FormeFactory {
        com.kcdformes.model.formes.Forme creer();
    }

    // =============================================
    // AJOUT MANUEL (pour les survivants de la vague précédente)
    // =============================================

    public void ajouterEnnemis(List<Ennemi> survivants) {
        if (survivants == null || survivants.isEmpty()) return;

        // Les survivants forment une escouade en tête de vague (spawn immédiat)
        Escouade escouadeSurvivants = new Escouade(0, 1);
        for (Ennemi e : survivants) {
            escouadeSurvivants.ajouterEnnemi(e);
        }

        // Insérer en première position
        escouades.add(0, escouadeSurvivants);
    }

    // =============================================
    // SPAWN
    // =============================================

    /**
     * Tente de spawn le prochain ennemi de l'escouade en cours.
     * Passe automatiquement à l'escouade suivante quand la courante est terminée.
     */
    public Ennemi spawnSuivant() {
        if (indexEscouade >= escouades.size()) {
            return null;
        }

        Escouade escouadeCourante = escouades.get(indexEscouade);
        Ennemi ennemi = escouadeCourante.spawnSuivant();

        // Ennemi spawné → l'ajouter aux actifs
        if (ennemi != null) {
            ennemisSpawnes.add(ennemi);
        }

        // Escouade terminée → passer à la suivante
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
            return getNombreVivants() == 0 && toutSpawne();
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

    /**
     * Retourne tous les ennemis déjà spawnés (pour les tirs des tourelles).
     */
    public List<Ennemi> getEnnemisActifs() {
        return new ArrayList<>(ennemisSpawnes);
    }

    public int getNombreSpawnes() {
        return ennemisSpawnes.size();
    }

    public int calculerRecompense() {
        int total = 0;
        for (Escouade esc : escouades) {
            for (Ennemi e : esc.getEnnemis()) {
                if (!e.estVivant()) total += e.getRecompense();
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