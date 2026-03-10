package com.kcdformes.service;

import com.kcdformes.dto.FormeDTO;
import com.kcdformes.dto.FormeResponseDTO;
import com.kcdformes.entity.FormeEntity;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Forme;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
import com.kcdformes.repository.FormeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FormeService {

    private final FormeRepository formeRepository;

    public FormeService(FormeRepository formeRepository) {
        this.formeRepository = formeRepository;
    }

    public FormeResponseDTO creerForme(FormeDTO dto) {
        Forme forme = construireForme(dto);

        FormeEntity entity = new FormeEntity(
                dto.getType().toUpperCase(),
                dto.getCouleur(),
                dto.getCouleur(),
                dto.getValeur1(),
                dto.getValeur2(),
                forme.aire(),
                forme.perimetre(),
                forme.dps(),
                forme.cout()
        );

        return toDTO(formeRepository.save(entity));
    }

    public List<FormeResponseDTO> listerFormes() {
        return formeRepository.findAll().stream().map(this::toDTO).toList();
    }

    public FormeResponseDTO getForme(Long id) {
        return formeRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forme introuvable"));
    }

    public FormeResponseDTO modifierForme(Long id, FormeDTO dto) {
        var entity = formeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forme introuvable"));

        Forme forme = construireForme(dto);

        entity.setType(dto.getType().toUpperCase());
        entity.setNom(dto.getCouleur());
        entity.setCouleur(dto.getCouleur());
        entity.setValeur1(dto.getValeur1());
        entity.setValeur2(dto.getValeur2());
        entity.setAire(forme.aire());
        entity.setPerimetre(forme.perimetre());
        entity.setDps(forme.dps());
        entity.setCout(forme.cout());

        return toDTO(formeRepository.save(entity));
    }

    public void supprimerForme(Long id) {
        if (!formeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forme introuvable");
        }
        formeRepository.deleteById(id);
    }

    private Forme construireForme(FormeDTO dto) {
        return switch (dto.getType().toUpperCase()) {
            case "TRIANGLE" -> new Triangle(dto.getCouleur(), dto.getValeur1(), dto.getValeur2());
            case "CERCLE" -> new Cercle(dto.getCouleur(), dto.getValeur1());
            case "RECTANGLE" -> new Rectangle(dto.getCouleur(), dto.getValeur1(), dto.getValeur2());
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de forme inconnu : " + dto.getType());
        };
    }

    private FormeResponseDTO toDTO(FormeEntity e) {
        return new FormeResponseDTO(
                e.getId(), e.getType(), e.getNom(), e.getCouleur(),
                e.getValeur1(), e.getValeur2(), e.getAire(),
                e.getPerimetre(), e.getDps(), e.getCout()
        );
    }
}