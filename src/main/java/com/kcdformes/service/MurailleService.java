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
        if (partieId == null || partieId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id de la partie doit être un nombre positif.");
        }
        var partie = partieRepository.findById(partieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Partie avec l'id " + partieId + " introuvable."));

        validerMurailleDTO(dto);

        boolean positionOccupee = murailleRepository.findByPartieId(partieId)
                .stream().anyMatch(m -> m.getPosition() == dto.getPosition());
        if (positionOccupee) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Position " + dto.getPosition() + " déjà occupée par une muraille.");
        }

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
        if (partieId == null || partieId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id de la partie doit être un nombre positif.");
        }
        return murailleRepository.findByPartieId(partieId)
                .stream().map(this::toDTO).toList();
    }

    public void supprimerMuraille(Long partieId, Long murailleId) {
        if (partieId == null || partieId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id de la partie doit être un nombre positif.");
        }
        if (murailleId == null || murailleId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id de la muraille doit être un nombre positif.");
        }
        var muraille = murailleRepository.findById(murailleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Muraille avec l'id " + murailleId + " introuvable."));
        if (!muraille.getPartie().getId().equals(partieId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La muraille " + murailleId + " n'appartient pas à la partie " + partieId + ".");
        }
        murailleRepository.deleteById(murailleId);
    }

    private void validerMurailleDTO(MurailleRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la muraille sont requises.");
        }
        if (dto.getPosition() < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "La position doit être positive. Reçu : " + dto.getPosition());
        }
        if (dto.getLargeur() <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "La largeur doit être strictement positive. Reçu : " + dto.getLargeur());
        }
        if (dto.getLongueur() <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "La longueur doit être strictement positive. Reçu : " + dto.getLongueur());
        }
    }

    private MurailleResponseDTO toDTO(MurailleEntity e) {
        return new MurailleResponseDTO(
                e.getId(), e.getPosition(), e.getPvMax(),
                e.getPvActuels(), e.getCout(), e.getPartie().getId()
        );
    }
}