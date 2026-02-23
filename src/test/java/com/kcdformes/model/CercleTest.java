package com.kcdformes.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CercleTest {

    private Cercle cercle;

    @BeforeEach
    void setUp() {
        cercle = new Cercle("Catapulte", 3);
    }

    // CONSTRUCTEUR

    @Test
    void constructeurValide() {
        assertEquals("Catapulte", cercle.getNom());
        assertEquals("bleu", cercle.getCouleur());
        assertEquals(3, cercle.getRayon(), 0.001);
        assertEquals(0.7, cercle.getCoeff(), 0.001);
        assertEquals(1.0, cercle.getCadence(), 0.001);
    }

    @Test
    void constructeurNomNull() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle(null, 3));
    }

    @Test
    void constructeurNomVide() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle("", 3));
    }

    @Test
    void constructeurRayonNegatif() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle("Test", -1));
    }

    @Test
    void constructeurRayonZero() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle("Test", 0));
    }

    // CALCULS

    @Test
    void aire() {
        // PI * 3² = 28.274
        assertEquals(Math.PI * 9, cercle.aire(), 0.001);
    }

    @Test
    void perimetre() {
        // 2 * PI * 3 = 18.849
        assertEquals(2 * Math.PI * 3, cercle.perimetre(), 0.001);
    }

    @Test
    void dps() {
        // aire * 0.7 * 1.0 = 28.274 * 0.7 = 19.792
        assertEquals(cercle.aire() * 0.7 * 1.0, cercle.dps(), 0.001);
    }

    @Test
    void cout() {
        // (int)(aire * 2.5) = (int)(28.274 * 2.5) = 70
        assertEquals((int) (cercle.aire() * 2.5), cercle.cout());
    }

    // SETTER

    @Test
    void setRayonValide() {
        cercle.setRayon(10);
        assertEquals(10, cercle.getRayon(), 0.001);
    }

    @Test
    void setRayonNegatif() {
        assertThrows(IllegalArgumentException.class, () -> cercle.setRayon(-1));
    }

    @Test
    void setRayonZero() {
        assertThrows(IllegalArgumentException.class, () -> cercle.setRayon(0));
    }

    // TO STRING

    @Test
    void toStringContientInfos() {
        String result = cercle.toString();
        assertTrue(result.contains("Catapulte"));
        assertTrue(result.contains("bleu"));
        assertTrue(result.contains("3.0"));
    }
}