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
@Tag(name = "Lobby Multijoueur", description = "Gestion des lobbies pour le mode multijoueur asymétrique.")
public class LobbyController {

    private final LobbyService lobbyService;
    private final CombatService combatService;

    public LobbyController(LobbyService lobbyService, CombatService combatService) {
        this.lobbyService = lobbyService;
        this.combatService = combatService;
    }

    @PostMapping("/creer")
    @Operation(summary = "Créer un lobby")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> creer(
            @RequestParam Long partieId,
            @RequestParam(defaultValue = "5") int nbVagues) {
        String lobbyId = lobbyService.creerLobby(partieId, nbVagues);
        return ResponseEntity.ok(ApiResponseDTO.ok("Lobby créé.", Map.of("lobbyId", lobbyId)));
    }

    @PostMapping("/{lobbyId}/rejoindre")
    @Operation(summary = "Rejoindre un lobby")
    public ResponseEntity<ApiResponseDTO<LobbyDTO>> rejoindre(
            @PathVariable String lobbyId,
            @RequestParam String role) {
        LobbyDTO dto = lobbyService.rejoindre(lobbyId, role.toUpperCase());
        return ResponseEntity.ok(ApiResponseDTO.ok("Rejoint en tant que " + role + ".", dto));
    }

    @PostMapping("/{lobbyId}/vagues")
    @Operation(summary = "Configurer les vagues")
    public ResponseEntity<ApiResponseDTO<LobbyDTO>> configurerVagues(
            @PathVariable String lobbyId,
            @RequestBody List<VagueConfigDTO> vagues) {
        LobbyDTO dto = lobbyService.configurerVagues(lobbyId, vagues);
        return ResponseEntity.ok(ApiResponseDTO.ok("Vagues configurées.", dto));
    }

    @PostMapping("/{lobbyId}/pret")
    @Operation(summary = "Marquer prêt", description = "Quand les deux sont prêts, le combat se lance automatiquement.")
    public ResponseEntity<ApiResponseDTO<LobbyDTO>> pret(
            @PathVariable String lobbyId,
            @RequestParam String role) {
        LobbyDTO dto = lobbyService.marquerPret(lobbyId, role.toUpperCase());

        if (lobbyService.estPret(lobbyId)) {
            combatService.demarrerCombatMulti(lobbyId, lobbyService);
            lobbyService.marquerEnCours(lobbyId);
            dto = lobbyService.getEtat(lobbyId);
        }

        return ResponseEntity.ok(ApiResponseDTO.ok("Prêt !", dto));
    }

    @GetMapping("/{lobbyId}")
    @Operation(summary = "État du lobby")
    public ResponseEntity<ApiResponseDTO<LobbyDTO>> etat(@PathVariable String lobbyId) {
        LobbyDTO dto = lobbyService.getEtat(lobbyId);
        return ResponseEntity.ok(ApiResponseDTO.ok("État du lobby.", dto));
    }
}