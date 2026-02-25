package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DifficulteTest {

    // ECUYER

    @Test
    void quandEcuyer_alorsBudget500() {
        assertEquals(500, Difficulte.ECUYER.getBudgetInitial());
    }

    @Test
    void quandEcuyer_alors5Vagues() {
        assertEquals(5, Difficulte.ECUYER.getNombreVagues());
    }

    @Test
    void quandEcuyer_alorsNiveau1() {
        assertEquals(1, Difficulte.ECUYER.getNiveau());
    }

    // CHEVALIER

    @Test
    void quandChevalier_alorsBudget400() {
        assertEquals(400, Difficulte.CHEVALIER.getBudgetInitial());
    }

    @Test
    void quandChevalier_alors5Vagues() {
        assertEquals(5, Difficulte.CHEVALIER.getNombreVagues());
    }

    @Test
    void quandChevalier_alorsNiveau2() {
        assertEquals(2, Difficulte.CHEVALIER.getNiveau());
    }

    // SEIGNEUR

    @Test
    void quandSeigneur_alorsBudget300() {
        assertEquals(300, Difficulte.SEIGNEUR.getBudgetInitial());
    }

    @Test
    void quandSeigneur_alors5Vagues() {
        assertEquals(5, Difficulte.SEIGNEUR.getNombreVagues());
    }

    @Test
    void quandSeigneur_alorsNiveau3() {
        assertEquals(3, Difficulte.SEIGNEUR.getNiveau());
    }

    // PROGRESSION

    @Test
    void quandDifficulteAugmente_alorsBudgetDiminue() {
        assertTrue(Difficulte.ECUYER.getBudgetInitial() > Difficulte.CHEVALIER.getBudgetInitial());
        assertTrue(Difficulte.CHEVALIER.getBudgetInitial() > Difficulte.SEIGNEUR.getBudgetInitial());
    }

    @Test
    void quandDifficulteAugmente_alorsDefenseForteresseDiminue() {
        assertTrue(Difficulte.ECUYER.getDefenseForteresse() > Difficulte.CHEVALIER.getDefenseForteresse());
        assertTrue(Difficulte.CHEVALIER.getDefenseForteresse() > Difficulte.SEIGNEUR.getDefenseForteresse());
    }

    @Test
    void quandDifficulteAugmente_alorsDureeVagueDiminue() {
        assertTrue(Difficulte.ECUYER.getDureeVagueSecondes() >= Difficulte.CHEVALIER.getDureeVagueSecondes());
        assertTrue(Difficulte.CHEVALIER.getDureeVagueSecondes() >= Difficulte.SEIGNEUR.getDureeVagueSecondes());
    }

    // ENUM VALUES

    @Test
    void quandValues_alors3Difficultes() {
        assertEquals(3, Difficulte.values().length);
    }

    @Test
    void quandValueOf_alorsRetourneBonEnum() {
        assertEquals(Difficulte.ECUYER, Difficulte.valueOf("ECUYER"));
        assertEquals(Difficulte.CHEVALIER, Difficulte.valueOf("CHEVALIER"));
        assertEquals(Difficulte.SEIGNEUR, Difficulte.valueOf("SEIGNEUR"));
    }
}