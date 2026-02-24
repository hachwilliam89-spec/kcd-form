package com.kcdformes.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForteresseTest {

    private Forteresse forteresse;

    @BeforeEach
    void setUp() {
        forteresse = new Forteresse("Citadelle", 960, 20, 25, 2);
    }

    // CONSTRUCTEUR

    @Test
    void quandConstructeurValide_alorsAttributsCorrects() {
        assertEquals("Citadelle", forteresse.getNom());
        assertEquals(960, forteresse.getPvMax());
        assertEquals(960, forteresse.getPvActuels());
        assertEquals(20, forteresse.getDefense());
        assertEquals(25, forteresse.getDps(), 0.001);
        assertEquals(2, forteresse.getPortee());
    }

    @Test
    void quandNomNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse(null, 960, 20, 25, 2));
    }

    @Test
    void quandNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse("", 960, 20, 25, 2));
    }

    @Test
    void quandNomBlanc_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse("   ", 960, 20, 25, 2));
    }

    @Test
    void quandPvMaxNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse("Test", -1, 20, 25, 2));
    }

    @Test
    void quandPvMaxZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse("Test", 0, 20, 25, 2));
    }

    @Test
    void quandDefenseNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse("Test", 960, -1, 25, 2));
    }

    @Test
    void quandDefenseZero_alorsAccepte() {
        Forteresse f = new Forteresse("Test", 960, 0, 25, 2);
        assertEquals(0, f.getDefense());
    }

    @Test
    void quandDpsNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse("Test", 960, 20, -1, 2));
    }

    @Test
    void quandDpsZero_alorsAccepte() {
        Forteresse f = new Forteresse("Test", 960, 20, 0, 2);
        assertEquals(0, f.getDps(), 0.001);
    }

    @Test
    void quandPorteeNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse("Test", 960, 20, 25, -1));
    }

    @Test
    void quandPorteeZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Forteresse("Test", 960, 20, 25, 0));
    }

    // SUBIR ATTAQUE - CAVALIER (Triangle = charge, dégâts PV max)

    @Test
    void quandCavalierAttaque_alorsDegatsBaseSurPvMax() {
        Ennemi cavalier = new Ennemi("Cavalier", new Triangle("forme", 4, 3));
        cavalier.subirDegats(100); // blessé
        int pvAvant = forteresse.getPvActuels();
        forteresse.subirAttaque(cavalier);
        // Dégâts = pvMax - defense = 150 - 20 = 130
        assertEquals(pvAvant - (cavalier.getPvMax() - 20), forteresse.getPvActuels());
    }

    @Test
    void quandCavalierBlesseAttaque_alorsMemesDegatsQuePleinVie() {
        Ennemi cavalierPlein = new Ennemi("Cavalier1", new Triangle("forme", 4, 3));
        Ennemi cavalierBlesse = new Ennemi("Cavalier2", new Triangle("forme", 4, 3));
        cavalierBlesse.subirDegats(100);

        Forteresse f1 = new Forteresse("F1", 960, 20, 25, 2);
        Forteresse f2 = new Forteresse("F2", 960, 20, 25, 2);

        f1.subirAttaque(cavalierPlein);
        f2.subirAttaque(cavalierBlesse);

        assertEquals(f1.getPvActuels(), f2.getPvActuels());
    }

    // SUBIR ATTAQUE - INFANTERIE (Cercle = dégâts PV restants)

    @Test
    void quandInfanterieAttaque_alorsDegatsBaseSurPvRestants() {
        Ennemi infanterie = new Ennemi("Infanterie", new Cercle("forme", 2));
        int pvAvant = forteresse.getPvActuels();
        forteresse.subirAttaque(infanterie);
        // Dégâts = pvActuels - defense
        assertEquals(pvAvant - (infanterie.getPvActuels() - 20), forteresse.getPvActuels());
    }

    @Test
    void quandInfanterieBlesseeAttaque_alorsMoinsDeDegats() {
        Ennemi infPleine = new Ennemi("Inf1", new Cercle("forme", 2));
        Ennemi infBleessee = new Ennemi("Inf2", new Cercle("forme", 2));
        infBleessee.subirDegats(200);

        Forteresse f1 = new Forteresse("F1", 960, 20, 25, 2);
        Forteresse f2 = new Forteresse("F2", 960, 20, 25, 2);

        f1.subirAttaque(infPleine);
        f2.subirAttaque(infBleessee);

        assertTrue(f2.getPvActuels() > f1.getPvActuels());
    }

    // SUBIR ATTAQUE - BELIER (Rectangle = machine, dégâts PV max)

    @Test
    void quandBelierAttaque_alorsDegatsBaseSurPvMax() {
        Ennemi belier = new Ennemi("Belier", new Rectangle("forme", 6, 3));
        int pvAvant = forteresse.getPvActuels();
        forteresse.subirAttaque(belier);
        assertEquals(pvAvant - (belier.getPvMax() - 20), forteresse.getPvActuels());
    }

    @Test
    void quandBelierBlesseAttaque_alorsMemesDegatsQuePleinVie() {
        Ennemi belierPlein = new Ennemi("Belier1", new Rectangle("forme", 6, 3));
        Ennemi belierBlesse = new Ennemi("Belier2", new Rectangle("forme", 6, 3));
        belierBlesse.subirDegats(200);

        Forteresse f1 = new Forteresse("F1", 960, 20, 25, 2);
        Forteresse f2 = new Forteresse("F2", 960, 20, 25, 2);

        f1.subirAttaque(belierPlein);
        f2.subirAttaque(belierBlesse);

        assertEquals(f1.getPvActuels(), f2.getPvActuels());
    }

    // SUBIR ATTAQUE - CAS LIMITES

    @Test
    void quandEnnemiNull_alorsRien() {
        forteresse.subirAttaque(null);
        assertEquals(960, forteresse.getPvActuels());
    }

    @Test
    void quandEnnemiMort_alorsRien() {
        Ennemi mort = new Ennemi("Mort", new Triangle("forme", 4, 3));
        mort.subirDegats(9999);
        forteresse.subirAttaque(mort);
        assertEquals(960, forteresse.getPvActuels());
    }

    @Test
    void quandDegatsSuperieursPV_alorsPVaZero() {
        Forteresse faible = new Forteresse("Faible", 10, 0, 5, 1);
        Ennemi belier = new Ennemi("Belier", new Rectangle("forme", 6, 3));
        faible.subirAttaque(belier);
        assertEquals(0, faible.getPvActuels());
    }

    @Test
    void quandDefenseSuperieurePvEnnemi_alorsDegatsMinimum1() {
        Forteresse blindee = new Forteresse("Blindee", 960, 9999, 25, 2);
        Ennemi faible = new Ennemi("Faible", new Triangle("forme", 1, 1));
        blindee.subirAttaque(faible);
        // Math.max(1, pvMax - 9999) = 1
        assertEquals(959, blindee.getPvActuels());
    }

    // TIRER SUR

    @Test
    void quandTirerSurEnnemiValide_alorsRetourneDps() {
        Ennemi e = new Ennemi("Test", new Triangle("forme", 4, 3));
        assertEquals(25, forteresse.tirerSur(e), 0.001);
    }

    @Test
    void quandTirerSurEnnemiNull_alorsRetourneZero() {
        assertEquals(0, forteresse.tirerSur(null), 0.001);
    }

    @Test
    void quandTirerSurEnnemiMort_alorsRetourneZero() {
        Ennemi mort = new Ennemi("Mort", new Triangle("forme", 4, 3));
        mort.subirDegats(9999);
        assertEquals(0, forteresse.tirerSur(mort), 0.001);
    }

    // EST EN PORTEE

    @Test
    void quandEnnemiEnPortee_alorsTrue() {
        // Chemin de 10, portée 2 → en portée à partir de pos 8
        assertTrue(forteresse.estEnPortee(8, 10));
        assertTrue(forteresse.estEnPortee(9, 10));
    }

    @Test
    void quandEnnemiHorsPortee_alorsFalse() {
        assertFalse(forteresse.estEnPortee(5, 10));
        assertFalse(forteresse.estEnPortee(7, 10));
    }

    @Test
    void quandEnnemiExactementALimite_alorsTrue() {
        // tailleChemin - portee = 10 - 2 = 8
        assertTrue(forteresse.estEnPortee(8, 10));
    }

    // EST DETRUITE

    @Test
    void quandPVPositifs_alorsNonDetruite() {
        assertFalse(forteresse.estDetruite());
    }

    @Test
    void quandPVaZero_alorsDetruite() {
        Forteresse faible = new Forteresse("Faible", 1, 0, 5, 1);
        Ennemi e = new Ennemi("Test", new Rectangle("forme", 6, 3));
        faible.subirAttaque(e);
        assertTrue(faible.estDetruite());
    }

    // POURCENTAGE VIE

    @Test
    void quandPleineVie_alors100Pourcent() {
        assertEquals(100.0, forteresse.getPourcentageVie(), 0.001);
    }

    @Test
    void quandDemiVie_alors50Pourcent() {
        // Infliger 480 dégâts (960 / 2)
        Forteresse f = new Forteresse("Test", 480, 0, 5, 1);
        Ennemi e = new Ennemi("Test", new Rectangle("forme", 6, 3));
        // Rectangle(6,3) → aire=18, pvMax=18*25=450
        f.subirAttaque(e);
        // dégâts = 450 - 0 = 450 → PV = 480 - 450 = 30
        double expected = (30.0 / 480.0) * 100;
        assertEquals(expected, f.getPourcentageVie(), 0.001);
    }

    // SETTERS

    @Test
    void quandSetNomValide_alorsNomChange() {
        forteresse.setNom("Bastion");
        assertEquals("Bastion", forteresse.getNom());
    }

    @Test
    void quandSetNomNull_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> forteresse.setNom(null));
    }

    @Test
    void quandSetNomVide_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> forteresse.setNom(""));
    }

    @Test
    void quandSetPvMaxValide_alorsPvMaxChange() {
        forteresse.setPvMax(1200);
        assertEquals(1200, forteresse.getPvMax());
    }

    @Test
    void quandSetPvMaxZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> forteresse.setPvMax(0));
    }

    @Test
    void quandSetDefenseValide_alorsDefenseChange() {
        forteresse.setDefense(50);
        assertEquals(50, forteresse.getDefense());
    }

    @Test
    void quandSetDefenseNegative_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> forteresse.setDefense(-1));
    }

    @Test
    void quandSetDpsValide_alorsDpsChange() {
        forteresse.setDps(30);
        assertEquals(30, forteresse.getDps(), 0.001);
    }

    @Test
    void quandSetDpsNegatif_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> forteresse.setDps(-1));
    }

    @Test
    void quandSetPorteeValide_alorsPorteeChange() {
        forteresse.setPortee(5);
        assertEquals(5, forteresse.getPortee());
    }

    @Test
    void quandSetPorteeZero_alorsLeveIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> forteresse.setPortee(0));
    }

    // TO STRING

    @Test
    void quandToString_alorsContientInfos() {
        String str = forteresse.toString();
        assertTrue(str.contains("Citadelle"));
        assertTrue(str.contains("960/960"));
        assertTrue(str.contains("defense=20"));
        assertTrue(str.contains("100%"));
    }
}