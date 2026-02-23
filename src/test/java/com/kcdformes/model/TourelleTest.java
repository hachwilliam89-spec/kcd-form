package com.kcdformes.model;

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
        tourelle = new Tourelle("Alpha", 1);
        triangle = new Triangle("Archer", 4, 3);
        cercle = new Cercle("Catapulte", 3);
        rectangle = new Rectangle("Muraille", 4, 4);
    }

    // CONSTRUCTEUR

    @Test
    void constructeurValide() {
        assertEquals("Alpha", tourelle.getNom());
        assertEquals(1, tourelle.getPosition());
        assertEquals(0, tourelle.getNombreFormes());
    }

    @Test
    void constructeurNomNull() {
        assertThrows(IllegalArgumentException.class, () -> new Tourelle(null, 1));
    }

    @Test
    void constructeurNomVide() {
        assertThrows(IllegalArgumentException.class, () -> new Tourelle("", 1));
    }

    @Test
    void constructeurNomBlanc() {
        assertThrows(IllegalArgumentException.class, () -> new Tourelle("   ", 1));
    }

    @Test
    void constructeurPositionNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Tourelle("Test", -1));
    }

    @Test
    void constructeurPositionZero() {
        Tourelle t = new Tourelle("Test", 0);
        assertEquals(0, t.getPosition());
    }

    // AJOUT DE FORMES

    @Test
    void ajouterForme() {
        assertTrue(tourelle.ajouterForme(triangle));
        assertEquals(1, tourelle.getNombreFormes());
    }

    @Test
    void ajouterFormeNull() {
        assertThrows(IllegalArgumentException.class, () -> tourelle.ajouterForme(null));
    }

    @Test
    void ajouterFormeMax() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        tourelle.ajouterForme(rectangle);
        assertThrows(IllegalStateException.class, () -> tourelle.ajouterForme(new Triangle("Extra", 2, 3)));
    }

    @Test
    void ajouterTroisFormesValide() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        tourelle.ajouterForme(rectangle);
        assertEquals(3, tourelle.getNombreFormes());
    }

    // SUPPRESSION DE FORMES

    @Test
    void supprimerForme() {
        tourelle.ajouterForme(triangle);
        assertTrue(tourelle.supprimerForme(triangle));
        assertEquals(0, tourelle.getNombreFormes());
    }

    @Test
    void supprimerFormeInexistante() {
        assertFalse(tourelle.supprimerForme(triangle));
    }

    // COMPTEURS PAR TYPE

    @Test
    void compterTriangles() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        tourelle.ajouterForme(new Triangle("Archer2", 2, 3));
        assertEquals(2, tourelle.compterTriangles());
    }

    @Test
    void compterRectangles() {
        tourelle.ajouterForme(rectangle);
        tourelle.ajouterForme(rectangle);
        assertEquals(2, tourelle.compterRectangles());
    }

    @Test
    void compterCercles() {
        tourelle.ajouterForme(cercle);
        assertEquals(1, tourelle.compterCercles());
    }

    @Test
    void compteursVides() {
        assertEquals(0, tourelle.compterTriangles());
        assertEquals(0, tourelle.compterRectangles());
        assertEquals(0, tourelle.compterCercles());
    }

    // STATS DE GAMEPLAY

    @Test
    void nombreTirs() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(new Triangle("Archer2", 2, 3));
        assertEquals(2, tourelle.getNombreTirs());
    }

    @Test
    void nombreTirsSansTriangle() {
        tourelle.ajouterForme(cercle);
        assertEquals(0, tourelle.getNombreTirs());
    }

    @Test
    void hasAoEAvecCercle() {
        tourelle.ajouterForme(cercle);
        assertTrue(tourelle.hasAoE());
    }

    @Test
    void hasAoESansCercle() {
        tourelle.ajouterForme(triangle);
        assertFalse(tourelle.hasAoE());
    }

    @Test
    void rayonZone() {
        Cercle c1 = new Cercle("C1", 3);
        Cercle c2 = new Cercle("C2", 5);
        tourelle.ajouterForme(c1);
        tourelle.ajouterForme(c2);
        assertEquals(8.0, tourelle.getRayonZone(), 0.001);
    }

    @Test
    void rayonZoneSansCercle() {
        tourelle.ajouterForme(triangle);
        assertEquals(0.0, tourelle.getRayonZone(), 0.001);
    }

    @Test
    void pvAvecRectangles() {
        tourelle.ajouterForme(rectangle); // perimetre = 16, PV = 160
        assertEquals(160.0, tourelle.getPV(), 0.001);
    }

    @Test
    void pvDoubleRectangle() {
        tourelle.ajouterForme(rectangle);
        tourelle.ajouterForme(new Rectangle("Mur2", 4, 4));
        assertEquals(320.0, tourelle.getPV(), 0.001);
    }

    @Test
    void pvSansRectangle() {
        tourelle.ajouterForme(triangle);
        assertEquals(0.0, tourelle.getPV(), 0.001);
    }

    // CALCULS POLYMORPHIQUES

    @Test
    void dpsTotalMix() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        double expected = triangle.dps() + cercle.dps();
        assertEquals(expected, tourelle.dpsTotal(), 0.001);
    }

    @Test
    void dpsTotalRectangleZero() {
        tourelle.ajouterForme(rectangle);
        assertEquals(0.0, tourelle.dpsTotal(), 0.001);
    }

    @Test
    void dpsTotalVide() {
        assertEquals(0.0, tourelle.dpsTotal(), 0.001);
    }

    @Test
    void coutTotal() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        tourelle.ajouterForme(rectangle);
        int expected = triangle.cout() + cercle.cout() + rectangle.cout();
        assertEquals(expected, tourelle.coutTotal());
    }

    @Test
    void aireTotale() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        double expected = triangle.aire() + cercle.aire();
        assertEquals(expected, tourelle.aireTotale(), 0.001);
    }

    @Test
    void perimetreTotale() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(rectangle);
        double expected = triangle.perimetre() + rectangle.perimetre();
        assertEquals(expected, tourelle.perimetreTotale(), 0.001);
    }

    // COMBINAISONS GAMEPLAY

    @Test
    void comboTripleTriangle() {
        tourelle.ajouterForme(new Triangle("A1", 3, 4));
        tourelle.ajouterForme(new Triangle("A2", 3, 4));
        tourelle.ajouterForme(new Triangle("A3", 3, 4));
        assertEquals(3, tourelle.getNombreTirs());
        assertFalse(tourelle.hasAoE());
        assertEquals(0.0, tourelle.getPV(), 0.001);
    }

    @Test
    void comboTripleRectangle() {
        tourelle.ajouterForme(new Rectangle("M1", 4, 4));
        tourelle.ajouterForme(new Rectangle("M2", 4, 4));
        tourelle.ajouterForme(new Rectangle("M3", 4, 4));
        assertEquals(0, tourelle.getNombreTirs());
        assertFalse(tourelle.hasAoE());
        assertEquals(480.0, tourelle.getPV(), 0.001);
        assertEquals(0.0, tourelle.dpsTotal(), 0.001);
    }

    @Test
    void comboTripleCercle() {
        tourelle.ajouterForme(new Cercle("C1", 3));
        tourelle.ajouterForme(new Cercle("C2", 3));
        tourelle.ajouterForme(new Cercle("C3", 3));
        assertEquals(0, tourelle.getNombreTirs());
        assertTrue(tourelle.hasAoE());
        assertEquals(9.0, tourelle.getRayonZone(), 0.001);
    }

    @Test
    void comboEquilibree() {
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
    void copieDefensive() {
        tourelle.ajouterForme(triangle);
        tourelle.getFormes().add(cercle);
        assertEquals(1, tourelle.getNombreFormes());
    }

    // SETTERS

    @Test
    void setNomValide() {
        tourelle.setNom("Beta");
        assertEquals("Beta", tourelle.getNom());
    }

    @Test
    void setNomNull() {
        assertThrows(IllegalArgumentException.class, () -> tourelle.setNom(null));
    }

    @Test
    void setPositionValide() {
        tourelle.setPosition(5);
        assertEquals(5, tourelle.getPosition());
    }

    @Test
    void setPositionNegative() {
        assertThrows(IllegalArgumentException.class, () -> tourelle.setPosition(-1));
    }

    // TO STRING

    @Test
    void toStringContientInfos() {
        tourelle.ajouterForme(triangle);
        tourelle.ajouterForme(cercle);
        String result = tourelle.toString();
        assertTrue(result.contains("Alpha"));
        assertTrue(result.contains("tirs=1"));
        assertTrue(result.contains("AoE"));
        assertTrue(result.contains("DPS="));
    }

    @Test
    void toStringMurailleSansDPS() {
        tourelle.ajouterForme(rectangle);
        String result = tourelle.toString();
        assertTrue(result.contains("PV="));
        assertTrue(result.contains("DPS=0"));
    }
}