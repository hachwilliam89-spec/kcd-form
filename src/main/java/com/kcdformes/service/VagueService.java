package com.kcdformes.service;

import com.kcdformes.dto.VagueResponseDTO;
import com.kcdformes.model.gameplay.EtatPartie;
import com.kcdformes.repository.PartieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VagueService {

    private static final int VAGUE_MAX = 5;
    private final PartieRepository partieRepository;

    public VagueService(PartieRepository partieRepository) {
        this.partieRepository = partieRepository;
    }

    public VagueResponseDTO vaguesuivante(Long partieId) {
        var partie = partieRepository.findById(partieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partie introuvable"));

        if (partie.getEtat() == EtatPartie.GAGNE || partie.getEtat() == EtatPartie.PERDU) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La partie est terminée");
        }

        int vagueActuelle = partie.getVagueActuelle() + 1;

        if (vagueActuelle > VAGUE_MAX) {
            partie.setEtat(EtatPartie.GAGNE);
            partieRepository.save(partie);
            return new VagueResponseDTO(partieId, VAGUE_MAX, VAGUE_MAX, EtatPartie.GAGNE, "Félicitations, toutes les vagues sont terminées !");
        }

        partie.setVagueActuelle(vagueActuelle);
        partie.setEtat(EtatPartie.EN_COURS);
        partieRepository.save(partie);

        String message = vagueActuelle == VAGUE_MAX
                ? "Vague finale ! Tenez bon !"
                : "Vague " + vagueActuelle + "/" + VAGUE_MAX + " en cours";

        return new VagueResponseDTO(partieId, vagueActuelle, VAGUE_MAX, EtatPartie.EN_COURS, message);
    }

    public VagueResponseDTO getVagueActuelle(Long partieId) {
        var partie = partieRepository.findById(partieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partie introuvable"));

        int vagueActuelle = partie.getVagueActuelle();
        String message = vagueActuelle == 0
                ? "Partie non démarrée"
                : vagueActuelle == VAGUE_MAX
                ? "Vague finale en cours"
                : "Vague " + vagueActuelle + "/" + VAGUE_MAX;

        return new VagueResponseDTO(partieId, vagueActuelle, VAGUE_MAX, partie.getEtat(), message);
    }

}