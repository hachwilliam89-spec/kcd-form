package com.kcdformes.model.gameplay;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
import com.kcdformes.model.joueur.Joueur;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PartieTest {

    private Partie creerPartieBasique(Difficulte difficulte) {
        Joueur j = new Joueur("Kim", 0, 5);
        Carte c = new Carte("Niveau 1", 10, 10);
        c.setChemin(List.of(0, 1, 2, 3, 4, 5));
        c.setEmplacementsTourelles(List.of(2, 5));
        c.setEmplacementsMurailles(List.of(3));
        return new Partie(difficulte, j, c);
    }

    // BUDGET PAR DIFFICULTE

    @Test
    void quandEcuyer_alorsBudget1000() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        assertEquals(1000, p.getJoueur().getBudget());
    }

    @Test
    void quandChevalier_alorsBudget700() {
        Partie p = creerPartieBasique(Difficulte.CHEVALIER);
        assertEquals(700, p.getJoueur().getBudget());
    }

    @Test
    void quandSeigneur_alorsBudget400() {
        Partie p = creerPartieBasique(Difficulte.SEIGNEUR);
        assertEquals(400, p.getJoueur().getBudget());
    }

    // NOMBRE D'ASSAUTS

    @Test
    void quandEcuyer_alorsNombreAssautsCorrect() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        assertEquals(Difficulte.ECUYER.getNombreVagues(), p.getNombreAssauts());
    }

    @Test
    void quandChevalier_alorsNombreAssautsCorrect() {
        Partie p = creerPartieBasique(Difficulte.CHEVALIER);
        assertEquals(Difficulte.CHEVALIER.getNombreVagues(), p.getNombreAssauts());
    }

    @Test
    void quandSeigneur_alorsNombreAssautsCorrect() {
        Partie p = creerPartieBasique(Difficulte.SEIGNEUR);
        assertEquals(Difficulte.SEIGNEUR.getNombreVagues(), p.getNombreAssauts());
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeur_alorsEtatEnPause() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        assertEquals(EtatPartie.EN_PAUSE, p.getEtat());
        assertEquals(0, p.getVagueActuelle());
    }

    @Test
    void quandDemarrer_alorsEtatEnCours() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        p.demarrer();
        assertEquals(EtatPartie.EN_COURS, p.getEtat());
    }

    // VAGUES

    @Test
    void quandAjouterVague_alorsListeAugmente() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        p.ajouterVague(new Vague(1, 1.0, 45));
        assertEquals(1, p.getVagues().size());
    }

    @Test
    void quandAjouterVagueNull_alorsLeveIllegalArgument() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        assertThrows(IllegalArgumentException.class, () -> p.ajouterVague(null));
    }

    @Test
    void quandGetVagues_alorsCopieDefensive() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        p.ajouterVague(new Vague(1, 1.0, 45));
        p.getVagues().clear();
        assertEquals(1, p.getVagues().size());
    }

    // VALIDATION CONSTRUCTEUR

    @Test
    void quandDifficulteNull_alorsLeveIllegalArgument() {
        Joueur j = new Joueur("Kim", 0, 5);
        Carte c = new Carte("Niveau 1", 10, 10);
        c.setChemin(List.of(0, 1, 2, 3, 4, 5));
        assertThrows(IllegalArgumentException.class, () -> new Partie(null, j, c));
    }

    @Test
    void quandJoueurNull_alorsLeveIllegalArgument() {
        Carte c = new Carte("Niveau 1", 10, 10);
        c.setChemin(List.of(0, 1, 2, 3, 4, 5));
        assertThrows(IllegalArgumentException.class, () -> new Partie(Difficulte.ECUYER, null, c));
    }

    @Test
    void quandCarteNull_alorsLeveIllegalArgument() {
        Joueur j = new Joueur("Kim", 0, 5);
        assertThrows(IllegalArgumentException.class, () -> new Partie(Difficulte.ECUYER, j, null));
    }

    // LANCER VAGUE SUIVANTE

    @Test
    void quandLancerVagueSuivante_alorsVagueIncrement() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v1 = new Vague(1, 1.0, 45);
        v1.genererEscouades(1, 0, 0);
        Vague v2 = new Vague(2, 1.0, 45);
        v2.genererEscouades(1, 0, 0);
        p.ajouterVague(v1);
        p.ajouterVague(v2);
        p.demarrer();
        p.lancerVagueSuivante();
        assertEquals(1, p.getVagueActuelle());
    }

    // TIMER

    @Test
    void quandTimerExpire_alorsVagueTerminee() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v = new Vague(1, 1.0, 3);
        v.genererEscouades(1, 0, 0);
        p.ajouterVague(v);
        p.demarrer();

        p.update();
        p.update();
        p.update();
        assertTrue(v.estTerminee());
    }

    @Test
    void quandVagueTerminee_alorsSurvivantsPassentALaSuivante() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v1 = new Vague(1, 1.0, 1);
        v1.genererEscouades(0, 1, 0);
        Vague v2 = new Vague(2, 1.0, 45);
        v2.genererEscouades(1, 0, 0);
        p.ajouterVague(v1);
        p.ajouterVague(v2);
        p.demarrer();

        p.update();
        int ennemisV2Avant = v2.getNombreEnnemis();
        p.lancerVagueSuivante();

        assertTrue(v2.getNombreEnnemis() >= ennemisV2Avant);
    }

    // DERNIERE VAGUE

    @Test
    void quandDerniereVague_alorsTermineeQuandTousMorts() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v = new Vague(1, 1.0, 45);
        v.setDerniereVague(true);
        v.genererEscouades(1, 0, 0);
        p.ajouterVague(v);

        Ennemi e = v.spawnSuivant();
        assertNotNull(e);
        assertFalse(v.estTerminee());

        e.subirDegats(9999);
        assertTrue(v.estTerminee());
    }

    // UPDATE

    @Test
    void quandUpdateEnPause_alorsRienNeChange() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(1, 0, 0);
        p.ajouterVague(v);
        p.update();
        assertEquals(EtatPartie.EN_PAUSE, p.getEtat());
    }

    @Test
    void quandEnnemiAtteintFin_alorsForteresseSubitDegats() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(0, 0, 1);
        p.ajouterVague(v);

        Ennemi belier = v.spawnSuivant();
        if (belier == null) {
            for (int i = 0; i < 10 && belier == null; i++) {
                belier = v.spawnSuivant();
            }
        }
        assertNotNull(belier);
        belier.setPosition(5);

        p.demarrer();
        int pvForteresseAvant = p.getForteresse().getPvActuels();
        p.update();
        assertTrue(p.getForteresse().getPvActuels() < pvForteresseAvant);
    }

    // FIN DE PARTIE

    @Test
    void quandForteresseDetruite_alorsPartiePerdue() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v = new Vague(1, 1.0, 45);
        v.genererEscouades(1, 0, 0);
        p.ajouterVague(v);
        p.demarrer();

        Ennemi belier = new Ennemi("Belier", new Rectangle("forme", 8, 5), 1.0);
        while (!p.getForteresse().estDetruite()) {
            p.getForteresse().subirAttaque(belier);
        }

        p.verifierFinPartie();
        assertEquals(EtatPartie.PERDU, p.getEtat());
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        String str = p.toString();
        assertTrue(str.contains("ECUYER"));
        assertTrue(str.contains("Kim"));
    }

    // ===== TESTS MURAILLE =====

    @Test
    void quandAjouterMuraille_alorsMuraillePresente() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        p.ajouterMuraille(5, 5.0, 3.0, 150);
        assertFalse(p.getMurailles().isEmpty());
        assertEquals(150, p.getMurailles().get(5).getPvMax());
    }

    @Test
    void quandEnnemiBloqueParMuraille_alorsNePassePas() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v = new Vague(1, 1.0, 60);
        Ennemi cavalier = new Ennemi("Cavalier", new Triangle("forme", 4, 3));
        v.genererEscouades(0, 0, 0);
        v.ajouterEnnemis(java.util.List.of(cavalier));
        p.ajouterVague(v);
        p.demarrer();
        p.ajouterMuraille(5, 5.0, 3.0, 150);
        for (int i = 0; i < 3; i++) {
            p.update();
        }
        assertTrue(cavalier.getPosition() < 5);
    }

    @Test
    void quandBelierAttaqueMuraille_alorsDegatDoubles() {
        Partie.MurailleEnJeu m = new Partie.MurailleEnJeu(5, 5.0, 3.0, 150);
        Ennemi belier = new Ennemi("Bélier", new Rectangle("forme", 10, 6));
        double degatsBase = belier.getDegatsRempart();
        double degatsDoubles = degatsBase * 2;
        m.subirDegats(degatsDoubles);
        assertTrue(m.getPvActuels() < 150);
        assertEquals(150 - (int) degatsDoubles, m.getPvActuels());
    }

    // ===== TESTS ENTRE VAGUES =====

    @Test
    void quandEstDerniereVague_alorsRetourneCorrectement() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v1 = new Vague(1, 1.0, 30);
        Vague v2 = new Vague(2, 1.0, 30);
        v2.setDerniereVague(true);
        v1.genererEscouades(1, 0, 0);
        v2.genererEscouades(1, 0, 0);
        p.ajouterVague(v1);
        p.ajouterVague(v2);
        assertFalse(p.estDerniereVague());
    }

    @Test
    void quandReprendre_alorsEtatEnCours() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v1 = new Vague(1, 1.0, 1);
        Vague v2 = new Vague(2, 1.0, 30);
        v2.setDerniereVague(true);
        v1.genererEscouades(1, 0, 0);
        v2.genererEscouades(1, 0, 0);
        p.ajouterVague(v1);
        p.ajouterVague(v2);
        p.demarrer();
        for (int i = 0; i < 60; i++) {
            p.update();
            if (p.getEtat() == EtatPartie.ENTRE_VAGUES) break;
        }
        if (p.getEtat() == EtatPartie.ENTRE_VAGUES) {
            p.reprendre();
            assertEquals(EtatPartie.EN_COURS, p.getEtat());
            assertEquals(1, p.getVagueActuelle());
        }
    }

    // ===== TEST ENNEMI A LA FORTERESSE =====

    @Test
    void quandEnnemiAtteintForteresse_alorsAttaqueChaqueTick() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        Vague v = new Vague(1, 1.0, 120);
        v.setDerniereVague(true);
        Ennemi cavalier = new Ennemi("Cavalier", new Triangle("forme", 4, 3));
        v.genererEscouades(0, 0, 0);
        v.ajouterEnnemis(java.util.List.of(cavalier));
        p.ajouterVague(v);
        p.demarrer();
        int pvAvant = p.getForteresse().getPvActuels();
        for (int i = 0; i < 30; i++) {
            p.update();
        }
        assertTrue(p.getForteresse().getPvActuels() < pvAvant);
    }
}