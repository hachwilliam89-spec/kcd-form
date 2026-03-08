package com.kcdformes.service;

import com.kcdformes.dto.MurailleRequestDTO;
import com.kcdformes.dto.MurailleResponseDTO;
import com.kcdformes.entity.MurailleEntity;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.repository.MurailleRepository;
import com.kcdformes.repository.PartieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MurailleService {

    private final MurailleRepository murailleRepository;
    private final PartieRepository partieRepository;

    public MurailleService(MurailleRepository murailleRepository, PartieRepository partieRepository) {
        this.murailleRepository = murailleRepository;
        this.partieRepository = partieRepository;
    }

    public MurailleResponseDTO placerMuraille(Long partieId, MurailleRequestDTO dto) {
        var partie = partieRepository.findById(partieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partie introuvable"));

        // Vérifier qu'il n'y a pas déjà une muraille à cette position
        boolean positionOccupee = murailleRepository.findByPartieId(partieId)
                .stream().anyMatch(m -> m.getPosition() == dto.getPosition());
        if (positionOccupee) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position déjà occupée par une muraille");
        }

        // Calculer les stats via le modèle métier
        Rectangle rectangle = new Rectangle("muraille", dto.getLargeur(), dto.getLongueur());
        int pvMax = rectangle.getPv();
        int cout = rectangle.cout();

        MurailleEntity entity = new MurailleEntity(
                dto.getPosition(), dto.getLargeur(), dto.getLongueur(),
                pvMax, cout, partie
        );

        return toDTO(murailleRepository.save(entity));
    }

    public List<MurailleResponseDTO> getMurailles(Long partieId) {
        return murailleRepository.findByPartieId(partieId)
                .stream().map(this::toDTO).toList();
    }

    public void supprimerMuraille(Long partieId, Long murailleId) {
        var muraille = murailleRepository.findById(murailleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Muraille introuvable"));
        if (!muraille.getPartie().getId().equals(partieId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette muraille n'appartient pas à cette partie");
        }
        murailleRepository.deleteById(murailleId);
    }

    private MurailleResponseDTO toDTO(MurailleEntity e) {
        return new MurailleResponseDTO(
                e.getId(), e.getPosition(), e.getPvMax(),
                e.getPvActuels(), e.getCout(), e.getPartie().getId()
        );
    }
}