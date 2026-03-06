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
        var partie = partieRepository.findById(partieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partie introuvable"));

        if (dto.getFormes() == null || dto.getFormes().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une tourelle doit avoir au moins une forme");
        }
        if (dto.getFormes().size() > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une tourelle ne peut pas avoir plus de 3 formes");
        }

        // Construction via le modèle métier
        Tourelle tourelle = new Tourelle(dto.getNom(), dto.getPosition());
        tourelle.setPortee(dto.getPortee() > 0 ? dto.getPortee() : 3);

        for (FormeDTO f : dto.getFormes()) {
            switch (f.getType().toUpperCase()) {
                case "TRIANGLE" -> tourelle.ajouterForme(new Triangle(f.getCouleur(), f.getValeur1(), f.getValeur2()));
                case "CERCLE"   -> tourelle.ajouterForme(new Cercle(f.getCouleur(), f.getValeur1()));
                case "RECTANGLE"-> tourelle.ajouterForme(new Rectangle(f.getCouleur(), f.getValeur1(), f.getValeur2()));
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de forme inconnu : " + f.getType());
            }
        }

        TourelleEntity entity = new TourelleEntity(
                dto.getNom(), dto.getPosition(), tourelle.getPortee(),
                tourelle.getNombreTirs(), tourelle.hasAoE(), tourelle.getRayonZone(),
                tourelle.dpsTotal(), (int) tourelle.getPV(), tourelle.coutTotal(),
                partie
        );

        return toDTO(tourelleRepository.save(entity));
    }

    public List<TourelleResponseDTO> getTourelles(Long partieId) {
        return tourelleRepository.findByPartieId(partieId)
                .stream().map(this::toDTO).toList();
    }

    public void supprimerTourelle(Long partieId, Long tourelleId) {
        var tourelle = tourelleRepository.findById(tourelleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tourelle introuvable"));
        if (!tourelle.getPartie().getId().equals(partieId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette tourelle n'appartient pas à cette partie");
        }
        tourelleRepository.deleteById(tourelleId);
    }

    private TourelleResponseDTO toDTO(TourelleEntity e) {
        return new TourelleResponseDTO(
                e.getId(), e.getNom(), e.getPosition(), e.getPortee(),
                e.getNombreTirs(), e.isAoe(), e.getRayonAoe(),
                e.getDps(), e.getPv(), e.getCout(), e.getPartie().getId()
        );
    }
}