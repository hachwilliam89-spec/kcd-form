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
}