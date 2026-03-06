package com.kcdformes.service;

import com.kcdformes.dto.JoueurRequestDTO;
import com.kcdformes.dto.JoueurResponseDTO;
import com.kcdformes.entity.JoueurEntity;
import com.kcdformes.repository.JoueurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JoueurService {

    private final JoueurRepository joueurRepository;

    public JoueurService(JoueurRepository joueurRepository) {
        this.joueurRepository = joueurRepository;
    }

    public JoueurResponseDTO creerJoueur(JoueurRequestDTO dto) {
        JoueurEntity joueur = new JoueurEntity(dto.getNom(), dto.getBudget(), dto.getVies());
        JoueurEntity sauvegarde = joueurRepository.save(joueur);
        return toDTO(sauvegarde);
    }

    public List<JoueurResponseDTO> listerJoueurs() {
        return joueurRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<JoueurResponseDTO> getJoueur(Long id) {
        return joueurRepository.findById(id).map(this::toDTO);
    }

    private JoueurResponseDTO toDTO(JoueurEntity e) {
        return new JoueurResponseDTO(e.getId(), e.getNom(), e.getBudget(), e.getScore(), e.getVies());
    }
}