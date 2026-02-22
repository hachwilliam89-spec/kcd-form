package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CercleTest {

    @Test
    void quandRayon3_alorsAireCorrecte() {
        Cercle c = new Cercle("Catapulte", 3);
        assertEquals(28.27, c.aire(), 0.01);
    }

    @Test
    void quandRayon3_alorsPerimetreCorrect() {
        Cercle c = new Cercle("Catapulte", 3);
        assertEquals(18.85, c.perimetre(), 0.01);
    }

    @Test
    void quandCatapulte_alorsDpsCorrect() {
        Cercle c = new Cercle("Catapulte", 3);
        assertEquals(19.79, c.dps(), 0.01);
    }

    @Test
    void quandCatapulte_alorsCoutEgale70() {
        Cercle c = new Cercle("Catapulte", 3);
        assertEquals(70, c.cout());
    }

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        Cercle c = new Cercle("Catapulte", 3);
        assertEquals("Catapulte", c.getNom());
        assertEquals("bleu", c.getCouleur());
        assertEquals(3, c.getRayon());
    }

    @Test
    void quandConstructeur_alorsCoeffEtCadenceCorrects() {
        Cercle c = new Cercle("Catapulte", 3);
        assertEquals(0.7, c.getCoeff());
        assertEquals(1.0, c.getCadence());
    }

    @Test
    void quandSetRayon5_alorsRayonEgale5() {
        Cercle c = new Cercle("Catapulte", 3);
        c.setRayon(5);
        assertEquals(5, c.getRayon());
    }

    @Test
    void quandRayonNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cercle("Catapulte", -2);
        });
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cercle("", 3);
        });
    }

    @Test
    void quandToString_alorsContientInfos() {
        Cercle c = new Cercle("Catapulte", 3);
        String str = c.toString();
        assertTrue(str.contains("Catapulte"));
        assertTrue(str.contains("bleu"));
        assertTrue(str.contains("rayon=3.0"));
    }
}