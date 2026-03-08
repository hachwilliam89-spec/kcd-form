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
class TourelleServiceTest {

    @Autowired
    private TourelleService tourelleService;

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

    private TourelleRequestDTO creerTourelleDTO(String nom, int position, String typeForme) {
        TourelleRequestDTO dto = new TourelleRequestDTO();
        dto.setNom(nom);
        dto.setPosition(position);
        dto.setPortee(3);

        FormeDTO forme = new FormeDTO();
        forme.setType(typeForme);
        forme.setCouleur("rouge");
        forme.setValeur1(4.0);
        forme.setValeur2(3.0);
        dto.setFormes(List.of(forme));

        return dto;
    }

    // AJOUTER

    @Test
    void quandAjouterTourelle_alorsRetourneAvecId() {
        TourelleResponseDTO result = tourelleService.ajouterTourelle(partieId, creerTourelleDTO("Tour Nord", 1, "TRIANGLE"));
        assertNotNull(result.getId());
        assertEquals("Tour Nord", result.getNom());
        assertEquals(1, result.getPosition());
        assertTrue(result.getDps() > 0);
    }

    @Test
    void quandAjouterTourelleAvecCercle_alorsAoEActif() {
        TourelleResponseDTO result = tourelleService.ajouterTourelle(partieId, creerTourelleDTO("Catapulte", 2, "CERCLE"));
        assertTrue(result.isAoe());
    }

    @Test
    void quandAjouterTourelleSansFormes_alorsLeveBadRequest() {
        TourelleRequestDTO dto = new TourelleRequestDTO();
        dto.setNom("Vide");
        dto.setPosition(1);
        dto.setPortee(3);
        dto.setFormes(List.of());
        assertThrows(ResponseStatusException.class, () -> tourelleService.ajouterTourelle(partieId, dto));
    }

    @Test
    void quandAjouterTourelleAvec4Formes_alorsLeveBadRequest() {
        TourelleRequestDTO dto = new TourelleRequestDTO();
        dto.setNom("Trop");
        dto.setPosition(1);
        dto.setPortee(3);

        FormeDTO f = new FormeDTO();
        f.setType("TRIANGLE");
        f.setCouleur("rouge");
        f.setValeur1(4.0);
        f.setValeur2(3.0);
        dto.setFormes(List.of(f, f, f, f));

        assertThrows(ResponseStatusException.class, () -> tourelleService.ajouterTourelle(partieId, dto));
    }

    @Test
    void quandPartieInexistante_alorsLeveNotFound() {
        assertThrows(ResponseStatusException.class,
                () -> tourelleService.ajouterTourelle(99999L, creerTourelleDTO("Tour", 1, "TRIANGLE")));
    }

    // LISTER

    @Test
    void quandGetTourelles_alorsRetourneListe() {
        tourelleService.ajouterTourelle(partieId, creerTourelleDTO("Tour A", 1, "TRIANGLE"));
        tourelleService.ajouterTourelle(partieId, creerTourelleDTO("Tour B", 2, "CERCLE"));
        List<TourelleResponseDTO> tourelles = tourelleService.getTourelles(partieId);
        assertTrue(tourelles.size() >= 2);
    }

    // SUPPRIMER

    @Test
    void quandSupprimerTourelle_alorsNExistePlus() {
        TourelleResponseDTO creee = tourelleService.ajouterTourelle(partieId, creerTourelleDTO("A supprimer", 3, "TRIANGLE"));
        tourelleService.supprimerTourelle(partieId, creee.getId());
        // Vérifier qu'elle n'est plus dans la liste
        List<TourelleResponseDTO> tourelles = tourelleService.getTourelles(partieId);
        assertTrue(tourelles.stream().noneMatch(t -> t.getId().equals(creee.getId())));
    }

    @Test
    void quandSupprimerTourelleInexistante_alorsLeveNotFound() {
        assertThrows(ResponseStatusException.class,
                () -> tourelleService.supprimerTourelle(partieId, 99999L));
    }

    @Test
    void quandSupprimerTourelleMauvaisPartie_alorsLeveBadRequest() {
        TourelleResponseDTO creee = tourelleService.ajouterTourelle(partieId, creerTourelleDTO("Autre", 4, "TRIANGLE"));
        assertThrows(ResponseStatusException.class,
                () -> tourelleService.supprimerTourelle(99999L, creee.getId()));
    }
}