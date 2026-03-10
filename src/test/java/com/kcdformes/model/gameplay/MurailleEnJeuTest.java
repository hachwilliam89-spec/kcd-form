package com.kcdformes.model.gameplay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MurailleEnJeuTest {

    private Partie.MurailleEnJeu creerMuraille() {
        return new Partie.MurailleEnJeu(5, 5.0, 3.0, 150);
    }

    @Test
    void quandCreation_alorsPositionEtPvCorrects() {
        Partie.MurailleEnJeu m = creerMuraille();
        assertEquals(5, m.getPosition());
        assertEquals(150, m.getPvMax());
        assertEquals(150, m.getPvActuels());
        assertFalse(m.estDetruite());
    }

    @Test
    void quandSubirDegats_alorsPvDiminuent() {
        Partie.MurailleEnJeu m = creerMuraille();
        m.subirDegats(50);
        assertEquals(100, m.getPvActuels());
        assertFalse(m.estDetruite());
    }

    @Test
    void quandSubirDegatsLetaux_alorsMurailleDetruite() {
        Partie.MurailleEnJeu m = creerMuraille();
        m.subirDegats(200);
        assertEquals(0, m.getPvActuels());
        assertTrue(m.estDetruite());
    }

    @Test
    void quandSubirDegatsExacts_alorsPvAZero() {
        Partie.MurailleEnJeu m = creerMuraille();
        m.subirDegats(150);
        assertEquals(0, m.getPvActuels());
        assertTrue(m.estDetruite());
    }

    @Test
    void quandSubirPlusieursAttaques_alorsPvCumulent() {
        Partie.MurailleEnJeu m = creerMuraille();
        m.subirDegats(30);
        assertEquals(120, m.getPvActuels());
        m.subirDegats(50);
        assertEquals(70, m.getPvActuels());
        m.subirDegats(80);
        assertEquals(0, m.getPvActuels());
        assertTrue(m.estDetruite());
    }

    @Test
    void quandPvNegatifs_alorsClampAZero() {
        Partie.MurailleEnJeu m = creerMuraille();
        m.subirDegats(9999);
        assertEquals(0, m.getPvActuels());
        assertTrue(m.estDetruite());
    }
}