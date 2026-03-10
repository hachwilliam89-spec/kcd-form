package com.kcdformes.service;

import com.kcdformes.dto.TourelleRequestDTO;
import com.kcdformes.dto.TourelleResponseDTO;
import com.kcdformes.entity.TourelleEntity;
import com.kcdformes.model.defense.Tourelle;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
import com.kcdformes.repository.PartieRepository;
import com.kcdformes.repository.TourelleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.kcdformes.dto.FormeDTO;

import java.util.List;

@Service
public class TourelleService {

    private final TourelleRepository tourelleRepository;
    private final PartieRepository partieRepository;

    public TourelleService(TourelleRepository tourelleRepository, PartieRepository partieRepository) {
        this.tourelleRepository = tourelleRepository;
        this.partieRepository = partieRepository;
    }

    public TourelleResponseDTO ajouterTourelle(Long partieId, TourelleRequestDTO dto) {
        if (partieId == null || partieId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id de la partie doit être un nombre positif.");
        }
        var partie = partieRepository.findById(partieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Partie avec l'id " + partieId + " introuvable."));

        validerTourelleDTO(dto);

        Tourelle tourelle = new Tourelle(dto.getNom().trim(), dto.getPosition());
        tourelle.setPortee(dto.getPortee() > 0 ? dto.getPortee() : 3);

        for (FormeDTO f : dto.getFormes()) {
            validerFormeDTO(f);
            switch (f.getType().toUpperCase()) {
                case "TRIANGLE" -> tourelle.ajouterForme(new Triangle(f.getCouleur(), f.getValeur1(), f.getValeur2()));
                case "CERCLE" -> tourelle.ajouterForme(new Cercle(f.getCouleur(), f.getValeur1()));
                case "RECTANGLE" -> tourelle.ajouterForme(new Rectangle(f.getCouleur(), f.getValeur1(), f.getValeur2()));
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Type de forme inconnu : '" + f.getType() + "'. Types valides : TRIANGLE, CERCLE, RECTANGLE.");
            }
        }

        TourelleEntity entity = new TourelleEntity(
                dto.getNom().trim(), dto.getPosition(), tourelle.getPortee(),
                tourelle.getNombreTirs(), tourelle.hasAoE(), tourelle.getRayonZone(),
                tourelle.dpsTotal(), (int) tourelle.getPV(), tourelle.coutTotal(),
                partie
        );

        return toDTO(tourelleRepository.save(entity));
    }

    public List<TourelleResponseDTO> getTourelles(Long partieId) {
        if (partieId == null || partieId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id de la partie doit être un nombre positif.");
        }
        return tourelleRepository.findByPartieId(partieId)
                .stream().map(this::toDTO).toList();
    }

    public void supprimerTourelle(Long partieId, Long tourelleId) {
        if (partieId == null || partieId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id de la partie doit être un nombre positif.");
        }
        if (tourelleId == null || tourelleId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id de la tourelle doit être un nombre positif.");
        }
        var tourelle = tourelleRepository.findById(tourelleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tourelle avec l'id " + tourelleId + " introuvable."));
        if (!tourelle.getPartie().getId().equals(partieId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La tourelle " + tourelleId + " n'appartient pas à la partie " + partieId + ".");
        }
        tourelleRepository.deleteById(tourelleId);
    }

    private void validerTourelleDTO(TourelleRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la tourelle sont requises.");
        }
        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nom de la tourelle est requis.");
        }
        if (dto.getNom().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le nom doit contenir au moins 2 caractères.");
        }
        if (dto.getPosition() < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "La position doit être positive. Reçu : " + dto.getPosition());
        }
        if (dto.getFormes() == null || dto.getFormes().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Une tourelle doit avoir au moins une forme.");
        }
        if (dto.getFormes().size() > 3) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Une tourelle ne peut pas avoir plus de 3 formes. Reçu : " + dto.getFormes().size());
        }
    }

    private void validerFormeDTO(FormeDTO dto) {
        if (dto.getType() == null || dto.getType().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le type de forme est requis (TRIANGLE, CERCLE, RECTANGLE).");
        }
        if (dto.getCouleur() == null || dto.getCouleur().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La couleur de la forme est requise.");
        }
        if (dto.getValeur1() <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "La dimension valeur1 doit être strictement positive. Reçu : " + dto.getValeur1());
        }
        String type = dto.getType().toUpperCase();
        if ((type.equals("TRIANGLE") || type.equals("RECTANGLE")) && dto.getValeur2() <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "La dimension valeur2 doit être strictement positive pour un " + type + ". Reçu : " + dto.getValeur2());
        }
    }

    private TourelleResponseDTO toDTO(TourelleEntity e) {
        return new TourelleResponseDTO(
                e.getId(), e.getNom(), e.getPosition(), e.getPortee(),
                e.getNombreTirs(), e.isAoe(), e.getRayonAoe(),
                e.getDps(), e.getPv(), e.getCout(), e.getPartie().getId()
        );
    }
}