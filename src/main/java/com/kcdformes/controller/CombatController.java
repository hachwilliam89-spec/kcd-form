package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.service.CombatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parties/{partieId}/combat")
@Tag(name = "Combat", description = "Gestion du combat en temps réel via WebSocket (STOMP/SockJS). Le back envoie l'état du combat chaque seconde sur /topic/combat/{partieId}. Ces endpoints démarrent ou reprennent le combat.")
public class CombatController {

    private final CombatService combatService;

    public CombatController(CombatService combatService) {
        this.combatService = combatService;
    }

    @PostMapping("/demarrer")
    @Operation(summary = "Démarrer le combat", description = "Lance le combat pour une partie. Charge les tourelles et murailles depuis la BDD, génère les vagues d'ennemis selon la difficulté, et démarre le timer. L'état est envoyé chaque seconde via WebSocket sur /topic/combat/{partieId}.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Combat démarré"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable")
    })
    public ResponseEntity<ApiResponseDTO<Void>> demarrer(@PathVariable Long partieId) {
        combatService.demarrerCombat(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Combat démarré pour la partie " + partieId + ".", null));
    }

    @PostMapping("/reprendre")
    @Operation(summary = "Reprendre le combat après une pause", description = "Reprend le combat après une phase de fortification (état ENTRE_VAGUES). Recharge les tourelles et murailles ajoutées pendant la pause, reporte les survivants à la vague suivante.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Combat repris"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable")
    })
    public ResponseEntity<ApiResponseDTO<Void>> reprendre(@PathVariable Long partieId) {
        combatService.demarrerCombat(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Combat repris pour la partie " + partieId + ".", null));
    }
}