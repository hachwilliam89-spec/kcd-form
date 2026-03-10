package com.kcdformes.model.defense;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourelleTest {

    private Tourelle tourelle;
    private Triangle triangle;
    private Cercle cercle;
    private Rectangle rectangle;

    @BeforeEach
    void setUp() {
        tourelle = new Tourelle("A", 1);
        triangle = new Triangle("Archer", 4, 3);
        cercle = new Cercle("Catapulte", 3);
        rectangle = new Rectangle("Muraille", 4, 4);
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeurValide_alorsAttributsCorrects() {
        assertEquals("A", tourelle.getNom());
        assertEquals(1, tourelle.getPosition());
        assertEquals(3, tourelle.getPortee());
        assertEquals(0, tourelle.getNombreFormes());
    }

    @Test
    void quandNomNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Tourelle(null, 1));
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Tourelle("", 1));
    }

    @Test
    void quandNomBlanc_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Tourelle("   ", 1));
    }

    @Test
    void quandPositionNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Tourelle("Test", -1));
    }

    @Test
    void quandPositionZero_alorsAccepte() {
        Tourelle t = new Tourelle("Test", 0);
        assertEquals(0, t.getPosition());
    }

    // AJOUT DE FORMES

    @Test
    void quandAjouterForme_alorsNombreFormesAugmente() {
        assertTrue(tourelle.ajouterForme(triangle));
        assertEquals(1, tourelle.getNombreFormes());
    }

    @Test
    void quandAjouterFormeNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tourelle.ajouterForme(null));
    }

    @Test
    void quandAjouterFormeMax_alorsLeveIllegalState() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        tourelle.ajouterForme(rectangle);
        assertThrows(IllegalStateException.class, () -> tourelle.ajouterForme(new Triangle("Extra", 2, 3)));
    }

    @Test
    void quandAjouterTroisFormes_alorsNombreFormesEgal3() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        tourelle.ajouterForme(rectangle);
        assertEquals(3, tourelle.getNombreFormes());
    }

    // SUPPRESSION DE FORMES

    @Test
    void quandSupprimerForme_alorsNombreFormesDiminue() {
        tourelle.ajouterForme(triangle);
        assertTrue(tourelle.supprimerForme(triangle));
        assertEquals(0, tourelle.getNombreFormes());
    }

    @Test
    void quandSupprimerFormeInexistante_alorsRetourneFalse() {
        assertFalse(tourelle.supprimerForme(triangle));
    }

    // COMPTEURS PAR TYPE

