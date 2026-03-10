package com.kcdformes.model.formes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormeTest {

    // Forme est abstraite, on teste via Triangle (ou n'importe quelle sous-classe)

    // NOM

    @Test
    void quandSetNomValide_alorsNomChange() {
        Triangle t = new Triangle("Archer", 4, 3);
        t.setNom("Sniper");
        assertEquals("Sniper", t.getNom());
    }

    @Test
    void quandSetNomNull_alorsLeveIllegalArgument() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertThrows(IllegalArgumentException.class, () -> t.setNom(null));
    }

    @Test
    void quandSetNomVide_alorsLeveIllegalArgument() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertThrows(IllegalArgumentException.class, () -> t.setNom(""));
    }

    @Test
    void quandSetNomBlanc_alorsLeveIllegalArgument() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertThrows(IllegalArgumentException.class, () -> t.setNom("   "));
    }

    // COULEUR

    @Test
    void quandTriangle_alorsCouleurRouge() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertEquals("rouge", t.getCouleur());
    }

    @Test
    void quandCercle_alorsCouleurBleu() {
        Cercle c = new Cercle("Catapulte", 3);
        assertEquals("bleu", c.getCouleur());
    }

    @Test
    void quandRectangle_alorsCouleurGris() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        assertEquals("gris", r.getCouleur());
    }

    // TO STRING

    @Test
    void quandToString_alorsContientNomEtCouleur() {
        Triangle t = new Triangle("Archer", 4, 3);
        String result = t.toString();
        assertTrue(result.contains("Archer"));
        assertTrue(result.contains("rouge"));
    }
}