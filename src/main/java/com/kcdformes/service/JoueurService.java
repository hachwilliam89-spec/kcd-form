package com.kcdformes.service;

import com.kcdformes.dto.JoueurRequestDTO;
import com.kcdformes.dto.JoueurResponseDTO;
import com.kcdformes.entity.JoueurEntity;
import com.kcdformes.repository.JoueurRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class JoueurService {

    private final JoueurRepository joueurRepository;

    public JoueurService(JoueurRepository joueurRepository) {
        this.joueurRepository = joueurRepository;
    }

    public JoueurResponseDTO creerJoueur(JoueurRequestDTO dto) {
        validerJoueurDTO(dto);
        JoueurEntity joueur = new JoueurEntity(dto.getNom().trim(), dto.getBudget(), dto.getVies());
        return toDTO(joueurRepository.save(joueur));
    }

    public List<JoueurResponseDTO> listerJoueurs() {
        return joueurRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<JoueurResponseDTO> getJoueur(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id doit être un nombre positif.");
        }
        return joueurRepository.findById(id).map(this::toDTO);
    }

    public JoueurResponseDTO modifierJoueur(Long id, JoueurRequestDTO dto) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id doit être un nombre positif.");
        }
        var joueur = joueurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Joueur avec l'id " + id + " introuvable."));

        validerJoueurDTO(dto);
        joueur.setNom(dto.getNom().trim());
        joueur.setBudget(dto.getBudget());
        joueur.setVies(dto.getVies());
        return toDTO(joueurRepository.save(joueur));
    }

    public void supprimerJoueur(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id doit être un nombre positif.");
        }
        if (!joueurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Joueur avec l'id " + id + " introuvable.");
        }
        joueurRepository.deleteById(id);
    }

    private void validerJoueurDTO(JoueurRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données du joueur sont requises.");
        }
        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nom du joueur est requis.");
        }
        if (dto.getNom().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le nom doit contenir au moins 2 caractères.");
        }
        if (dto.getBudget() < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le budget ne peut pas être négatif. Reçu : " + dto.getBudget());
        }
        if (dto.getBudget() > 10000) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le budget ne peut pas dépasser 10000. Reçu : " + dto.getBudget());
        }
        if (dto.getVies() <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le nombre de vies doit être positif. Reçu : " + dto.getVies());
        }
        if (dto.getVies() > 10) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le nombre de vies ne peut pas dépasser 10. Reçu : " + dto.getVies());
        }
    }

    private JoueurResponseDTO toDTO(JoueurEntity e) {
        return new JoueurResponseDTO(e.getId(), e.getNom(), e.getBudget(), e.getScore(), e.getVies());
    }
}