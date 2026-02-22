package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    @Test
    void quandBase4Hauteur3_alorsAireEgale6() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertEquals(6.0, t.aire());
    }

    @Test
    void quandBase4Hauteur3_alorsPerimetreCorrect() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertEquals(11.21, t.perimetre(), 0.01);
    }

    @Test
    void quandArcher_alorsDpsEgale32_4() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertEquals(32.4, t.dps(), 0.001);
    }

    @Test
    void quandArcher_alorsCoutEgale15() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertEquals(15, t.cout());
    }

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertEquals("Archer", t.getNom());
        assertEquals("rouge", t.getCouleur());
        assertEquals(4, t.getBase());
        assertEquals(3, t.getHauteur());
    }

    @Test
    void quandConstructeur_alorsCoeffEtCadenceCorrects() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertEquals(1.8, t.getCoeff());
        assertEquals(3.0, t.getCadence());
    }

    @Test
    void quandSetBase5_alorsBaseEgale5() {
        Triangle t = new Triangle("Archer", 4, 3);
        t.setBase(5);
        assertEquals(5, t.getBase());
    }

    @Test
    void quandSetHauteur7_alorsHauteurEgale7() {
        Triangle t = new Triangle("Archer", 4, 3);
        t.setHauteur(7);
        assertEquals(7, t.getHauteur());
    }

    @Test
    void quandBaseNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Triangle("Archer", -5, 3);
        });
    }

    @Test
    void quandHauteurZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Triangle("Archer", 4, 0);
        });
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Triangle("", 4, 3);
        });
    }

    @Test
    void quandToString_alorsContientInfos() {
        Triangle t = new Triangle("Archer", 4, 3);
        String str = t.toString();
        assertTrue(str.contains("Archer"));
        assertTrue(str.contains("rouge"));
        assertTrue(str.contains("base=4.0"));
    }
}