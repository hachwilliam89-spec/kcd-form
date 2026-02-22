package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VagueTest {

    @Test
    void quandVagueVide_alorsZeroEnnemis() {
        Vague v = new Vague(1, 1.0, 1.0);
        assertEquals(0, v.getNombreEnnemis());
        assertEquals(0, v.getNombreVivants());
    }

    @Test
    void quandAjouterEnnemi_alorsNombreAugmente() {
        Vague v = new Vague(1, 1.0, 1.0);
        v.ajouterEnnemi(new Ennemi("Cavalerie", new Triangle("Tri", 4, 3)));
        assertEquals(1, v.getNombreEnnemis());
    }

    @Test
    void quandSpawnSuivant_alorsRetourneDansLOrdre() {
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e1 = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        Ennemi e2 = new Ennemi("Belier", new Rectangle("Rec", 4, 4));
        v.ajouterEnnemi(e1);
        v.ajouterEnnemi(e2);
        assertEquals(e1, v.spawnSuivant());
        assertEquals(e2, v.spawnSuivant());
    }

    @Test
    void quandTousSpawnes_alorsSpawnRetourneNull() {
        Vague v = new Vague(1, 1.0, 1.0);
        v.ajouterEnnemi(new Ennemi("Cavalerie", new Triangle("Tri", 4, 3)));
        v.spawnSuivant();
        assertNull(v.spawnSuivant());
    }

    @Test
    void quandEnnemisVivants_alorsVaguePasTerminee() {
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        v.ajouterEnnemi(e);
        v.spawnSuivant();
        assertFalse(v.estTerminee());
    }

    @Test
    void quandTousMortsEtSpawnes_alorsVagueTerminee() {
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        v.ajouterEnnemi(e);
        v.spawnSuivant();
        e.subirDegats(999);
        assertTrue(v.estTerminee());
    }

    @Test
    void quandUnEnnemiMeurt_alorsNombreVivantsReduit() {
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e1 = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3));
        Ennemi e2 = new Ennemi("Belier", new Rectangle("Rec", 4, 4));
        v.ajouterEnnemi(e1);
        v.ajouterEnnemi(e2);
        assertEquals(2, v.getNombreVivants());
        e1.subirDegats(999);
        assertEquals(1, v.getNombreVivants());
    }

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        Vague v = new Vague(1, 2.0, 1.5);
        assertEquals(1, v.getNumero());
        assertEquals(2.0, v.getDelaiSpawn());
        assertEquals(1.5, v.getCoeffDifficulte());
    }

    @Test
    void quandGetEnnemis_alorsCopieDefensive() {
        Vague v = new Vague(1, 1.0, 1.0);
        v.ajouterEnnemi(new Ennemi("Cavalerie", new Triangle("Tri", 4, 3)));
        v.getEnnemis().clear();
        assertEquals(1, v.getNombreEnnemis());
    }

    @Test
    void quandAjouterEnnemiNull_alorsLeveIllegalArgument() {
        Vague v = new Vague(1, 1.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> {
            v.ajouterEnnemi(null);
        });
    }

    @Test
    void quandNumeroNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Vague(-1, 1.0, 1.0);
        });
    }

    @Test
    void quandDelaiSpawnNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Vague(1, -1.0, 1.0);
        });
    }

    @Test
    void quandCoeffDifficulteZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Vague(1, 1.0, 0);
        });
    }

    @Test
    void quandToString_alorsContientInfos() {
        Vague v = new Vague(1, 1.0, 1.0);
        String str = v.toString();
        assertTrue(str.contains("Vague 1"));
    }
}