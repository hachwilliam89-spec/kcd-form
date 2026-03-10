package com.kcdformes.model.gameplay;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VagueTest {

    // CONSTRUCTEUR

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        Vague v = new Vague(1, 1.5, 30);
        assertEquals(1, v.getNumero());
        assertEquals(1.5, v.getCoeffDifficulte());
        assertEquals(30, v.getDureeSecondes());
        assertEquals(0, v.getTempsEcoule());
        assertEquals(0, v.getNombreEnnemis());
        assertFalse(v.isDerniereVague());
    }

    @Test
    void quandNumeroNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(-1, 1.0, 45));
    }

    @Test
    void quandCoeffDifficulteZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(1, 0, 45));
    }

    @Test
    void quandDureeSecondesZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(1, 1.0, 0));
    }

    @Test
    void quandDureeSecondesNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Vague(1, 1.0, -5));
    }

    // GENERATION ESCOUADES

    @Test
    void quandGenererEscouades_alorsNombreEnnemisCorrect() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(4, 2, 0);
        assertEquals(6, v.getNombreEnnemis());
    }

    @Test
    void quandGenererEscouadesAvecBeliers_alorsTousTypes() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(3, 3, 2);
        assertEquals(8, v.getNombreEnnemis());
    }

    @Test
    void quandGenererEscouadesZeroPartout_alorsAucunEnnemi() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(0, 0, 0);
        assertEquals(0, v.getNombreEnnemis());
        assertEquals(0, v.getEscouades().size());
    }

    @Test
    void quandGenererEscouades6Cavaliers_alorsDeuxEscouades() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(6, 0, 0);
        assertEquals(2, v.getEscouades().size());
        assertEquals(6, v.getNombreEnnemis());
    }

    @Test
    void quandGenererEscouades_alorsPremiereEscouadeSpawnImmediat() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(3, 0, 0);
        assertEquals(0, v.getEscouades().get(0).getDelaiAvantSpawn());
    }

    @Test
    void quandGenererEscouadesAvecCoeff_alorsEnnemisScales() {
        Vague v = new Vague(1, 2.0, 45);
        v.genererEscouades(1, 0, 0);

        Ennemi cavalier = v.getEscouades().get(0).getEnnemis().get(0);
        Ennemi cavalierNormal = new Ennemi("test", new Triangle("forme", 4, 3), 1.0);
        assertTrue(cavalier.getPvMax() > cavalierNormal.getPvMax());
    }

    @Test
    void quandGenererEscouadesDeuxFois_alorsResetComplet() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(5, 5, 5);
        assertEquals(15, v.getNombreEnnemis());

        v.genererEscouades(2, 0, 0);
        assertEquals(2, v.getNombreEnnemis());
    }

    // SPAWN

    @Test
    void quandSpawnSuivant_alorsRetourneEnnemiOuNull() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(2, 0, 0);

        Ennemi premier = v.spawnSuivant();
        assertNotNull(premier);
        assertEquals(1, v.getNombreSpawnes());
    }

    @Test
    void quandTousSpawnes_alorsSpawnRetourneNull() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(1, 0, 0);

        v.spawnSuivant();
        assertNull(v.spawnSuivant());
        assertNull(v.spawnSuivant());
    }

    @Test
    void quandVagueVide_alorsSpawnRetourneNull() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(0, 0, 0);
        assertNull(v.spawnSuivant());
    }

    // ENNEMIS ACTIFS

    @Test
    void quandAucunSpawn_alorsEnnemisActifsVide() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(3, 0, 0);
        assertEquals(0, v.getEnnemisActifs().size());
    }

    @Test
    void quandSpawn_alorsEnnemisActifsContientLesSpawnes() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(2, 0, 0);

        Ennemi e = v.spawnSuivant();
        assertNotNull(e);
        assertEquals(1, v.getEnnemisActifs().size());
        assertTrue(v.getEnnemisActifs().contains(e));
    }

    // TIMER

    @Test
    void quandTick_alorsTempsEcouleAugmente() {
        Vague v = new Vague(1, 1.0, 45);
        assertEquals(0, v.getTempsEcoule());
        v.tick();
        assertEquals(1, v.getTempsEcoule());
        v.tick();
        assertEquals(2, v.getTempsEcoule());
    }

    @Test
    void quandTimerPasExpire_alorsVaguePasTerminee() {
        Vague v = new Vague(1, 1.0, 10);
        v.genererEscouades(3, 0, 0); // ajouter des ennemis vivants
        v.tick();
        assertFalse(v.estTerminee());
    }

    @Test
    void quandTimerExpire_alorsVagueTerminee() {
        Vague v = new Vague(1, 1.0, 3);
        v.tick();
        v.tick();
        v.tick();
        assertTrue(v.estTerminee());
    }

    // DERNIERE VAGUE

    @Test
    void quandDerniereVagueAvecVivants_alorsPasTerminee() {
        Vague v = new Vague(1, 1.0, 3);
        v.setDerniereVague(true);
        v.genererEscouades(1, 0, 0);

        v.spawnSuivant();
        v.tick(); v.tick(); v.tick();

        assertFalse(v.estTerminee());
    }

    @Test
    void quandDerniereVagueTousMorts_alorsTerminee() {
        Vague v = new Vague(1, 1.0, 45);
        v.setDerniereVague(true);
        v.genererEscouades(1, 0, 0);

        Ennemi e = v.spawnSuivant();
        assertNotNull(e);
        e.subirDegats(9999);

        assertTrue(v.estTerminee());
    }

    @Test
    void quandDerniereVaguePasEncoreToutSpawne_alorsPasTerminee() {
        Vague v = new Vague(1, 1.0, 45);
        v.setDerniereVague(true);
        v.genererEscouades(3, 0, 0);

        Ennemi e = v.spawnSuivant();
        assertNotNull(e);
        e.subirDegats(9999);

        assertFalse(v.estTerminee());
    }

    // SURVIVANTS

    @Test
    void quandUnEnnemiMeurt_alorsNombreVivantsReduit() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(2, 0, 0);
        assertEquals(2, v.getNombreVivants());

        v.getEscouades().get(0).getEnnemis().get(0).subirDegats(9999);
        assertEquals(1, v.getNombreVivants());
    }

    @Test
    void quandGetEnnemisSurvivants_alorsSeulementLesVivants() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(2, 0, 0);

        Ennemi premier = v.getEscouades().get(0).getEnnemis().get(0);
        premier.subirDegats(9999);

        List<Ennemi> survivants = v.getEnnemisSurvivants();
        assertEquals(1, survivants.size());
        assertFalse(survivants.contains(premier));
    }

    // AJOUT SURVIVANTS VAGUE PRECEDENTE

    @Test
    void quandAjouterSurvivants_alorsInsereEnTeteDeLaVague() {
        Vague v = new Vague(2, 1.0, 45);
        v.genererEscouades(2, 0, 0);
        int escouadesAvant = v.getEscouades().size();

        Ennemi survivant = new Ennemi("Survivant", new Triangle("forme", 4, 3));
        v.ajouterEnnemis(List.of(survivant));

        assertEquals(escouadesAvant + 1, v.getEscouades().size());
        assertTrue(v.getEscouades().get(0).getEnnemis().contains(survivant));
    }

    @Test
    void quandAjouterSurvivantsNull_alorsPasDerreur() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(2, 0, 0);
        int avant = v.getNombreEnnemis();

        v.ajouterEnnemis(null);
        assertEquals(avant, v.getNombreEnnemis());
    }

    @Test
    void quandAjouterSurvivantsVide_alorsPasDerreur() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(2, 0, 0);
        int avant = v.getNombreEnnemis();

        v.ajouterEnnemis(List.of());
        assertEquals(avant, v.getNombreEnnemis());
    }

    // POINTS SCORE

    @Test
    void quandCalculerPointsScore_alorsSommeDesMorts() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(2, 0, 0);

        Ennemi premier = v.getEscouades().get(0).getEnnemis().get(0);
        premier.subirDegats(9999);

        int points = v.calculerPointsScore();
        assertTrue(points > 0);
    }

    @Test
    void quandAucunMort_alorsPointsScoreZero() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(2, 0, 0);
        assertEquals(0, v.calculerPointsScore());
    }

    // GETTERS DEFENSIFS

    @Test
    void quandGetEscouades_alorsCopieDefensive() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(3, 0, 0);
        v.getEscouades().clear();
        assertTrue(v.getNombreEnnemis() > 0);
    }

    // SETTERS

    @Test
    void quandSetNumeroValide_alorsChange() {
        Vague v = new Vague(1, 1.0, 45);
        v.setNumero(5);
        assertEquals(5, v.getNumero());
    }

    @Test
    void quandSetNumeroZero_alorsLeveIllegalArgument() {
        Vague v = new Vague(1, 1.0, 45);
        assertThrows(IllegalArgumentException.class, () -> v.setNumero(0));
    }

    @Test
    void quandSetCoeffValide_alorsChange() {
        Vague v = new Vague(1, 1.0, 45);
        v.setCoeffDifficulte(2.5);
        assertEquals(2.5, v.getCoeffDifficulte());
    }

    @Test
    void quandSetCoeffZero_alorsLeveIllegalArgument() {
        Vague v = new Vague(1, 1.0, 45);
        assertThrows(IllegalArgumentException.class, () -> v.setCoeffDifficulte(0));
    }

    @Test
    void quandSetDureeValide_alorsChange() {
        Vague v = new Vague(1, 1.0, 45);
        v.setDureeSecondes(60);
        assertEquals(60, v.getDureeSecondes());
    }

    @Test
    void quandSetDureeZero_alorsLeveIllegalArgument() {
        Vague v = new Vague(1, 1.0, 45);
        assertThrows(IllegalArgumentException.class, () -> v.setDureeSecondes(0));
    }

    @Test
    void quandSetDerniereVague_alorsChange() {
        Vague v = new Vague(1, 1.0, 45);
        assertFalse(v.isDerniereVague());
        v.setDerniereVague(true);
        assertTrue(v.isDerniereVague());
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(3, 2, 1);
        String str = v.toString();
        assertTrue(str.contains("Vague 1"));
        assertTrue(str.contains("escouades="));
        assertTrue(str.contains("ennemis=6"));
        assertTrue(str.contains("temps="));
    }

    @Test
    void quandDerniereVagueToString_alorsContientFinale() {
        Vague v = new Vague(5, 2.0, 45);
        v.setDerniereVague(true);
        String str = v.toString();
        assertTrue(str.contains("FINALE"));
    }
}