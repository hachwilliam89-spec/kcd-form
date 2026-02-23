package com.kcdformes.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormeTest {

    // Forme est abstraite, on teste via Triangle (ou n'importe quelle sous-classe)

    // NOM

    @Test
    void setNomValide() {
        Triangle t = new Triangle("Archer", 4, 3);
        t.setNom("Sniper");
        assertEquals("Sniper", t.getNom());
    }

    @Test
    void setNomNull() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertThrows(IllegalArgumentException.class, () -> t.setNom(null));
    }

    @Test
    void setNomVide() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertThrows(IllegalArgumentException.class, () -> t.setNom(""));
    }

    @Test
    void setNomBlanc() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertThrows(IllegalArgumentException.class, () -> t.setNom("   "));
    }

    // COULEUR

    @Test
    void couleurTriangle() {
        Triangle t = new Triangle("Archer", 4, 3);
        assertEquals("rouge", t.getCouleur());
    }

    @Test
    void couleurCercle() {
        Cercle c = new Cercle("Catapulte", 3);
        assertEquals("bleu", c.getCouleur());
    }

    @Test
    void couleurRectangle() {
        Rectangle r = new Rectangle("Muraille", 4, 4);
        assertEquals("gris", r.getCouleur());
    }

    // TO STRING

    @Test
    void toStringContientNomEtCouleur() {
        Triangle t = new Triangle("Archer", 4, 3);
        String result = t.toString();
        assertTrue(result.contains("Archer"));
        assertTrue(result.contains("rouge"));
    }
}