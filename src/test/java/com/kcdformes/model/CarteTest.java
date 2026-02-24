package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CarteTest {

    private Carte creerCarteBasique() {
        Carte c = new Carte("Niveau 1", 10, 10);
        c.setChemin(List.of(0, 1, 2, 3, 4, 5));
        c.setEmplacementsTourelles(List.of(10, 11, 12));
        return c;
    }

    @Test
    void constructeurValide() {
        Carte c = new Carte("Niveau 1", 10, 10);
        assertEquals("Niveau 1", c.getNom());
        assertEquals(10, c.getLargeur());
        assertEquals(10, c.getHauteur());
    }

    @Test
    void placerTourelleSurEmplacementValide() {
        Carte c = creerCarteBasique();
        Tourelle t = new Tourelle("Tour A", 0);
        assertTrue(c.placerTourelle(t, 10));
        assertEquals(1, c.getTourelles().size());
    }

    @Test
    void placerTourelleSurEmplacementInvalide() {
        Carte c = creerCarteBasique();
        Tourelle t = new Tourelle("Tour A", 0);
        assertFalse(c.placerTourelle(t, 99));  // emplacement inexistant
    }

    @Test
    void placerTourelleSurEmplacementOccupe() {
        Carte c = creerCarteBasique();
        c.placerTourelle(new Tourelle("Tour A", 0), 10);
        assertFalse(c.placerTourelle(new Tourelle("Tour B", 0), 10));  // déjà pris
    }

    @Test
    void supprimerTourelle() {
        Carte c = creerCarteBasique();
        c.placerTourelle(new Tourelle("Tour A", 0), 10);
        assertTrue(c.supprimerTourelle(10));
        assertEquals(0, c.getTourelles().size());
    }

    @Test
    void tourellesEnPortee() {
        Carte c = creerCarteBasique();
        Tourelle t = new Tourelle("Tour A", 0);
        c.placerTourelle(t, 10);
        assertEquals(1, c.getTourellesEnPortee(10).size());  // sur la tourelle
        assertEquals(1, c.getTourellesEnPortee(11).size());  // à 1 case
        assertEquals(0, c.getTourellesEnPortee(15).size());  // trop loin
    }

    @Test
    void cheminVideLeveException() {
        Carte c = new Carte("Niveau 1", 10, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            c.setChemin(List.of());
        });
    }

    @Test
    void nomVideLeveException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Carte("", 10, 10);
        });
    }

    // CONSTRUCTEUR VALIDATION

    @Test
    void constructeurNomNull() {
        assertThrows(IllegalArgumentException.class, () -> new Carte(null, 10, 10));
    }

    @Test
    void constructeurLargeurZero() {
        assertThrows(IllegalArgumentException.class, () -> new Carte("Test", 0, 10));
    }

    @Test
    void constructeurLargeurNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Carte("Test", -1, 10));
    }

    @Test
    void constructeurHauteurZero() {
        assertThrows(IllegalArgumentException.class, () -> new Carte("Test", 10, 0));
    }

    @Test
    void constructeurHauteurNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Carte("Test", 10, -1));
    }

// CHEMIN ET EMPLACEMENTS

    @Test
    void cheminNull() {
        Carte c = new Carte("Test", 10, 10);
        assertThrows(IllegalArgumentException.class, () -> c.setChemin(null));
    }

    @Test
    void emplacementsTourellesNull() {
        Carte c = new Carte("Test", 10, 10);
        assertThrows(IllegalArgumentException.class, () -> c.setEmplacementsTourelles(null));
    }

    @Test
    void emplacementsMuraillesNull() {
        Carte c = new Carte("Test", 10, 10);
        assertThrows(IllegalArgumentException.class, () -> c.setEmplacementsMurailles(null));
    }

// TOURELLE NULL

    @Test
    void placerTourelleNull() {
        Carte c = creerCarteBasique();
        assertThrows(IllegalArgumentException.class, () -> c.placerTourelle(null, 10));
    }

// SUPPRESSION INEXISTANTE

    @Test
    void supprimerTourelleInexistante() {
        Carte c = creerCarteBasique();
        assertFalse(c.supprimerTourelle(99));
    }

// MURAILLES

    @Test
    void placerMurailleValide() {
        Carte c = creerCarteBasique();
        c.setEmplacementsMurailles(List.of(3));
        Rectangle mur = new Rectangle("Mur", 4, 4);
        assertTrue(c.placerMuraille(mur, 3));
        assertEquals(1, c.getMurailles().size());
    }

    @Test
    void placerMurailleNull() {
        Carte c = creerCarteBasique();
        c.setEmplacementsMurailles(List.of(3));
        assertThrows(IllegalArgumentException.class, () -> c.placerMuraille(null, 3));
    }

    @Test
    void placerMurailleEmplacementInvalide() {
        Carte c = creerCarteBasique();
        c.setEmplacementsMurailles(List.of(3));
        Rectangle mur = new Rectangle("Mur", 4, 4);
        assertFalse(c.placerMuraille(mur, 99));
    }

    @Test
    void getMuraillesSurChemin() {
        Carte c = creerCarteBasique();
        c.setEmplacementsMurailles(List.of(3));
        Rectangle mur = new Rectangle("Mur", 4, 4);
        c.placerMuraille(mur, 3);
        assertEquals(1, c.getMuraillesSurChemin(3).size());
    }

    @Test
    void getMuraillesSurCheminVide() {
        Carte c = creerCarteBasique();
        assertEquals(0, c.getMuraillesSurChemin(99).size());
    }

// SETTERS

    @Test
    void setNomValide() {
        Carte c = creerCarteBasique();
        c.setNom("Niveau 2");
        assertEquals("Niveau 2", c.getNom());
    }

    @Test
    void setLargeurValide() {
        Carte c = creerCarteBasique();
        c.setLargeur(20);
        assertEquals(20, c.getLargeur());
    }

    @Test
    void setHauteurValide() {
        Carte c = creerCarteBasique();
        c.setHauteur(20);
        assertEquals(20, c.getHauteur());
    }

// COPIES DEFENSIVES

    @Test
    void getCheminCopieDefensive() {
        Carte c = creerCarteBasique();
        c.getChemin().clear();
        assertEquals(6, c.getChemin().size());
    }

    @Test
    void getTourellesCopieDefensive() {
        Carte c = creerCarteBasique();
        c.getTourelles().clear();
        assertEquals(0, c.getTourelles().size());
    }

    @Test
    void getEmplacementsTourellesCopieDefensive() {
        Carte c = creerCarteBasique();
        c.getEmplacementsTourelles().clear();
        assertEquals(3, c.getEmplacementsTourelles().size());
    }

// TO STRING

    @Test
    void toStringContientInfos() {
        Carte c = creerCarteBasique();
        String result = c.toString();
        assertTrue(result.contains("Niveau 1"));
        assertTrue(result.contains("10x10"));
    }

}