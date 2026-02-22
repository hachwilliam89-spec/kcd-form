package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    @Test
    void quandLargeur4Longueur4_alorsAireEgale16() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        assertEquals(16.0, r.aire());
    }

    @Test
    void quandLargeur4Longueur4_alorsPerimetreEgale16() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        assertEquals(16.0, r.perimetre());
    }

    @Test
    void quandMuraille_alorsDpsEgaleZero() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        assertEquals(0, r.dps());
    }

    @Test
    void quandMuraille_alorsCoutEgale40() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        assertEquals(40, r.cout());
    }

    @Test
    void quandMuraille4x4_alorsPvEgale160() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        assertEquals(160, r.getPv());
    }

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        assertEquals("Muraille", r.getNom());
        assertEquals("gris", r.getCouleur());
        assertEquals(4, r.getLargeur());
        assertEquals(4, r.getLongueur());
    }

    @Test
    void quandSetLargeur6_alorsLargeurEgale6() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        r.setLargeur(6);
        assertEquals(6, r.getLargeur());
    }

    @Test
    void quandSetLongueur8_alorsLongueurEgale8() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        r.setLongueur(8);
        assertEquals(8, r.getLongueur());
    }

    @Test
    void quandLargeurNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Rectangle("Muraille", -3, 4);
        });
    }

    @Test
    void quandLongueurZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Rectangle("Muraille", 4, 0);
        });
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Rectangle("", 4, 4);
        });
    }

    @Test
    void quandToString_alorsContientInfos() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        String str = r.toString();
        assertTrue(str.contains("Muraille"));
        assertTrue(str.contains("gris"));
        assertTrue(str.contains("PV=160"));
    }
}