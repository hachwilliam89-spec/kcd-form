package com.kcdformes.model.gameplay;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EscouadeTest {

    private Escouade escouade;

    @BeforeEach
    void setUp() {
        escouade = new Escouade(3, 1);
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeur_alorsValeursCorrectes() {
        assertEquals(3, escouade.getDelaiAvantSpawn());
        assertEquals(1, escouade.getDelaiEntreEnnemis());
        assertEquals(0, escouade.getNombreEnnemis());
        assertFalse(escouade.estCommencee());
        assertTrue(escouade.estTerminee()); // escouade vide → terminée (0 >= 0)
    }

    @Test
    void quandDelaiAvantSpawnZero_alorsAccepte() {
        Escouade e = new Escouade(0, 1);
        assertEquals(0, e.getDelaiAvantSpawn());
    }

    @Test
    void quandDelaiAvantSpawnNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Escouade(-1, 1));
    }

    @Test
    void quandDelaiEntreEnnemisNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Escouade(3, -1));
    }

    // AJOUT ENNEMIS

    @Test
    void quandAjouterEnnemi_alorsNombreAugmente() {
        escouade.ajouterEnnemi(new Ennemi("Cavalier", new Triangle("forme", 4, 3)));
        assertEquals(1, escouade.getNombreEnnemis());
    }

    @Test
    void quandAjouterEnnemiNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> escouade.ajouterEnnemi(null));
    }

    @Test
    void quandGetEnnemis_alorsCopieDefensive() {
        escouade.ajouterEnnemi(new Ennemi("Cavalier", new Triangle("forme", 4, 3)));
        escouade.getEnnemis().clear();
        assertEquals(1, escouade.getNombreEnnemis());
    }

    // SPAWN AVEC DELAI AVANT

    @Test
    void quandDelaiAvant3_alorsNullPendant3Ticks() {
        escouade.ajouterEnnemi(new Ennemi("Cavalier", new Triangle("forme", 4, 3)));

        assertNull(escouade.spawnSuivant());  // tick 1 → attend
        assertNull(escouade.spawnSuivant());  // tick 2 → attend
        assertNotNull(escouade.spawnSuivant()); // tick 3 → spawn !
    }

    @Test
    void quandDelaiAvantZero_alorsSpawnImmediateAuPremierTick() {
        Escouade e = new Escouade(0, 1);
        Ennemi cavalier = new Ennemi("Cavalier", new Triangle("forme", 4, 3));
        e.ajouterEnnemi(cavalier);

        assertEquals(cavalier, e.spawnSuivant()); // immédiat
    }

    @Test
    void quandSpawnPremier_alorsEstCommencee() {
        escouade.ajouterEnnemi(new Ennemi("Cavalier", new Triangle("forme", 4, 3)));

        assertFalse(escouade.estCommencee());
        escouade.spawnSuivant(); // tick 1
        escouade.spawnSuivant(); // tick 2
        escouade.spawnSuivant(); // tick 3 → spawn

        assertTrue(escouade.estCommencee());
    }

    // SPAWN AVEC DELAI ENTRE ENNEMIS

    @Test
    void quandDelaiEntreEnnemis1_alorsSpawnTousLes1Ticks() {
        Escouade e = new Escouade(0, 1);
        Ennemi e1 = new Ennemi("Cavalier 1", new Triangle("forme", 4, 3));
        Ennemi e2 = new Ennemi("Cavalier 2", new Triangle("forme", 4, 3));
        e.ajouterEnnemi(e1);
        e.ajouterEnnemi(e2);

        assertEquals(e1, e.spawnSuivant()); // immédiat
        assertEquals(e2, e.spawnSuivant()); // 1 tick après
    }

    @Test
    void quandDelaiEntreEnnemis2_alorsNullEntreLesSpawns() {
        Escouade e = new Escouade(0, 2);
        Ennemi e1 = new Ennemi("Cavalier 1", new Triangle("forme", 4, 3));
        Ennemi e2 = new Ennemi("Cavalier 2", new Triangle("forme", 4, 3));
        e.ajouterEnnemi(e1);
        e.ajouterEnnemi(e2);

        assertEquals(e1, e.spawnSuivant()); // immédiat
        assertNull(e.spawnSuivant());       // tick 1 → attend
        assertEquals(e2, e.spawnSuivant()); // tick 2 → spawn
    }

    // ESCOUADE TERMINÉE

    @Test
    void quandTousSpawnes_alorsEstTerminee() {
        Escouade e = new Escouade(0, 1);
        e.ajouterEnnemi(new Ennemi("Cavalier", new Triangle("forme", 4, 3)));

        assertFalse(e.estTerminee());
        e.spawnSuivant();
        assertTrue(e.estTerminee());
    }

    @Test
    void quandTerminee_alorsSpawnRetourneNull() {
        Escouade e = new Escouade(0, 1);
        e.ajouterEnnemi(new Ennemi("Cavalier", new Triangle("forme", 4, 3)));
        e.spawnSuivant();

        assertNull(e.spawnSuivant());
        assertNull(e.spawnSuivant());
    }

    @Test
    void quandEscouadeVide_alorsTermineeImmediatement() {
        Escouade e = new Escouade(0, 1);
        assertTrue(e.estTerminee());
    }

    // SCENARIO COMPLET

    @Test
    void scenarioComplet_escouade3Cavaliers() {
        Escouade e = new Escouade(2, 1);
        Ennemi c1 = new Ennemi("Cavalier 1", new Triangle("forme", 4, 3));
        Ennemi c2 = new Ennemi("Cavalier 2", new Triangle("forme", 4, 3));
        Ennemi c3 = new Ennemi("Cavalier 3", new Triangle("forme", 4, 3));
        e.ajouterEnnemi(c1);
        e.ajouterEnnemi(c2);
        e.ajouterEnnemi(c3);

        // Phase d'attente (delaiAvant = 2)
        assertNull(e.spawnSuivant());     // tick 1 → attend
        assertEquals(c1, e.spawnSuivant()); // tick 2 → premier spawn

        // Phase de spawn (delaiEntre = 1)
        assertEquals(c2, e.spawnSuivant()); // tick 3
        assertEquals(c3, e.spawnSuivant()); // tick 4

        // Terminée
        assertTrue(e.estTerminee());
        assertNull(e.spawnSuivant());
    }

    // SETTERS

    @Test
    void quandSetDelaiAvantSpawnValide_alorsChange() {
        escouade.setDelaiAvantSpawn(5);
        assertEquals(5, escouade.getDelaiAvantSpawn());
    }

    @Test
    void quandSetDelaiAvantSpawnNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> escouade.setDelaiAvantSpawn(-1));
    }

    @Test
    void quandSetDelaiEntreEnnemisValide_alorsChange() {
        escouade.setDelaiEntreEnnemis(3);
        assertEquals(3, escouade.getDelaiEntreEnnemis());
    }

    @Test
    void quandSetDelaiEntreEnnemisNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> escouade.setDelaiEntreEnnemis(-1));
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        escouade.ajouterEnnemi(new Ennemi("Cavalier", new Triangle("forme", 4, 3)));
        String str = escouade.toString();
        assertTrue(str.contains("1 ennemis"));
        assertTrue(str.contains("delaiAvant=3"));
        assertTrue(str.contains("delaiEntre=1"));
    }
}