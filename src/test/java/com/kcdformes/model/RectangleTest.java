package com.kcdformes.model;

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
    void quandCout_alorsAire_fois_2point5() {
        assertEquals(40, rectangle.cout());
    }

    @Test
    void quandPv_alorsAire_fois_10() {
        assertEquals(160, rectangle.getPv());
    }

    // SETTERS

    @Test
    void quandSetLargeurValide_alorsLargeurChange() {
        rectangle.setLargeur(10);
        assertEquals(10, rectangle.getLargeur(), 0.001);
    }

    @Test
    void quandSetLargeurNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> rectangle.setLargeur(-1));
    }

    @Test
    void quandSetLargeurZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> rectangle.setLargeur(0));
    }

    @Test
    void quandSetLongueurValide_alorsLongueurChange() {
        rectangle.setLongueur(10);
        assertEquals(10, rectangle.getLongueur(), 0.001);
    }

    @Test
    void quandSetLongueurNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> rectangle.setLongueur(-1));
    }

    @Test
    void quandSetLongueurZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> rectangle.setLongueur(0));
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