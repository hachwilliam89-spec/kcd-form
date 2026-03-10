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

    // PLACER — SUCCES

    @Test
    void quandPlacerMuraille_alorsRetourneAvecId() {
        MurailleResponseDTO result = murailleService.placerMuraille(partieId, creerMurailleDTO(3));
        assertNotNull(result.getId());
        assertEquals(3, result.getPosition());
        assertTrue(result.getPvMax() > 0);
        assertTrue(result.getCout() > 0);
    }

    @Test
    void quandPlacerMuraille_alorsPvCalcules() {
        MurailleResponseDTO result = murailleService.placerMuraille(partieId, creerMurailleDTO(5));
        assertEquals(150, result.getPvMax());
        assertEquals(150, result.getPvActuels());
    }

    @Test
    void quandPlacerMuraille_alorsCoutCalcule() {
        MurailleResponseDTO result = murailleService.placerMuraille(partieId, creerMurailleDTO(5));
        assertEquals(33, result.getCout());
    }

    // PLACER — ERREURS VALIDATION

    @Test
    void quandLargeurNegative_alorsLeveUnprocessable() {
        MurailleRequestDTO dto = new MurailleRequestDTO();
        dto.setPosition(3);
        dto.setLargeur(-5.0);
        dto.setLongueur(3.0);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> murailleService.placerMuraille(partieId, dto));
        assertEquals(422, ex.getStatusCode().value());
    }

    @Test
    void quandLongueurZero_alorsLeveUnprocessable() {
        MurailleRequestDTO dto = new MurailleRequestDTO();
        dto.setPosition(3);
        dto.setLargeur(5.0);
        dto.setLongueur(0);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> murailleService.placerMuraille(partieId, dto));
        assertEquals(422, ex.getStatusCode().value());
    }

    @Test
    void quandPositionOccupee_alorsLeveConflict() {
        murailleService.placerMuraille(partieId, creerMurailleDTO(3));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> murailleService.placerMuraille(partieId, creerMurailleDTO(3)));
        assertEquals(409, ex.getStatusCode().value());
    }

    @Test
    void quandPartieInexistante_alorsLeveNotFound() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> murailleService.placerMuraille(99999L, creerMurailleDTO(3)));
        assertEquals(404, ex.getStatusCode().value());
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

    // SUPPRIMER — SUCCES

    @Test
    void quandSupprimerMuraille_alorsNExistePlus() {
        MurailleResponseDTO creee = murailleService.placerMuraille(partieId, creerMurailleDTO(3));
        murailleService.supprimerMuraille(partieId, creee.getId());
        List<MurailleResponseDTO> murailles = murailleService.getMurailles(partieId);
        assertTrue(murailles.stream().noneMatch(m -> m.getId().equals(creee.getId())));
    }

    // SUPPRIMER — ERREURS

    @Test
    void quandSupprimerMurailleInexistante_alorsLeveNotFound() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> murailleService.supprimerMuraille(partieId, 99999L));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void quandSupprimerMurailleMauvaisePartie_alorsLeveConflict() {
        MurailleResponseDTO creee = murailleService.placerMuraille(partieId, creerMurailleDTO(3));

        JoueurRequestDTO j2 = new JoueurRequestDTO();
        j2.setNom("Autre");
        j2.setBudget(500);
        j2.setVies(3);
        JoueurResponseDTO joueur2 = joueurService.creerJoueur(j2);
        PartieRequestDTO p2 = new PartieRequestDTO();
        p2.setJoueurId(joueur2.getId());
        p2.setDifficulte(Difficulte.ECUYER);
        PartieResponseDTO autrePartie = partieService.creerPartie(p2);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> murailleService.supprimerMuraille(autrePartie.getId(), creee.getId()));
        assertEquals(409, ex.getStatusCode().value());
    }
}