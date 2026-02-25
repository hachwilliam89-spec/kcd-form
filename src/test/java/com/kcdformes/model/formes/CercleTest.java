package com.kcdformes.model.formes;

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
    void quandConstructeurValide_alorsAttributsCorrects() {
        assertEquals("Catapulte", cercle.getNom());
        assertEquals("bleu", cercle.getCouleur());
        assertEquals(3, cercle.getRayon(), 0.001);
        assertEquals(1.5, cercle.getCoeff(), 0.001);
        assertEquals(0.4, cercle.getCadence(), 0.001);
    }

    @Test
    void quandNomNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle(null, 3));
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle("", 3));
    }

    @Test
    void quandRayonNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle("Test", -1));
    }

    @Test
    void quandRayonZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle("Test", 0));
    }

    // CALCULS

    @Test
    void quandAire_alorsPI_fois_rayonCarre() {
        assertEquals(Math.PI * 9, cercle.aire(), 0.001);
    }

    @Test
    void quandPerimetre_alors2PI_fois_rayon() {
        assertEquals(2 * Math.PI * 3, cercle.perimetre(), 0.001);
    }

    @Test
    void quandDps_alorsAire_fois_coeff_fois_cadence() {
        assertEquals(cercle.aire() * 1.5 * 0.4, cercle.dps(), 0.001);
    }

    @Test
    void quandCout_alorsAire_fois_2point65() {
        assertEquals((int) (cercle.aire() * 2.65), cercle.cout());
    }

    // CONSTRUCTION AVEC DIFFÉRENTS RAYONS

    @Test
    void quandRayonValide_alorsRayonCorrect() {
        Cercle c = new Cercle("Test", 10);
        assertEquals(10, c.getRayon(), 0.001);
    }

    @Test
    void quandRayonNegatif_alorsException() {
        assertThrows(IllegalArgumentException.class, () -> new Cercle("Test", -5));
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        String result = cercle.toString();
        assertTrue(result.contains("Catapulte"));
        assertTrue(result.contains("bleu"));
        assertTrue(result.contains("3.0"));
    }
}