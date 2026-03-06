package com.kcdformes.service;

import com.kcdformes.dto.PartieRequestDTO;
import com.kcdformes.dto.PartieResponseDTO;
import com.kcdformes.entity.PartieEntity;
import com.kcdformes.repository.JoueurRepository;
import com.kcdformes.repository.PartieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PartieService {

    private final PartieRepository partieRepository;
    private final JoueurRepository joueurRepository;

    public PartieService(PartieRepository partieRepository, JoueurRepository joueurRepository) {
        this.partieRepository = partieRepository;
        this.joueurRepository = joueurRepository;
    }

    public PartieResponseDTO creerPartie(PartieRequestDTO dto) {
        var joueur = joueurRepository.findById(dto.getJoueurId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Joueur introuvable"));
        PartieEntity partie = new PartieEntity(dto.getDifficulte(), joueur);
        return toDTO(partieRepository.save(partie));
    }

    public List<PartieResponseDTO> listerParties() {
        return partieRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<PartieResponseDTO> getPartie(Long id) {
        return partieRepository.findById(id).map(this::toDTO);
    }

    private PartieResponseDTO toDTO(PartieEntity e) {
        return new PartieResponseDTO(
                e.getId(), e.getDifficulte(), e.getEtat(),
                e.getVagueActuelle(), e.getJoueur().getId(), e.getJoueur().getNom()
        );
    }
}