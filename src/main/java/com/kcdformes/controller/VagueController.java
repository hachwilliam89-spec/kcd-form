package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.VagueResponseDTO;
import com.kcdformes.service.VagueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parties/{partieId}/vague")
@Tag(name = "Vagues", description = "Gestion des vagues d'ennemis via l'API REST. Le nombre de vagues dépend de la difficulté. Utilisé pour l'avancement manuel des vagues (le combat temps réel passe par le CombatController + WebSocket).")
public class VagueController {

    private final VagueService vagueService;

    public VagueController(VagueService vagueService) {
        this.vagueService = vagueService;
    }

    @PostMapping("/suivante")
    @Operation(summary = "Lancer la vague suivante", description = "Incrémente le numéro de vague et passe la partie en état EN_COURS. Si toutes les vagues sont terminées, la partie passe en état GAGNE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vague suivante lancée"),
            @ApiResponse(responseCode = "400", description = "Partie déjà terminée"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable")
    })
    public ResponseEntity<ApiResponseDTO<VagueResponseDTO>> vagueSuivante(@PathVariable Long partieId) {
        VagueResponseDTO vague = vagueService.vaguesuivante(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Vague " + vague.getVagueActuelle() + " lancée.", vague));
    }

    @GetMapping
    @Operation(summary = "Récupérer la vague actuelle", description = "Retourne le numéro de vague actuel, le nombre total de vagues et l'état de la partie.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vague actuelle récupérée"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable")
    })
    public ResponseEntity<ApiResponseDTO<VagueResponseDTO>> getVagueActuelle(@PathVariable Long partieId) {
        VagueResponseDTO vague = vagueService.getVagueActuelle(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Vague actuelle récupérée.", vague));
    }
}