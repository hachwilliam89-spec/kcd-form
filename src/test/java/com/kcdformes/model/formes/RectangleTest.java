package com.kcdformes.model.formes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    private Rectangle rectangle;

    @BeforeEach
    void setUp() {
        rectangle = new Rectangle("Muraille", 4, 4);
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeurValide_alorsAttributsCorrects() {
        assertEquals("Muraille", rectangle.getNom());
        assertEquals("gris", rectangle.getCouleur());
        assertEquals(4, rectangle.getLargeur(), 0.001);
        assertEquals(4, rectangle.getLongueur(), 0.001);
    }

    @Test
    void quandNomNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle(null, 4, 4));
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("", 4, 4));
    }

    @Test
    void quandLargeurNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("Test", -1, 4));
    }

    @Test
    void quandLargeurZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("Test", 0, 4));
    }

    @Test
    void quandLongueurNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("Test", 4, -1));
    }

    @Test
    void quandLongueurZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("Test", 4, 0));
    }

    // CALCULS

    @Test
    void quandAire_alorsLargeur_fois_longueur() {
        assertEquals(16.0, rectangle.aire(), 0.001);
    }

    @Test
    void quandPerimetre_alors2_fois_somme() {
        assertEquals(16.0, rectangle.perimetre(), 0.001);
    }

    @Test
    void quandDps_alorsZero() {
        assertEquals(0.0, rectangle.dps(), 0.001);
    }

    @Test
    void quandCout_alorsAire_fois_2point2() {
        assertEquals((int)(16.0 * 2.2), rectangle.cout());
    }

    @Test
    void quandPv_alorsAire_fois_10() {
        assertEquals(160, rectangle.getPv());
    }

    // CONSTRUCTION AVEC DIFFÉRENTES DIMENSIONS

    @Test
    void quandLargeurValide_alorsLargeurCorrecte() {
        Rectangle r = new Rectangle("Test", 10, 5);
        assertEquals(10, r.getLargeur(), 0.001);
    }

    @Test
    void quandLongueurValide_alorsLongueurCorrecte() {
        Rectangle r = new Rectangle("Test", 4, 10);
        assertEquals(10, r.getLongueur(), 0.001);
    }

    @Test
    void quandPvDynamique_alorsSuitAire() {
        Rectangle r = new Rectangle("Test", 8, 4);
        assertEquals((int)(8 * 4 * 10), r.getPv());
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        String result = rectangle.toString();
        assertTrue(result.contains("Muraille"));
        assertTrue(result.contains("gris"));
        assertTrue(result.contains("PV=160"));
    }
}