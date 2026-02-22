package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TourelleTest {

    // TOURELLE VIDE

    @Test
    void quandTourelleVide_alorsDpsTotalEgaleZero() {
        Tourelle t = new Tourelle("Tour Vide", 0);
        assertEquals(0, t.dpsTotal());
    }

    @Test
    void quandTourelleVide_alorsCoutTotalEgaleZero() {
        Tourelle t = new Tourelle("Tour Vide", 0);
        assertEquals(0, t.coutTotal());
    }

    @Test
    void quandTourelleVide_alorsAireTotaleEgaleZero() {
        Tourelle t = new Tourelle("Tour Vide", 0);
        assertEquals(0, t.aireTotale());
    }

    @Test
    void quandTourelleVide_alorsPerimetreTotalEgaleZero() {
        Tourelle t = new Tourelle("Tour Vide", 0);
        assertEquals(0, t.perimetreTotale());
    }

    @Test
    void quandTourelleVide_alorsNombreFormesEgaleZero() {
        Tourelle t = new Tourelle("Tour Vide", 0);
        assertEquals(0, t.getNombreFormes());
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        Tourelle t = new Tourelle("Tour A", 3);
        assertEquals("Tour A", t.getNom());
        assertEquals(3, t.getPosition());
        assertEquals(0, t.getOrientation());
    }

    // AJOUT / SUPPRESSION

    @Test
    void quandAjouterForme_alorsNombreFormesAugmente() {
        Tourelle t = new Tourelle("Tour A", 3);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        assertEquals(1, t.getNombreFormes());
    }

    @Test
    void quandSupprimerForme_alorsNombreFormesDiminue() {
        Tourelle t = new Tourelle("Tour A", 3);
        Triangle archer = new Triangle("Archer", 4, 3);
        t.ajouterForme(archer);
        t.supprimerForme(archer);
        assertEquals(0, t.getNombreFormes());
    }

    @Test
    void quandSupprimerFormeInexistante_alorsRetourneFalse() {
        Tourelle t = new Tourelle("Tour A", 3);
        Triangle archer = new Triangle("Archer", 4, 3);
        assertFalse(t.supprimerForme(archer));
    }

    @Test
    void quandAjouterFormeNull_alorsLeveIllegalArgument() {
        Tourelle t = new Tourelle("Tour A", 3);
        assertThrows(IllegalArgumentException.class, () -> {
            t.ajouterForme(null);
        });
    }

    // CALCULS

    @Test
    void quandArcherEtCatapulte_alorsDpsTotalCorrect() {
        Tourelle t = new Tourelle("Tour A", 3);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        t.ajouterForme(new Cercle("Catapulte", 3));
        assertEquals(52.19, t.dpsTotal(), 0.01);
    }

    @Test
    void quandArcherEtCatapulte_alorsCoutTotalCorrect() {
        Tourelle t = new Tourelle("Tour A", 3);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        t.ajouterForme(new Cercle("Catapulte", 3));
        assertEquals(85, t.coutTotal());
    }

    @Test
    void quandArcherEtCatapulte_alorsAireTotaleCorrecte() {
        Tourelle t = new Tourelle("Tour A", 3);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        t.ajouterForme(new Cercle("Catapulte", 3));
        assertEquals(34.27, t.aireTotale(), 0.01);
    }

    @Test
    void quandArcherEtCatapulte_alorsPerimetreTotalCorrect() {
        Tourelle t = new Tourelle("Tour A", 3);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        t.ajouterForme(new Cercle("Catapulte", 3));
        assertEquals(30.06, t.perimetreTotale(), 0.01);
    }

    // MURAILLE

    @Test
    void quandAjouterMuraille_alorsDpsInchange() {
        Tourelle t = new Tourelle("Tour A", 3);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        double dpsSansMuraille = t.dpsTotal();
        t.ajouterForme(new Rectangle("Muraille", 4, 4));
        assertEquals(dpsSansMuraille, t.dpsTotal());
    }

    // BONUS / MALUS

    @Test
    void quandArcherContreBelier_alorsMalus075() {
        Tourelle t = new Tourelle("Tour", 0);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        Ennemi belier = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(32.4 * 0.75, t.degatsContre(belier), 0.01);
    }

    @Test
    void quandCatapulteContreBelier_alorsBonus125() {
        Tourelle t = new Tourelle("Tour", 0);
        t.ajouterForme(new Cercle("Catapulte", 3));
        Ennemi belier = new Ennemi("Belier", new Rectangle("R", 4, 4));
        assertEquals(19.79 * 1.25, t.degatsContre(belier), 0.1);
    }

    @Test
    void quandArcherContreCavalerie_alorsPasDeBonus() {
        Tourelle t = new Tourelle("Tour", 0);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        Ennemi cavalerie = new Ennemi("Cavalerie", new Triangle("T", 4, 3));
        assertEquals(32.4, t.degatsContre(cavalerie), 0.01);
    }

    // ZONE VS MONO

    @Test
    void quandTourelleAvecCercle_alorsEstZone() {
        Tourelle t = new Tourelle("Tour", 0);
        t.ajouterForme(new Cercle("Catapulte", 3));
        assertTrue(t.estZone());
    }

    @Test
    void quandTourelleSanseCercle_alorsEstPasZone() {
        Tourelle t = new Tourelle("Tour", 0);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        assertFalse(t.estZone());
    }

    // ORIENTATION

    @Test
    void quandSetOrientation90_alorsOrientationEgale90() {
        Tourelle t = new Tourelle("Tour", 0);
        t.setOrientation(90);
        assertEquals(90, t.getOrientation());
    }

    @Test
    void quandOrientationInvalide_alorsLeveIllegalArgument() {
        Tourelle t = new Tourelle("Tour", 0);
        assertThrows(IllegalArgumentException.class, () -> {
            t.setOrientation(45);
        });
    }

    // SETTERS VALIDATION

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Tourelle("", 0);
        });
    }

    @Test
    void quandPositionNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Tourelle("Tour", -1);
        });
    }

    // COPIE DEFENSIVE

    @Test
    void quandGetFormesClear_alorsListeOrigineIntacte() {
        Tourelle t = new Tourelle("Tour A", 3);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        t.getFormes().clear();
        assertEquals(1, t.getNombreFormes());
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        Tourelle t = new Tourelle("Tour A", 3);
        String str = t.toString();
        assertTrue(str.contains("Tour A"));
        assertTrue(str.contains("position=3"));
        assertTrue(str.contains("orientation=0°"));
    }
}