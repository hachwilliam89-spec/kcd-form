package com.kcdformes.service;

import com.kcdformes.dto.PartieRequestDTO;
import com.kcdformes.dto.PartieResponseDTO;
import com.kcdformes.entity.PartieEntity;
import com.kcdformes.repository.JoueurRepository;
import com.kcdformes.repository.PartieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.kcdformes.model.gameplay.EtatPartie;

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
        validerPartieDTO(dto);
        var joueur = joueurRepository.findById(dto.getJoueurId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Joueur avec l'id " + dto.getJoueurId() + " introuvable."));
        PartieEntity partie = new PartieEntity(dto.getDifficulte(), joueur);
        return toDTO(partieRepository.save(partie));
    }

    public List<PartieResponseDTO> listerParties() {
        return partieRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<PartieResponseDTO> getPartie(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id doit être un nombre positif.");
        }
        return partieRepository.findById(id).map(this::toDTO);
    }

    public PartieResponseDTO changerEtat(Long id, EtatPartie nouvelEtat) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id doit être un nombre positif.");
        }
        if (nouvelEtat == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'état est requis (EN_COURS, EN_PAUSE, ENTRE_VAGUES, GAGNE, PERDU).");
        }
        var partie = partieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Partie avec l'id " + id + " introuvable."));

        // Vérifier les transitions d'état valides
        EtatPartie etatActuel = partie.getEtat();
        if (etatActuel == EtatPartie.GAGNE || etatActuel == EtatPartie.PERDU) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La partie est terminée (" + etatActuel + "). Impossible de changer l'état.");
        }

        partie.setEtat(nouvelEtat);
        return toDTO(partieRepository.save(partie));
    }

    public void supprimerPartie(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id doit être un nombre positif.");
        }
        if (!partieRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Partie avec l'id " + id + " introuvable.");
        }
        partieRepository.deleteById(id);
    }

    private void validerPartieDTO(PartieRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la partie sont requises.");
        }
        if (dto.getJoueurId() == null || dto.getJoueurId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id du joueur est requis et doit être positif.");
        }
        if (dto.getDifficulte() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La difficulté est requise (ECUYER, CHEVALIER, SEIGNEUR).");
        }
    }

    private PartieResponseDTO toDTO(PartieEntity e) {
        return new PartieResponseDTO(
                e.getId(), e.getDifficulte(), e.getEtat(),
                e.getVagueActuelle(), e.getJoueur().getId(), e.getJoueur().getNom(),
                e.getScoreFinal(), e.getEtoiles(),
                e.getEnnemisElimines(), e.getEnnemisTotal(),
                e.getForteressePvRestants(), e.getForteressePvMax(),
                e.getOrDepense()
        );
    }
}