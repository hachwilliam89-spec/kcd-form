package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VagueTest {

    @Test
    void quandVagueVide_alorsZeroEnnemis() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        assertEquals(0, v.getNombreEnnemis());
        assertEquals(0, v.getNombreVivants());
    }

    @Test
    void quandAjouterEnnemi_alorsNombreAugmente() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        v.ajouterEnnemi(new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v.getCoeffDifficulte()));
        assertEquals(1, v.getNombreEnnemis());
    }

    @Test
    void quandSpawnSuivant_alorsRetourneDansLOrdre() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        Ennemi e1 = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v.getCoeffDifficulte());
        Ennemi e2 = new Ennemi("Belier", new Rectangle("Rec", 4, 4), v.getCoeffDifficulte());
        v.ajouterEnnemi(e1);
        v.ajouterEnnemi(e2);
        assertEquals(e1, v.spawnSuivant());
        assertEquals(e2, v.spawnSuivant());
    }

    @Test
    void quandTousSpawnes_alorsSpawnRetourneNull() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        v.ajouterEnnemi(new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v.getCoeffDifficulte()));
        v.spawnSuivant();
        assertNull(v.spawnSuivant());
    }

    // TIMER

    @Test
    void quandTimerPasExpire_alorsVaguePasTerminee() {
        Vague v = new Vague(1, 1.0, 1.0, 10);
        v.tick();
        assertFalse(v.estTerminee());
    }

    @Test
    void quandTimerExpire_alorsVagueTerminee() {
        Vague v = new Vague(1, 1.0, 1.0, 3);
        v.tick(); // 1
        v.tick(); // 2
        v.tick(); // 3
        assertTrue(v.estTerminee());
    }

    @Test
    void quandTick_alorsTempsEcouleIncrement() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        assertEquals(0, v.getTempsEcoule());
        v.tick();
        assertEquals(1, v.getTempsEcoule());
    }

    // SURVIVANTS

    @Test
    void quandUnEnnemiMeurt_alorsNombreVivantsReduit() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        Ennemi e1 = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v.getCoeffDifficulte());
        Ennemi e2 = new Ennemi("Belier", new Rectangle("Rec", 4, 4), v.getCoeffDifficulte());
        v.ajouterEnnemi(e1);
        v.ajouterEnnemi(e2);
        assertEquals(2, v.getNombreVivants());
        e1.subirDegats(999);
        assertEquals(1, v.getNombreVivants());
    }

    @Test
    void quandGetEnnemisSurvivants_alorsSeulementLesVivants() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        Ennemi e1 = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v.getCoeffDifficulte());
        Ennemi e2 = new Ennemi("Belier", new Rectangle("Rec", 4, 4), v.getCoeffDifficulte());
        v.ajouterEnnemi(e1);
        v.ajouterEnnemi(e2);
        e1.subirDegats(999);
        List<Ennemi> survivants = v.getEnnemisSurvivants();
        assertEquals(1, survivants.size());
        assertEquals(e2, survivants.get(0));
    }

    @Test
    void quandAjouterEnnemis_alorsTousAjoutes() {
        Vague v1 = new Vague(1, 1.0, 1.0, 45);
        Ennemi e1 = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v1.getCoeffDifficulte());
        Ennemi e2 = new Ennemi("Belier", new Rectangle("Rec", 4, 4), v1.getCoeffDifficulte());
        v1.ajouterEnnemi(e1);
        v1.ajouterEnnemi(e2);

        Vague v2 = new Vague(2, 1.0, 1.0, 45);
        v2.ajouterEnnemis(v1.getEnnemisSurvivants());
        assertEquals(2, v2.getNombreEnnemis());
    }

    @Test
    void quandAjouterEnnemisNull_alorsLeveIllegalArgument() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        assertThrows(IllegalArgumentException.class, () -> v.ajouterEnnemis(null));
    }

    // RECOMPENSE

    @Test
    void quandCalculerRecompense_alorsSommeDesMorts() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        Ennemi e1 = new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v.getCoeffDifficulte());
        Ennemi e2 = new Ennemi("Belier", new Rectangle("Rec", 4, 4), v.getCoeffDifficulte());
        v.ajouterEnnemi(e1);
        v.ajouterEnnemi(e2);
        e1.subirDegats(999);
        int recompense = v.calculerRecompense();
        assertTrue(recompense > 0);
    }

    @Test
    void quandAucunMort_alorsRecompenseZero() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        v.ajouterEnnemi(new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v.getCoeffDifficulte()));
        assertEquals(0, v.calculerRecompense());
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        Vague v = new Vague(1, 2.0, 1.5, 30);
        assertEquals(1, v.getNumero());
        assertEquals(2.0, v.getDelaiSpawn());
        assertEquals(1.5, v.getCoeffDifficulte());
        assertEquals(30, v.getDureeSecondes());
        assertEquals(0, v.getTempsEcoule());
    }

    @Test
    void quandGetEnnemis_alorsCopieDefensive() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        v.ajouterEnnemi(new Ennemi("Cavalerie", new Triangle("Tri", 4, 3), v.getCoeffDifficulte()));
        v.getEnnemis().clear();
        assertEquals(1, v.getNombreEnnemis());
    }

    // VALIDATION

    @Test
    void quandAjouterEnnemiNull_alorsLeveIllegalArgument() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        assertThrows(IllegalArgumentException.class, () -> v.ajouterEnnemi(null));
    }

    @Test
    void quandNumeroNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(-1, 1.0, 1.0, 45));
    }

    @Test
    void quandDelaiSpawnNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(1, -1.0, 1.0, 45));
    }

    @Test
    void quandCoeffDifficulteZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(1, 1.0, 0, 45));
    }

    @Test
    void quandDureeSecondesZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(1, 1.0, 1.0, 0));
    }

    @Test
    void quandDureeSecondesNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(1, 1.0, 1.0, -5));
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        Vague v = new Vague(1, 1.0, 1.0, 45);
        String str = v.toString();
        assertTrue(str.contains("Vague 1"));
        assertTrue(str.contains("temps="));
    }
}