package com.kcdformes.service;

import com.kcdformes.dto.*;
import com.kcdformes.model.gameplay.Difficulte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MurailleServiceTest {

    @Autowired
    private MurailleService murailleService;

    @Autowired
    private PartieService partieService;

    @Autowired
    private JoueurService joueurService;

    private Long partieId;

    @BeforeEach
    void setUp() {
        JoueurRequestDTO joueurDTO = new JoueurRequestDTO();
        joueurDTO.setNom("TestJoueur");
        joueurDTO.setBudget(1000);
        joueurDTO.setVies(3);
        JoueurResponseDTO joueur = joueurService.creerJoueur(joueurDTO);

        PartieRequestDTO partieDTO = new PartieRequestDTO();
        partieDTO.setJoueurId(joueur.getId());
        partieDTO.setDifficulte(Difficulte.ECUYER);
        PartieResponseDTO partie = partieService.creerPartie(partieDTO);
        partieId = partie.getId();
    }

    private MurailleRequestDTO creerMurailleDTO(int position) {
        MurailleRequestDTO dto = new MurailleRequestDTO();
        dto.setPosition(position);
        dto.setLargeur(5.0);
        dto.setLongueur(3.0);
        return dto;
    }

    // PLACER

    @Test
    void quandPlacerMuraille_alorsRetourneAvecId() {
        MurailleResponseDTO result = murailleService.placerMuraille(partieId, creerMurailleDTO(3));
        assertNotNull(result.getId());
        assertEquals(3, result.getPosition());
        assertTrue(result.getPvMax() > 0);
        assertTrue(result.getCout() > 0);
    }

    @Test
    void quandPlacerMuraille_alorsPvCalculesDepuisRectangle() {
        MurailleResponseDTO result = murailleService.placerMuraille(partieId, creerMurailleDTO(5));
        // PV = aire * 10 = (5.0 * 3.0) * 10 = 150
        assertEquals(150, result.getPvMax());
        assertEquals(150, result.getPvActuels());
    }

    @Test
    void quandPlacerMuraille_alorsCoutCalculeDepuisRectangle() {
        MurailleResponseDTO result = murailleService.placerMuraille(partieId, creerMurailleDTO(5));
        // Cout = aire * 2.2 = (5.0 * 3.0) * 2.2 = 33
        assertEquals(33, result.getCout());
    }

    @Test
    void quandPlacerMuraillePositionOccupee_alorsLeveBadRequest() {
        murailleService.placerMuraille(partieId, creerMurailleDTO(3));
        assertThrows(ResponseStatusException.class,
                () -> murailleService.placerMuraille(partieId, creerMurailleDTO(3)));
    }

    @Test
    void quandPartieInexistante_alorsLeveNotFound() {
        assertThrows(ResponseStatusException.class,
                () -> murailleService.placerMuraille(99999L, creerMurailleDTO(3)));
    }

    // LISTER

    @Test
    void quandGetMurailles_alorsRetourneListe() {
        murailleService.placerMuraille(partieId, creerMurailleDTO(3));
        murailleService.placerMuraille(partieId, creerMurailleDTO(6));
        List<MurailleResponseDTO> murailles = murailleService.getMurailles(partieId);
        assertTrue(murailles.size() >= 2);
    }

    @Test
    void quandAucuneMuraille_alorsListeVide() {
        List<MurailleResponseDTO> murailles = murailleService.getMurailles(partieId);
        assertTrue(murailles.isEmpty());
    }

    // SUPPRIMER

    @Test
    void quandSupprimerMuraille_alorsNExistePlus() {
        MurailleResponseDTO creee = murailleService.placerMuraille(partieId, creerMurailleDTO(3));
        murailleService.supprimerMuraille(partieId, creee.getId());
        List<MurailleResponseDTO> murailles = murailleService.getMurailles(partieId);
        assertTrue(murailles.stream().noneMatch(m -> m.getId().equals(creee.getId())));
    }

    @Test
    void quandSupprimerMurailleInexistante_alorsLeveNotFound() {
        assertThrows(ResponseStatusException.class,
                () -> murailleService.supprimerMuraille(partieId, 99999L));
    }

    @Test
    void quandSupprimerMurailleMauvaisePartie_alorsLeveBadRequest() {
        MurailleResponseDTO creee = murailleService.placerMuraille(partieId, creerMurailleDTO(3));
        assertThrows(ResponseStatusException.class,
                () -> murailleService.supprimerMuraille(99999L, creee.getId()));
    }
}