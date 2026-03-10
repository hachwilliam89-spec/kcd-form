package com.kcdformes.model.formes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    private Triangle triangle;

    @BeforeEach
    void setUp() {
        triangle = new Triangle("Archer", 4, 3);
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeurValide_alorsAttributsCorrects() {
        assertEquals("Archer", triangle.getNom());
        assertEquals("rouge", triangle.getCouleur());
        assertEquals(4, triangle.getBase(), 0.001);
        assertEquals(3, triangle.getHauteur(), 0.001);
        assertEquals(0.8, triangle.getCoeff(), 0.001);
        assertEquals(3.0, triangle.getCadence(), 0.001);
    }

    @Test
    void quandNomNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle(null, 4, 3));
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("", 4, 3));
    }

    @Test
    void quandBaseNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("Test", -1, 3));
    }

    @Test
    void quandBaseZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("Test", 0, 3));
    }

    @Test
    void quandHauteurNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("Test", 4, -1));
    }

    @Test
    void quandHauteurZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("Test", 4, 0));
    }

    // CALCULS

    @Test
    void quandAire_alorsBase_fois_hauteur_divise2() {
        assertEquals(6.0, triangle.aire(), 0.001);
    }

    @Test
    void quandPerimetre_alorsBase_plus_2cotes() {
        double coteOblique = Math.sqrt(Math.pow(2, 2) + Math.pow(3, 2));
        double expected = 4 + 2 * coteOblique;
        assertEquals(expected, triangle.perimetre(), 0.001);
    }

    @Test
    void quandDps_alorsAire_fois_coeff_fois_cadence() {
        assertEquals(6.0 * 0.8 * 3.0, triangle.dps(), 0.001);
    }

    @Test
    void quandCout_alorsAire_fois_9() {
        assertEquals((int)(6.0 * 9), triangle.cout());
    }

    // CONSTRUCTION AVEC DIFFÉRENTES DIMENSIONS

    @Test
    void quandBaseValide_alorsBaseCorrecte() {
        Triangle t = new Triangle("Test", 10, 5);
        assertEquals(10, t.getBase(), 0.001);
    }

    @Test
    void quandHauteurValide_alorsHauteurCorrecte() {
        Triangle t = new Triangle("Test", 4, 10);
        assertEquals(10, t.getHauteur(), 0.001);
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        String result = triangle.toString();
        assertTrue(result.contains("Archer"));
        assertTrue(result.contains("rouge"));
        assertTrue(result.contains("4.0"));
        assertTrue(result.contains("3.0"));
    }
}