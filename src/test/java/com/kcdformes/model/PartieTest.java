package com.kcdformes.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PartieTest {

    private Partie creerPartieBasique(int difficulte) {
        Joueur j = new Joueur("Kim", 0, 5);
        Carte c = new Carte("Niveau 1", 10, 10);
        c.setChemin(List.of(0, 1, 2, 3, 4, 5));
        c.setEmplacementsTourelles(List.of(2, 5));
        c.setEmplacementsMurailles(List.of(3));
        return new Partie(difficulte, j, c);
    }

    // BUDGET PAR DIFFICULTE

    @Test
    void quandDifficulte1_alorsBudget500() {
        Partie p = creerPartieBasique(1);
        assertEquals(500, p.getJoueur().getBudget());
    }

    @Test
    void quandDifficulte2_alorsBudget400() {
        Partie p = creerPartieBasique(2);
        assertEquals(400, p.getJoueur().getBudget());
    }

    @Test
    void quandDifficulte3_alorsBudget300() {
        Partie p = creerPartieBasique(3);
        assertEquals(300, p.getJoueur().getBudget());
    }

    // NOMBRE D'ASSAUTS

    @Test
    void quandDifficulte1_alors5Assauts() {
        Partie p = creerPartieBasique(1);
        assertEquals(5, p.getNombreAssauts());
    }

    @Test
    void quandDifficulte2_alors8Assauts() {
        Partie p = creerPartieBasique(2);
        assertEquals(8, p.getNombreAssauts());
    }

    @Test
    void quandDifficulte3_alors12Assauts() {
        Partie p = creerPartieBasique(3);
        assertEquals(12, p.getNombreAssauts());
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeur_alorsEtatEnPause() {
        Partie p = creerPartieBasique(1);
        assertEquals(EtatPartie.EN_PAUSE, p.getEtat());
        assertEquals(0, p.getVagueActuelle());
    }

    @Test
    void quandDemarrer_alorsEtatEnCours() {
        Partie p = creerPartieBasique(1);
        p.demarrer();
        assertEquals(EtatPartie.EN_COURS, p.getEtat());
        assertEquals(1, p.getVagueActuelle());
    }

    // VAGUES

    @Test
    void quandAjouterVague_alorsListeAugmente() {
        Partie p = creerPartieBasique(1);
        p.ajouterVague(new Vague(1, 1.0, 1.0));
        assertEquals(1, p.getVagues().size());
    }

    @Test
    void quandAjouterVagueNull_alorsLeveIllegalArgument() {
        Partie p = creerPartieBasique(1);
        assertThrows(IllegalArgumentException.class, () -> p.ajouterVague(null));
    }

    @Test
    void quandGetVagues_alorsCopieDefensive() {
        Partie p = creerPartieBasique(1);
        p.ajouterVague(new Vague(1, 1.0, 1.0));
        p.getVagues().clear();
        assertEquals(1, p.getVagues().size());
    }

    // VALIDATION CONSTRUCTEUR

    @Test
    void quandDifficulteInvalide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> creerPartieBasique(5));
    }

    @Test
    void quandDifficulte0_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> creerPartieBasique(0));
    }

    @Test
    void quandJoueurNull_alorsLeveIllegalArgument() {
        Carte c = new Carte("Niveau 1", 10, 10);
        c.setChemin(List.of(0, 1, 2, 3, 4, 5));
        assertThrows(IllegalArgumentException.class, () -> new Partie(1, null, c));
    }

    @Test
    void quandCarteNull_alorsLeveIllegalArgument() {
        Joueur j = new Joueur("Kim", 0, 5);
        assertThrows(IllegalArgumentException.class, () -> new Partie(1, j, null));
    }

    // LANCER VAGUE SUIVANTE

    @Test
    void quandLancerVagueSuivante_alorsVagueIncrement() {
        Partie p = creerPartieBasique(1);
        p.ajouterVague(new Vague(1, 1.0, 1.0));
        p.ajouterVague(new Vague(2, 1.0, 1.0));
        p.demarrer();
        p.lancerVagueSuivante();
        assertEquals(2, p.getVagueActuelle());
    }

    @Test
    void quandLancerVagueSuivanteMax_alorsResteAuMax() {
        Partie p = creerPartieBasique(1);
        p.ajouterVague(new Vague(1, 1.0, 1.0));
        p.demarrer();
        p.lancerVagueSuivante();
        assertEquals(1, p.getVagueActuelle());
    }

    // UPDATE

    @Test
    void quandUpdateEnPause_alorsRienNeChange() {
        Partie p = creerPartieBasique(1);
        p.ajouterVague(new Vague(1, 1.0, 1.0));
        p.update();
        assertEquals(EtatPartie.EN_PAUSE, p.getEtat());
    }

    @Test
    void quandUpdateAvecEnnemi_alorsTourelleInfligeDegats() {
        Partie p = creerPartieBasique(1);
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e = new Ennemi("Goblin", new Triangle("forme", 2, 2));
        v.ajouterEnnemi(e);
        p.ajouterVague(v);

        Tourelle t = new Tourelle("Tour", 0);
        t.ajouterForme(new Triangle("Archer", 4, 3));
        p.getCarte().placerTourelle(t, 2);

        p.demarrer();
        int pvAvant = e.getPvActuels();
        p.update();
        assertTrue(e.getPvActuels() < pvAvant);
    }

    @Test
    void quandEnnemiMeurt_alorsScoreAugmente() {
        Partie p = creerPartieBasique(1);
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e = new Ennemi("Goblin", new Triangle("forme", 10, 10));
        e.setPosition(2);
        v.ajouterEnnemi(e);
        p.ajouterVague(v);

        Tourelle t = new Tourelle("Tour", 0);
        t.ajouterForme(new Triangle("Archer", 100, 100));
        p.getCarte().placerTourelle(t, 2);

        p.demarrer();
        int scoreAvant = p.getJoueur().getScore();
        p.update();
        assertTrue(p.getJoueur().getScore() > scoreAvant);
    }

    @Test
    void quandEnnemiAtteintFin_alorsForteresseSubitDegats() {
        Partie p = creerPartieBasique(1);
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e = new Ennemi("Belier", new Rectangle("forme", 6, 3));
        v.ajouterEnnemi(e);
        p.ajouterVague(v);

        // Placer l'ennemi juste avant la fin du chemin
        e.setPosition(5);

        p.demarrer();
        int pvForteresseAvant = p.getForteresse().getPvActuels();
        p.update();
        assertTrue(p.getForteresse().getPvActuels() < pvForteresseAvant);
    }

    @Test
    void quandUpdateEnnemiDejaMort_alorsIgnore() {
        Partie p = creerPartieBasique(1);
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi mort = new Ennemi("Goblin", new Triangle("forme", 2, 2));
        mort.subirDegats(9999);
        Ennemi vivant = new Ennemi("Orc", new Rectangle("forme", 10, 10));
        v.ajouterEnnemi(mort);
        v.ajouterEnnemi(vivant);
        p.ajouterVague(v);

        p.demarrer();
        p.update();
        assertEquals(EtatPartie.EN_COURS, p.getEtat());
    }

    @Test
    void quandUpdateEnnemiSurvit_alorsAvance() {
        Partie p = creerPartieBasique(1);
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e = new Ennemi("Goblin", new Rectangle("forme", 10, 10));
        v.ajouterEnnemi(e);
        p.ajouterVague(v);

        p.demarrer();
        int posAvant = e.getPosition();
        p.update();
        assertEquals(posAvant + 1, e.getPosition());
    }

    // FIN DE PARTIE

    @Test
    void quandForteresseDetruite_alorsPartiePerdue() {
        Partie p = creerPartieBasique(1);
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e = new Ennemi("Goblin", new Triangle("forme", 2, 2));
        v.ajouterEnnemi(e);
        p.ajouterVague(v);
        p.demarrer();

        // Simuler destruction de la forteresse
        Ennemi belier = new Ennemi("Belier", new Rectangle("forme", 6, 3));
        while (!p.getForteresse().estDetruite()) {
            p.getForteresse().subirAttaque(belier);
        }

        p.verifierFinPartie();
        assertEquals(EtatPartie.PERDU, p.getEtat());
    }

    @Test
    void quandTousEnnemisMortsEtDerniereVague_alorsPartieGagnee() {
        Partie p = creerPartieBasique(1);
        Vague v = new Vague(1, 1.0, 1.0);
        Ennemi e = new Ennemi("Goblin", new Triangle("forme", 2, 2));
        v.ajouterEnnemi(e);
        p.ajouterVague(v);
        p.demarrer();

        e.subirDegats(9999);
        v.spawnSuivant();

        p.verifierFinPartie();
        assertEquals(EtatPartie.GAGNE, p.getEtat());
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        Partie p = creerPartieBasique(1);
        String str = p.toString();
        assertTrue(str.contains("difficulte=1"));
        assertTrue(str.contains("Kim"));
    }
}