package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PartieTest {

    private Partie creerPartieBasique(int difficulte) {
        Joueur j = new Joueur("Kim", 0, 5);
        Carte c = new Carte("Niveau 1", 10, 10);
        c.setChemin(List.of(0, 1, 2, 3, 4, 5));
        c.setEmplacementsTourelles(List.of(2, 5));
        c.setEmplacementsMurailles(List.of(3));
        return new Partie(difficulte, j, c);
    }

    @Test
    void quandDifficulte1_alorsBudget500() {
        Partie p = creerPartieBasique(1);
        assertEquals(500, p.getJoueur().getBudget());
    }

    @Test
    void quandDifficulte2_alorsBudget400() {
        Partie p = creerPartieBasique(2);
        assertEquals(400, p.getJoueur().getBudget());
    }

    @Test
    void quandDifficulte3_alorsBudget300() {
        Partie p = creerPartieBasique(3);
        assertEquals(300, p.getJoueur().getBudget());
    }

    @Test
    void quandDifficulte1_alors5Assauts() {
        Partie p = creerPartieBasique(1);
        assertEquals(5, p.getNombreAssauts());
    }

    @Test
    void quandDifficulte2_alors8Assauts() {
        Partie p = creerPartieBasique(2);
        assertEquals(8, p.getNombreAssauts());
    }

    @Test
    void quandDifficulte3_alors12Assauts() {
        Partie p = creerPartieBasique(3);
        assertEquals(12, p.getNombreAssauts());
    }

    @Test
    void quandConstructeur_alorsEtatEnPause() {
        Partie p = creerPartieBasique(1);
        assertEquals(EtatPartie.EN_PAUSE, p.getEtat());
        assertEquals(0, p.getVagueActuelle());
    }

    @Test
    void quandDemarrer_alorsEtatEnCours() {
        Partie p = creerPartieBasique(1);
        p.demarrer();
        assertEquals(EtatPartie.EN_COURS, p.getEtat());
        assertEquals(1, p.getVagueActuelle());
    }

    @Test
    void quandAjouterVague_alorsListeAugmente() {
        Partie p = creerPartieBasique(1);
        p.ajouterVague(new Vague(1, 1.0, 1.0));
        assertEquals(1, p.getVagues().size());
    }

    @Test
    void quandJoueurElimine_alorsPartiePerdue() {
        Partie p = creerPartieBasique(1);
        p.demarrer();
        p.ajouterVague(new Vague(1, 1.0, 1.0));
        for (int i = 0; i < 5; i++) {
            p.getJoueur().perdreVie();
        }
        p.verifierFinPartie();
        assertEquals(EtatPartie.PERDU, p.getEtat());
    }

    @Test
    void quandDifficulteInvalide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            creerPartieBasique(5);
        });
    }

    @Test
    void quandAjouterVagueNull_alorsLeveIllegalArgument() {
        Partie p = creerPartieBasique(1);
        assertThrows(IllegalArgumentException.class, () -> {
            p.ajouterVague(null);
        });
    }

    @Test
    void quandGetVagues_alorsCopieDefensive() {
        Partie p = creerPartieBasique(1);
        p.ajouterVague(new Vague(1, 1.0, 1.0));
        p.getVagues().clear();
        assertEquals(1, p.getVagues().size());
    }

    @Test
    void quandToString_alorsContientInfos() {
        Partie p = creerPartieBasique(1);
        String str = p.toString();
        assertTrue(str.contains("difficulte=1"));
        assertTrue(str.contains("Kim"));
    }
}