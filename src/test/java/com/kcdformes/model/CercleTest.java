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
    void quandConstructeurValide_alorsAttributsCorrects() {
        assertEquals("Catapulte", cercle.getNom());
        assertEquals("bleu", cercle.getCouleur());
        assertEquals(3, cercle.getRayon(), 0.001);
        assertEquals(0.7, cercle.getCoeff(), 0.001);
        assertEquals(1.0, cercle.getCadence(), 0.001);
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
        assertEquals(cercle.aire() * 0.7 * 1.0, cercle.dps(), 0.001);
    }

    @Test
    void quandCout_alorsAire_fois_2point5() {
        assertEquals((int) (cercle.aire() * 2.5), cercle.cout());
    }

    // SETTER

    @Test
    void quandSetRayonValide_alorsRayonChange() {
        cercle.setRayon(10);
        assertEquals(10, cercle.getRayon(), 0.001);
    }

    @Test
    void quandSetRayonNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cercle.setRayon(-1));
    }

    @Test
    void quandSetRayonZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cercle.setRayon(0));
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