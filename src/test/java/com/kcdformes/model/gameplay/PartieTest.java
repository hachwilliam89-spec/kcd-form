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
    void quandEcuyer_alorsBudget500() {
        Partie p = creerPartieBasique(Difficulte.ECUYER);
        assertEquals(500, p.getJoueur().getBudget());
    }

    @Test
    void quandChevalier_alorsBudget400() {
        Partie p = creerPartieBasique(Difficulte.CHEVALIER);
        assertEquals(400, p.getJoueur().getBudget());
    }

    @Test
    void quandSeigneur_alorsBudget300() {
        Partie p = creerPartieBasique(Difficulte.SEIGNEUR);
        assertEquals(300, p.getJoueur().getBudget());
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
        // Vague 1 très courte (1s) avec un tank costaud
        Vague v1 = new Vague(1, 1.0, 1);
        v1.genererEscouades(0, 1, 0); // 1 infanterie costaud
        Vague v2 = new Vague(2, 1.0, 45);
        v2.genererEscouades(1, 0, 0);
        p.ajouterVague(v1);
        p.ajouterVague(v2);
        p.demarrer();

        p.update(); // timer expire à 1s
        int ennemisV2Avant = v2.getNombreEnnemis();
        p.lancerVagueSuivante();

        // Les survivants de v1 sont ajoutés à v2
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

        // Spawn l'ennemi
        Ennemi e = v.spawnSuivant();
        assertNotNull(e);
        assertFalse(v.estTerminee());

        // Le tuer
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
        v.genererEscouades(0, 0, 1); // 1 bélier
        p.ajouterVague(v);

        // Spawn le bélier et le placer en fin de chemin
        Ennemi belier = v.spawnSuivant();
        if (belier == null) {
            // Si délai avant spawn, on tick jusqu'au spawn
            for (int i = 0; i < 10 && belier == null; i++) {
                belier = v.spawnSuivant();
            }
        }
        assertNotNull(belier);
        belier.setPosition(5); // fin du chemin

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

        // Détruire la forteresse manuellement
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
}