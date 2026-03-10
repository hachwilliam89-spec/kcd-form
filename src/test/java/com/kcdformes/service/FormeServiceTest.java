package com.kcdformes.service;

import com.kcdformes.dto.FormeDTO;
import com.kcdformes.dto.FormeResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FormeServiceTest {

    @Autowired
    private FormeService formeService;

    private FormeDTO creerFormeDTO(String type, double v1, double v2) {
        FormeDTO dto = new FormeDTO();
        dto.setType(type);
        dto.setValeur1(v1);
        dto.setValeur2(v2);
        dto.setCouleur("rouge");
        return dto;
    }

    // CREER

    @Test
    void quandCreerTriangle_alorsRetourneAvecId() {
        FormeResponseDTO result = formeService.creerForme(creerFormeDTO("TRIANGLE", 4.0, 3.0));
        assertNotNull(result.getId());
        assertEquals("TRIANGLE", result.getType());
        assertTrue(result.getAire() > 0);
        assertTrue(result.getDps() > 0);
        assertTrue(result.getCout() > 0);
    }

    @Test
    void quandCreerCercle_alorsAireCorrect() {
        FormeResponseDTO result = formeService.creerForme(creerFormeDTO("CERCLE", 3.0, 0));
        assertNotNull(result.getId());
        assertEquals("CERCLE", result.getType());
        assertTrue(result.getAire() > 0);
    }

    @Test
    void quandCreerRectangle_alorsDpsZero() {
        FormeResponseDTO result = formeService.creerForme(creerFormeDTO("RECTANGLE", 5.0, 3.0));
        assertEquals("RECTANGLE", result.getType());
        assertEquals(0, result.getDps());
    }

    @Test
    void quandTypeInconnu_alorsLeveBadRequest() {
        assertThrows(ResponseStatusException.class,
                () -> formeService.creerForme(creerFormeDTO("LOSANGE", 3.0, 3.0)));
    }

    // LISTER

    @Test
    void quandListerFormes_alorsRetourneListe() {
        formeService.creerForme(creerFormeDTO("TRIANGLE", 4.0, 3.0));
        List<FormeResponseDTO> formes = formeService.listerFormes();
        assertFalse(formes.isEmpty());
    }

    // GET PAR ID

    @Test
    void quandGetFormeExistante_alorsRetourneForme() {
        FormeResponseDTO creee = formeService.creerForme(creerFormeDTO("CERCLE", 5.0, 0));
        FormeResponseDTO trouvee = formeService.getForme(creee.getId());
        assertEquals(creee.getId(), trouvee.getId());
        assertEquals("CERCLE", trouvee.getType());
    }

    @Test
    void quandGetFormeInexistante_alorsLeveNotFound() {
        assertThrows(ResponseStatusException.class, () -> formeService.getForme(99999L));
    }

    // MODIFIER

    @Test
    void quandModifierForme_alorsStatsRecalculees() {
        FormeResponseDTO creee = formeService.creerForme(creerFormeDTO("TRIANGLE", 4.0, 3.0));
        double ancienneAire = creee.getAire();

        FormeResponseDTO modifiee = formeService.modifierForme(creee.getId(), creerFormeDTO("TRIANGLE", 8.0, 6.0));
        assertTrue(modifiee.getAire() > ancienneAire);
    }

    @Test
    void quandModifierFormeInexistante_alorsLeveNotFound() {
        assertThrows(ResponseStatusException.class,
                () -> formeService.modifierForme(99999L, creerFormeDTO("TRIANGLE", 4.0, 3.0)));
    }

    // SUPPRIMER

    @Test
    void quandSupprimerForme_alorsNExistePlus() {
        FormeResponseDTO creee = formeService.creerForme(creerFormeDTO("RECTANGLE", 5.0, 3.0));
        formeService.supprimerForme(creee.getId());
        assertThrows(ResponseStatusException.class, () -> formeService.getForme(creee.getId()));
    }

    @Test
    void quandSupprimerFormeInexistante_alorsLeveNotFound() {
        assertThrows(ResponseStatusException.class, () -> formeService.supprimerForme(99999L));
    }
}