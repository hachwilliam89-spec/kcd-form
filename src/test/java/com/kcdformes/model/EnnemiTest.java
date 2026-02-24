package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnnemiTest {

    // PV = aire * 25 (coeff 1.0 par défaut)
    // Triangle(4,3) : aire=6, PV=150
    // Cercle(3) : aire≈28.27, PV=706
    // Rectangle(4,4) : aire=16, PV=400

    @Test
    void quandTriangleAire6_alorsPvEgale150() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        assertEquals(150, e.getPvMax());
    }

    @Test
    void quandCavalerie_alorsVitesse3() {
        assertEquals(3.0, new Ennemi("Cavalerie", new Triangle("T", 4, 3)).getVitesse());
    }

    @Test
    void quandInfanterie_alorsVitesse1() {
        assertEquals(1.0, new Ennemi("Infanterie", new Cercle("C", 3)).getVitesse());
    }

    @Test
    void quandBelier_alorsVitesse1() {
        assertEquals(1.0, new Ennemi("Belier", new Rectangle("R", 4, 4)).getVitesse());
    }

    @Test
    void quandCavalerie_alorsRecompense3() {
        assertEquals(3, new Ennemi("Cavalerie", new Triangle("T", 4, 3)).getRecompense());
    }

    @Test
    void quandInfanterie_alorsRecompense18() {
        assertEquals(18, new Ennemi("Infanterie", new Cercle("C", 3)).getRecompense());
    }

    @Test
    void quandBelier_alorsRecompense24() {
        assertEquals(24, new Ennemi("Belier", new Rectangle("R", 4, 4)).getRecompense());
    }

    @Test
    void quandSubirDegats20_alorsPvDiminue() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        e.subirDegats(20);
        assertEquals(130, e.getPvActuels());
    }

    @Test
    void quandSubirDegatsEnormes_alorsPvPasNegatif() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        e.subirDegats(999);
        assertEquals(0, e.getPvActuels());
    }

    @Test
    void quandPvSuperieursAZero_alorsEstVivant() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        assertTrue(e.estVivant());
    }

    @Test
    void quandPvEgaleZero_alorsEstMort() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        e.subirDegats(999);
        assertFalse(e.estVivant());
    }

    @Test
    void quandAvancer_alorsPositionAugmenteDeVitesse() {
        // Cavalier vitesse 3.0 → avance de 3
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        assertEquals(0, e.getPosition());
        e.avancer();
        assertEquals(3, e.getPosition());
    }

    @Test
    void quandBelierAvance_alorsPositionAugmenteDe1() {
        // Bélier vitesse 1.0 → avance de 1
        Ennemi e = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(0, e.getPosition());
        e.avancer();
        assertEquals(1, e.getPosition());
    }

    @Test
    void quandBelier_alorsPlusPvQueCavalerie() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("Rec", 4, 4));
        Ennemi cavalerie = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        assertTrue(belier.getPvMax() > cavalerie.getPvMax());
    }

    @Test
    void quandBelier_alorsPlusLentQueCavalerie() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("Rec", 4, 4));
        Ennemi cavalerie = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        assertTrue(belier.getVitesse() < cavalerie.getVitesse());
    }

    @Test
    void quandInfanteriePleinePv_alorsForceEgale1() {
        Ennemi inf = new Ennemi("Infanterie", new Cercle("C", 3));
        assertEquals(1.0, inf.getForceAttaque());
    }

    @Test
    void quandInfanterieMoitiePv_alorsForceEgale0_5() {
        Ennemi inf = new Ennemi("Infanterie", new Cercle("C", 3));
        inf.subirDegats(inf.getPvMax() / 2);
        assertEquals(0.5, inf.getForceAttaque(), 0.05);
    }

    @Test
    void quandCavalerie_alorsForceAttaqueToujoursEgale1() {
        Ennemi cav = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        cav.subirDegats(30);
        assertEquals(1.0, cav.getForceAttaque());
    }

    @Test
    void quandBelier_alorsDegatsRempartEgale2() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(2.0, belier.getDegatsRempart());
    }

    @Test
    void quandCavalerie_alorsDegatsRempartEgale1() {
        Ennemi cav = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        assertEquals(1.0, cav.getDegatsRempart());
    }

    @Test
    void quandBelierPleinsVie_alorsDegatsReelsEgale2() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(2.0, belier.degatsReels());
    }

    @Test
    void quandInfanterieMoitiePv_alorsDegatsReelsReduits() {
        Ennemi inf = new Ennemi("Infanterie", new Cercle("C", 3));
        inf.subirDegats(inf.getPvMax() / 2);
        assertEquals(0.5, inf.degatsReels(), 0.05);
    }

    // DEGATS FORTERESSE

    @Test
    void quandCavalerie_alorsDegatsForteresse10() {
        Ennemi cav = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        assertEquals(10, cav.getDegatsForteresse());
    }

    @Test
    void quandInfanterie_alorsDegatsForteresse15() {
        Ennemi inf = new Ennemi("Infanterie", new Cercle("C", 3));
        assertEquals(15, inf.getDegatsForteresse());
    }

    @Test
    void quandBelier_alorsDegatsForteresse40() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(40, belier.getDegatsForteresse());
    }

    // COEFF DIFFICULTE

    @Test
    void quandCoeff2_alorsPvDoubles() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("T", 4, 3), 2.0);
        assertEquals(300, e.getPvMax());
    }

    @Test
    void quandCoeff2_alorsDegatsForteresseInchanges() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("T", 4, 3), 2.0);
        assertEquals(10, e.getDegatsForteresse());
    }

    @Test
    void quandGetForme_alorsRetourneFormeCorrecte() {
        Triangle tri = new Triangle("Tri", 4, 3);
        Ennemi e = new Ennemi("Cavalerie", tri);
        assertEquals(tri, e.getForme());
    }

    @Test
    void quandSetPosition5_alorsPositionEgale5() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        e.setPosition(5);
        assertEquals(5, e.getPosition());
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ennemi("", new Triangle("Tri", 4, 3));
        });
    }

    @Test
    void quandPositionNegative_alorsLeveIllegalArgument() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        assertThrows(IllegalArgumentException.class, () -> {
            e.setPosition(-1);
        });
    }

    @Test
    void quandToString_alorsContientInfos() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        String str = e.toString();
        assertTrue(str.contains("Cavalerie"));
        assertTrue(str.contains("PV=150/150"));
        assertTrue(str.contains("vitesse=3.0"));
        assertTrue(str.contains("degatsForteresse=10"));
    }
}