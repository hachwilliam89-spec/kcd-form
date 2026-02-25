package com.kcdformes.model.joueur;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JoueurTest {

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        Joueur j = new Joueur("Kim", 200, 10);
        assertEquals("Kim", j.getNom());
        assertEquals(200, j.getBudget());
        assertEquals(10, j.getVies());
        assertEquals(0, j.getScore());
    }

    @Test
    void quandDepenserBudgetSuffisant_alorsRetourneTrueEtBudgetDiminue() {
        Joueur j = new Joueur("Kim", 200, 10);
        assertTrue(j.depenser(50));
        assertEquals(150, j.getBudget());
    }

    @Test
    void quandDepenserBudgetInsuffisant_alorsRetourneFalseEtBudgetInchange() {
        Joueur j = new Joueur("Kim", 200, 10);
        assertFalse(j.depenser(300));
        assertEquals(200, j.getBudget());
    }

    @Test
    void quandGagner50_alorsBudgetEtScoreAugmentent() {
        Joueur j = new Joueur("Kim", 200, 10);
        j.gagner(50);
        assertEquals(250, j.getBudget());
        assertEquals(50, j.getScore());
    }

    @Test
    void quandGagnerNegatif_alorsLeveIllegalArgument() {
        Joueur j = new Joueur("Kim", 200, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            j.gagner(-10);
        });
    }

    @Test
    void quandPerdreVie_alorsViesDiminuent() {
        Joueur j = new Joueur("Kim", 200, 3);
        j.perdreVie();
        assertEquals(2, j.getVies());
    }

    @Test
    void quandViesRestantes_alorsPasElimine() {
        Joueur j = new Joueur("Kim", 200, 3);
        assertFalse(j.estElimine());
    }

    @Test
    void quandPlusDeVies_alorsElimine() {
        Joueur j = new Joueur("Kim", 200, 1);
        j.perdreVie();
        assertTrue(j.estElimine());
    }

    @Test
    void quandSetBudget300_alorsBudgetEgale300() {
        Joueur j = new Joueur("Kim", 200, 10);
        j.setBudget(300);
        assertEquals(300, j.getBudget());
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Joueur("", 200, 10);
        });
    }

    @Test
    void quandBudgetNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Joueur("Kim", -100, 10);
        });
    }

    @Test
    void quandViesesNegatives_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Joueur("Kim", 200, -1);
        });
    }

    @Test
    void quandToString_alorsContientInfos() {
        Joueur j = new Joueur("Kim", 200, 5);
        String str = j.toString();
        assertTrue(str.contains("Kim"));
        assertTrue(str.contains("budget=200"));
        assertTrue(str.contains("vies=5"));
    }
}