// ANCIEN — compterRectangles / compterCercles / compterTriangles
// NOUVEAU — on teste via les méthodes polymorphiques

    @Test
    void quandDeuxRectangles_alorsPVPositif() {
        tourelle.ajouterForme(rectangle);
        tourelle.ajouterForme(rectangle);
        assertTrue(tourelle.getPV() > 0);
    }

    @Test
    void quandAucuneForme_alorsPVZero() {
        assertEquals(0, tourelle.getPV());
    }

    @Test
    void quandUnCercle_alorsAoEActif() {
        tourelle.ajouterForme(cercle);
        assertTrue(tourelle.hasAoE());
    }

    @Test
    void quandAucunCercle_alorsAoEInactif() {
        tourelle.ajouterForme(triangle);
        assertFalse(tourelle.hasAoE());
    }

    @Test
    void quandUnTriangle_alorsUnTir() {
        tourelle.ajouterForme(triangle);
        assertEquals(1, tourelle.getNombreTirs());
    }

    @Test
    void quandAucunTriangle_alorsZeroTirs() {
        tourelle.ajouterForme(cercle);
        assertEquals(0, tourelle.getNombreTirs());
    }
    // STATS DE GAMEPLAY

    @Test
    void quandDeuxTriangles_alorsDeuxTirs() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(new Triangle("Archer2", 2, 3));
        assertEquals(2, tourelle.getNombreTirs());
    }

    @Test
    void quandSansTriangle_alorsZeroTirs() {
        tourelle.ajouterForme(cercle);
        assertEquals(0, tourelle.getNombreTirs());
    }

    @Test
    void quandCercle_alorsAoETrue() {
        tourelle.ajouterForme(cercle);
        assertTrue(tourelle.hasAoE());
    }

    @Test
    void quandSansCercle_alorsAoEFalse() {
        tourelle.ajouterForme(triangle);
        assertFalse(tourelle.hasAoE());
    }

    @Test
    void quandDeuxCercles_alorsRayonCumule() {
        Cercle c1 = new Cercle("C1", 3);
        Cercle c2 = new Cercle("C2", 5);
        tourelle.ajouterForme(c1);
        tourelle.ajouterForme(c2);
        assertEquals(8.0, tourelle.getRayonZone(), 0.001);
    }

    @Test
    void quandSansCercle_alorsRayonZero() {
        tourelle.ajouterForme(triangle);
        assertEquals(0.0, tourelle.getRayonZone(), 0.001);
    }

    @Test
    void quandUnRectangle_alorsPVCalcules() {
        tourelle.ajouterForme(rectangle);
        assertEquals(160.0, tourelle.getPV(), 0.001);
    }

    @Test
    void quandDeuxRectangles_alorsPVDoubles() {
        tourelle.ajouterForme(rectangle);
        tourelle.ajouterForme(new Rectangle("Mur2", 4, 4));
        assertEquals(320.0, tourelle.getPV(), 0.001);
    }

    @Test
    void quandSansRectangle_alorsPVZero() {
        tourelle.ajouterForme(triangle);
        assertEquals(0.0, tourelle.getPV(), 0.001);
    }

    // CALCULS POLYMORPHIQUES

    @Test
    void quandMixFormes_alorsDpsTotalCumule() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        double expected = triangle.dps() + cercle.dps();
        assertEquals(expected, tourelle.dpsTotal(), 0.001);
    }

    @Test
    void quandRectangleSeul_alorsDpsZero() {
        tourelle.ajouterForme(rectangle);
        assertEquals(0.0, tourelle.dpsTotal(), 0.001);
    }

    @Test
    void quandTourelleVide_alorsDpsZero() {
        assertEquals(0.0, tourelle.dpsTotal(), 0.001);
    }

    @Test
    void quandTroisFormes_alorsCoutTotalCumule() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        tourelle.ajouterForme(rectangle);
        int expected = triangle.cout() + cercle.cout() + rectangle.cout();
        assertEquals(expected, tourelle.coutTotal());
    }

    @Test
    void quandDeuxFormes_alorsAireTotaleCumulee() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        double expected = triangle.aire() + cercle.aire();
        assertEquals(expected, tourelle.aireTotale(), 0.001);
    }

    @Test
    void quandDeuxFormes_alorsPerimetreTotalCumule() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(rectangle);
        double expected = triangle.perimetre() + rectangle.perimetre();
        assertEquals(expected, tourelle.perimetreTotal(), 0.001);
    }

    // COMBINAISONS GAMEPLAY

    @Test
    void quandTripleTriangle_alors3TirsSansAoESansPV() {
        tourelle.ajouterForme(new Triangle("A1", 3, 4));
        tourelle.ajouterForme(new Triangle("A2", 3, 4));
        tourelle.ajouterForme(new Triangle("A3", 3, 4));
        assertEquals(3, tourelle.getNombreTirs());
        assertFalse(tourelle.hasAoE());
        assertEquals(0.0, tourelle.getPV(), 0.001);
    }

    @Test
    void quandTripleRectangle_alorsPVEnormesZeroDps() {
        tourelle.ajouterForme(new Rectangle("M1", 4, 4));
        tourelle.ajouterForme(new Rectangle("M2", 4, 4));
        tourelle.ajouterForme(new Rectangle("M3", 4, 4));
        assertEquals(0, tourelle.getNombreTirs());
        assertFalse(tourelle.hasAoE());
        assertEquals(480.0, tourelle.getPV(), 0.001);
        assertEquals(0.0, tourelle.dpsTotal(), 0.001);
    }

    @Test
    void quandTripleCercle_alorsAoEGrosRayon() {
        tourelle.ajouterForme(new Cercle("C1", 3));
        tourelle.ajouterForme(new Cercle("C2", 3));
        tourelle.ajouterForme(new Cercle("C3", 3));
        assertEquals(0, tourelle.getNombreTirs());
        assertTrue(tourelle.hasAoE());
        assertEquals(9.0, tourelle.getRayonZone(), 0.001);
    }

    @Test
    void quandComboEquilibree_alorsToutesStatsPositives() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        tourelle.ajouterForme(rectangle);
        assertEquals(1, tourelle.getNombreTirs());
        assertTrue(tourelle.hasAoE());
        assertTrue(tourelle.getPV() > 0);
        assertTrue(tourelle.dpsTotal() > 0);
    }

    // COPIE DEFENSIVE

    @Test
    void quandModifierCopie_alorsOriginalInchange() {
        tourelle.ajouterForme(triangle);
        tourelle.getFormes().add(cercle);
        assertEquals(1, tourelle.getNombreFormes());
    }

    // PORTEE

    @Test
    void quandConstructeur_alorsPorteeParDefaut() {
        assertEquals(3, tourelle.getPortee());
    }

    @Test
    void quandSetPorteeValide_alorsPorteeChange() {
        tourelle.setPortee(5);
        assertEquals(5, tourelle.getPortee());
    }

    @Test
    void quandSetPorteeZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tourelle.setPortee(0));
    }

    @Test
    void quandSetPorteeNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tourelle.setPortee(-1));
    }

    // SETTERS

    @Test
    void quandSetNomValide_alorsNomChange() {
        tourelle.setNom("Beta");
        assertEquals("Beta", tourelle.getNom());
    }

    @Test
    void quandSetNomNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tourelle.setNom(null));
    }

    @Test
    void quandSetPositionValide_alorsPositionChange() {
        tourelle.setPosition(5);
        assertEquals(5, tourelle.getPosition());
    }

    @Test
    void quandSetPositionNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tourelle.setPosition(-1));
    }

    // TO STRING

    @Test
    void quandToStringAvecTriangleEtCercle_alorsContientTirsEtAoE() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        String result = tourelle.toString();
        assertTrue(result.contains("A"));
        assertTrue(result.contains("tirs=1"));
        assertTrue(result.contains("AoE"));
        assertTrue(result.contains("DPS="));
    }

    @Test
    void quandToStringAvecRectangle_alorsContientPVEtDpsZero() {
        tourelle.ajouterForme(rectangle);
        String result = tourelle.toString();
        assertTrue(result.contains("PV="));
        assertTrue(result.contains("DPS=0"));
    }

    // DEGATS CONTRE

    @Test
    void quandDegatsContreEnnemiNull_alorsZero() {
        tourelle.ajouterForme(triangle);
        assertEquals(0.0, tourelle.degatsContre(null), 0.001);
    }

    @Test
    void quandDegatsContreEnnemiMort_alorsZero() {
        tourelle.ajouterForme(triangle);
        Ennemi ennemi = new Ennemi("Cavalier", new Triangle("forme", 2, 2));
        ennemi.subirDegats(9999);
        assertEquals(0.0, tourelle.degatsContre(ennemi), 0.001);
    }
}