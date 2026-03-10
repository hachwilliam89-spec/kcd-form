package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.PartieRequestDTO;
import com.kcdformes.dto.PartieResponseDTO;
import com.kcdformes.service.PartieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kcdformes.model.gameplay.EtatPartie;

import java.util.List;

@RestController
@RequestMapping("/api/parties")
@Tag(name = "Parties", description = "Gestion des parties. Une partie lie un joueur à une difficulté (ECUYER, CHEVALIER, SEIGNEUR) et suit son état (EN_PAUSE, EN_COURS, ENTRE_VAGUES, GAGNE, PERDU).")
public class PartieController {

    private final PartieService partieService;

    public PartieController(PartieService partieService) {
        this.partieService = partieService;
    }

    @PostMapping
    @Operation(summary = "Créer une partie", description = "Crée une nouvelle partie pour un joueur existant. La difficulté détermine le budget, les PV de la forteresse et le nombre de vagues.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Partie créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Joueur ID manquant ou difficulté manquante"),
            @ApiResponse(responseCode = "404", description = "Joueur introuvable")
    })
    public ResponseEntity<ApiResponseDTO<PartieResponseDTO>> creerPartie(@RequestBody PartieRequestDTO dto) {
        PartieResponseDTO partie = partieService.creerPartie(dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Partie créée en difficulté " + partie.getDifficulte() + ".", partie));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les parties", description = "Retourne toutes les parties avec leur état, score et statistiques de combat.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public ResponseEntity<ApiResponseDTO<List<PartieResponseDTO>>> listerParties() {
        List<PartieResponseDTO> parties = partieService.listerParties();
        return ResponseEntity.ok(ApiResponseDTO.ok("Liste des parties récupérée.", parties));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une partie par ID", description = "Retourne une partie avec son état, score final, étoiles et statistiques de combat.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partie trouvée"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable")
    })
    public ResponseEntity<ApiResponseDTO<PartieResponseDTO>> getPartie(@PathVariable Long id) {
        PartieResponseDTO partie = partieService.getPartie(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Partie avec l'id " + id + " introuvable."));
        return ResponseEntity.ok(ApiResponseDTO.ok("Partie récupérée.", partie));
    }

    @PutMapping("/{id}/etat")
    @Operation(summary = "Changer l'état d'une partie", description = "Change l'état de la partie. Impossible si la partie est déjà terminée (GAGNE ou PERDU). États valides : EN_PAUSE, EN_COURS, ENTRE_VAGUES.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "État changé avec succès"),
            @ApiResponse(responseCode = "400", description = "État manquant ou invalide"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable"),
            @ApiResponse(responseCode = "409", description = "Partie déjà terminée, changement impossible")
    })
    public ResponseEntity<ApiResponseDTO<PartieResponseDTO>> changerEtat(
            @PathVariable Long id,
            @RequestParam EtatPartie etat) {
        PartieResponseDTO partie = partieService.changerEtat(id, etat);
        return ResponseEntity.ok(ApiResponseDTO.ok("État de la partie changé en " + etat + ".", partie));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une partie", description = "Supprime définitivement une partie et toutes ses données associées.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partie supprimée"),
            @ApiResponse(responseCode = "400", description = "ID invalide"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable")
    })
    public ResponseEntity<ApiResponseDTO<Void>> supprimerPartie(@PathVariable Long id) {
        partieService.supprimerPartie(id);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Partie supprimée avec succès."));
    }
}