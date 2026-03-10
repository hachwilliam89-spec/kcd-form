package com.kcdformes.model.ennemis;

import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
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

    // POINTS DE SCORE

    @Test
    void quandCavalerie_alorsPoints6() {
        assertEquals(6, new Ennemi("Cavalerie", new Triangle("T", 4, 3)).getPointsScore());
    }

    @Test
    void quandInfanterie_alorsPoints28() {
        assertEquals(28, new Ennemi("Infanterie", new Cercle("C", 3)).getPointsScore());
    }

    @Test
    void quandBelier_alorsPoints32() {
        assertEquals(32, new Ennemi("Belier", new Rectangle("R", 4, 4)).getPointsScore());
    }

    // DEGATS ET PV

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

    // DEPLACEMENT

    @Test
    void quandAvancer_alorsPositionAugmenteDeVitesse() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        assertEquals(0, e.getPosition());
        e.avancer();
        assertEquals(3, e.getPosition());
    }

    @Test
    void quandBelierAvance_alorsPositionAugmenteDe1() {
        Ennemi e = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(0, e.getPosition());
        e.avancer();
        assertEquals(1, e.getPosition());
    }

    // COMPARAISONS ENTRE TYPES

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

    // FORCE D'ATTAQUE — TOUS LES TYPES

    @Test
    void quandPleinePv_alorsForceEgale1() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        assertEquals(1.0, e.getForceAttaque());
    }

    @Test
    void quandMoitiePv_alorsForceEgale0_5() {
        Ennemi e = new Ennemi("Infanterie", new Cercle("C", 3));
        e.subirDegats(e.getPvMax() / 2);
        assertEquals(0.5, e.getForceAttaque(), 0.05);
    }

    @Test
    void quandCavalerieMoitiePv_alorsForceReduite() {
        Ennemi cav = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        cav.subirDegats(cav.getPvMax() / 2);
        assertEquals(0.5, cav.getForceAttaque(), 0.05);
    }

    // DEGATS REMPART

    @Test
    void quandBelier_alorsDegatsRempart8() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(8.0, belier.getDegatsRempart());
    }

    @Test
    void quandCavalerie_alorsDegatsRempart5() {
        Ennemi cav = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        assertEquals(5.0, cav.getDegatsRempart());
    }

    @Test
    void quandBelierPleinsVie_alorsDegatsReelsEgale8() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(8.0, belier.degatsReels());
    }

    @Test
    void quandInfanterieMoitiePv_alorsDegatsReelsReduits() {
        Ennemi inf = new Ennemi("Infanterie", new Cercle("C", 3));
        inf.subirDegats(inf.getPvMax() / 2);
        assertEquals(1.5, inf.degatsReels(), 0.1);
    }

    // DEGATS FORTERESSE

    @Test
    void quandCavalerie_alorsDegatsForteresse12() {
        Ennemi cav = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        assertEquals(12, cav.getDegatsForteresse());
    }

    @Test
    void quandInfanterie_alorsDegatsForteresse56() {
        Ennemi inf = new Ennemi("Infanterie", new Cercle("C", 3));
        assertEquals(56, inf.getDegatsForteresse());
    }

    @Test
    void quandBelier_alorsDegatsForteresse64() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(64, belier.getDegatsForteresse());
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
        assertEquals(12, e.getDegatsForteresse());
    }

    @Test
    void quandCoeff2_alorsPointsScoreDoubles() {
        Ennemi e1 = new Ennemi("Cavalerie", new Triangle("T", 4, 3), 1.0);
        Ennemi e2 = new Ennemi("Cavalerie", new Triangle("T", 4, 3), 2.0);
        assertEquals(e1.getPointsScore() * 2, e2.getPointsScore());
    }

    // GETTERS

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

    // VALIDATION

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

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        String str = e.toString();
        assertTrue(str.contains("Cavalerie"));
        assertTrue(str.contains("PV=150/150"));
        assertTrue(str.contains("vitesse=3.0"));
        assertTrue(str.contains("degatsForteresse=12"));
    }
}