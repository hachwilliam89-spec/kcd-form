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
    void constructeurValide() {
        assertEquals("Muraille", rectangle.getNom());
        assertEquals("gris", rectangle.getCouleur());
        assertEquals(4, rectangle.getLargeur(), 0.001);
        assertEquals(4, rectangle.getLongueur(), 0.001);
    }

    @Test
    void constructeurNomNull() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle(null, 4, 4));
    }

    @Test
    void constructeurNomVide() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("", 4, 4));
    }

    @Test
    void constructeurLargeurNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("Test", -1, 4));
    }

    @Test
    void constructeurLargeurZero() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("Test", 0, 4));
    }

    @Test
    void constructeurLongueurNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("Test", 4, -1));
    }

    @Test
    void constructeurLongueurZero() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle("Test", 4, 0));
    }

    // CALCULS

    @Test
    void aire() {
        // 4 * 4 = 16.0
        assertEquals(16.0, rectangle.aire(), 0.001);
    }

    @Test
    void perimetre() {
        // 2 * (4 + 4) = 16.0
        assertEquals(16.0, rectangle.perimetre(), 0.001);
    }

    @Test
    void dpsZero() {
        // Le mur ne fait PAS de dégâts
        assertEquals(0.0, rectangle.dps(), 0.001);
    }

    @Test
    void cout() {
        // (int)(16.0 * 2.5) = 40
        assertEquals(40, rectangle.cout());
    }

    @Test
    void pv() {
        // (int)(aire * 10) = (int)(16.0 * 10) = 160
        assertEquals(160, rectangle.getPv());
    }

    // SETTERS

    @Test
    void setLargeurValide() {
        rectangle.setLargeur(10);
        assertEquals(10, rectangle.getLargeur(), 0.001);
    }

    @Test
    void setLargeurNegative() {
        assertThrows(IllegalArgumentException.class, () -> rectangle.setLargeur(-1));
    }

    @Test
    void setLargeurZero() {
        assertThrows(IllegalArgumentException.class, () -> rectangle.setLargeur(0));
    }

    @Test
    void setLongueurValide() {
        rectangle.setLongueur(10);
        assertEquals(10, rectangle.getLongueur(), 0.001);
    }

    @Test
    void setLongueurNegative() {
        assertThrows(IllegalArgumentException.class, () -> rectangle.setLongueur(-1));
    }

    @Test
    void setLongueurZero() {
        assertThrows(IllegalArgumentException.class, () -> rectangle.setLongueur(0));
    }

    // TO STRING

    @Test
    void toStringContientInfos() {
        String result = rectangle.toString();
        assertTrue(result.contains("Muraille"));
        assertTrue(result.contains("gris"));
        assertTrue(result.contains("PV=160"));
    }
}