package com.kcdformes.model;

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
    void constructeurValide() {
        assertEquals("Archer", triangle.getNom());
        assertEquals("rouge", triangle.getCouleur());
        assertEquals(4, triangle.getBase(), 0.001);
        assertEquals(3, triangle.getHauteur(), 0.001);
        assertEquals(1.8, triangle.getCoeff(), 0.001);
        assertEquals(3.0, triangle.getCadence(), 0.001);
    }

    @Test
    void constructeurNomNull() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle(null, 4, 3));
    }

    @Test
    void constructeurNomVide() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("", 4, 3));
    }

    @Test
    void constructeurBaseNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("Test", -1, 3));
    }

    @Test
    void constructeurBaseZero() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("Test", 0, 3));
    }

    @Test
    void constructeurHauteurNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("Test", 4, -1));
    }

    @Test
    void constructeurHauteurZero() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle("Test", 4, 0));
    }

    // CALCULS

    @Test
    void aire() {
        // (4 * 3) / 2 = 6.0
        assertEquals(6.0, triangle.aire(), 0.001);
    }

    @Test
    void perimetre() {
        // coteOblique = sqrt((4/2)² + 3²) = sqrt(4 + 9) = sqrt(13) ≈ 3.606
        // perimetre = 4 + 2 * 3.606 ≈ 11.211
        double coteOblique = Math.sqrt(Math.pow(2, 2) + Math.pow(3, 2));
        double expected = 4 + 2 * coteOblique;
        assertEquals(expected, triangle.perimetre(), 0.001);
    }

    @Test
    void dps() {
        // aire * coeff * cadence = 6.0 * 1.8 * 3.0 = 32.4
        assertEquals(32.4, triangle.dps(), 0.001);
    }

    @Test
    void cout() {
        // (int)(aire * 2.5) = (int)(6.0 * 2.5) = (int)15.0 = 15
        assertEquals(15, triangle.cout());
    }

    // SETTERS

    @Test
    void setBaseValide() {
        triangle.setBase(10);
        assertEquals(10, triangle.getBase(), 0.001);
    }

    @Test
    void setBaseNegative() {
        assertThrows(IllegalArgumentException.class, () -> triangle.setBase(-1));
    }

    @Test
    void setBaseZero() {
        assertThrows(IllegalArgumentException.class, () -> triangle.setBase(0));
    }

    @Test
    void setHauteurValide() {
        triangle.setHauteur(10);
        assertEquals(10, triangle.getHauteur(), 0.001);
    }

    @Test
    void setHauteurNegative() {
        assertThrows(IllegalArgumentException.class, () -> triangle.setHauteur(-1));
    }

    @Test
    void setHauteurZero() {
        assertThrows(IllegalArgumentException.class, () -> triangle.setHauteur(0));
    }

    // TO STRING

    @Test
    void toStringContientInfos() {
        String result = triangle.toString();
        assertTrue(result.contains("Archer"));
        assertTrue(result.contains("rouge"));
        assertTrue(result.contains("4.0"));
        assertTrue(result.contains("3.0"));
    }
}