package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.PartieRequestDTO;
import com.kcdformes.dto.PartieResponseDTO;
import com.kcdformes.service.PartieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kcdformes.model.gameplay.EtatPartie;

import java.util.List;

@RestController
@RequestMapping("/api/parties")
public class PartieController {

    private final PartieService partieService;

    public PartieController(PartieService partieService) {
        this.partieService = partieService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<PartieResponseDTO>> creerPartie(@RequestBody PartieRequestDTO dto) {
        PartieResponseDTO partie = partieService.creerPartie(dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Partie créée en difficulté " + partie.getDifficulte() + ".", partie));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PartieResponseDTO>>> listerParties() {
        List<PartieResponseDTO> parties = partieService.listerParties();
        return ResponseEntity.ok(ApiResponseDTO.ok("Liste des parties récupérée.", parties));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PartieResponseDTO>> getPartie(@PathVariable Long id) {
        PartieResponseDTO partie = partieService.getPartie(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Partie avec l'id " + id + " introuvable."));
        return ResponseEntity.ok(ApiResponseDTO.ok("Partie récupérée.", partie));
    }

    @PutMapping("/{id}/etat")
    public ResponseEntity<ApiResponseDTO<PartieResponseDTO>> changerEtat(
            @PathVariable Long id,
            @RequestParam EtatPartie etat) {
        PartieResponseDTO partie = partieService.changerEtat(id, etat);
        return ResponseEntity.ok(ApiResponseDTO.ok("État de la partie changé en " + etat + ".", partie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> supprimerPartie(@PathVariable Long id) {
        partieService.supprimerPartie(id);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Partie supprimée avec succès."));
    }
}