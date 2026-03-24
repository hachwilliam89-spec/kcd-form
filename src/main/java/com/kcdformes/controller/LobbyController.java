package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.LobbyDTO;
import com.kcdformes.dto.VagueConfigDTO;
import com.kcdformes.service.CombatService;
import com.kcdformes.service.LobbyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lobby")
@Tag(name = "Lobby Multijoueur", description = "Gestion des lobbies pour le mode multijoueur asymétrique. Le défenseur crée le lobby, l'attaquant rejoint avec le code. L'état du lobby est broadcasté via WebSocket sur /topic/lobby/{lobbyId}.")
public class LobbyController {

    private final LobbyService lobbyService;
    private final CombatService combatService;

    public LobbyController(LobbyService lobbyService, CombatService combatService) {
        this.lobbyService = lobbyService;
        this.combatService = combatService;
    }

    @PostMapping("/creer")
    @Operation(summary = "Créer un lobby", description = "Le défenseur crée un lobby lié à sa partie. Retourne un code à 6 caractères à partager.")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> creer(
            @RequestParam Long partieId,
            @RequestParam(defaultValue = "5") int nbVagues) {
        String lobbyId = lobbyService.creerLobby(partieId, nbVagues);
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Lobby créé.",
                Map.of("lobbyId", lobbyId)
        ));
    }

    @PostMapping("/{lobbyId}/rejoindre")
    @Operation(summary = "Rejoindre un lobby", description = "Un joueur rejoint le lobby avec son rôle (DEFENSEUR ou ATTAQUANT).")
    public ResponseEntity<ApiResponseDTO<LobbyDTO>> rejoindre(
            @PathVariable String lobbyId,
            @RequestParam String role) {
        LobbyDTO dto = lobbyService.rejoindre(lobbyId, role.toUpperCase());
        return ResponseEntity.ok(ApiResponseDTO.ok("Rejoint en tant que " + role + ".", dto));
    }

    @PostMapping("/{lobbyId}/vagues")
    @Operation(summary = "Configurer les vagues", description = "L'attaquant envoie sa composition de vagues. Vérifie que le budget n'est pas dépassé.")
    public ResponseEntity<ApiResponseDTO<LobbyDTO>> configurerVagues(
            @PathVariable String lobbyId,
            @RequestBody List<VagueConfigDTO> vagues) {
        LobbyDTO dto = lobbyService.configurerVagues(lobbyId, vagues);
        return ResponseEntity.ok(ApiResponseDTO.ok("Vagues configurées.", dto));
    }

    @PostMapping("/{lobbyId}/pret")
    @Operation(summary = "Marquer prêt", description = "Un joueur se marque prêt. Quand les deux sont prêts, le combat se lance automatiquement.")
    public ResponseEntity<ApiResponseDTO<LobbyDTO>> pret(
            @PathVariable String lobbyId,
            @RequestParam String role) {
        LobbyDTO dto = lobbyService.marquerPret(lobbyId, role.toUpperCase());

        if (lobbyService.estPret(lobbyId)) {
            combatService.demarrerCombatMulti(lobbyId, lobbyService);
            dto = lobbyService.getEtat(lobbyId);
            dto.setEtat("EN_COURS");
        }

        return ResponseEntity.ok(ApiResponseDTO.ok("Prêt !", dto));
    }

    @GetMapping("/{lobbyId}")
    @Operation(summary = "État du lobby", description = "Retourne l'état actuel du lobby.")
    public ResponseEntity<ApiResponseDTO<LobbyDTO>> etat(@PathVariable String lobbyId) {
        LobbyDTO dto = lobbyService.getEtat(lobbyId);
        return ResponseEntity.ok(ApiResponseDTO.ok("État du lobby.", dto));
    }
}