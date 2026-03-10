package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.MurailleRequestDTO;
import com.kcdformes.dto.MurailleResponseDTO;
import com.kcdformes.service.MurailleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parties/{partieId}/murailles")
@Tag(name = "Murailles", description = "Gestion des murailles d'une partie. Les murailles sont des obstacles placés sur le chemin. Les ennemis doivent les détruire pour avancer. Le bélier inflige x2 dégâts aux murailles.")
public class MurailleController {

    private final MurailleService murailleService;

    public MurailleController(MurailleService murailleService) {
        this.murailleService = murailleService;
    }

    @PostMapping
    @Operation(summary = "Placer une muraille", description = "Place une muraille sur le chemin à une position donnée. Les PV et le coût sont calculés automatiquement depuis les dimensions (PV = aire x 10, coût = aire x 2.2). Une seule muraille par position.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Muraille placée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données manquantes"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable"),
            @ApiResponse(responseCode = "409", description = "Position déjà occupée par une muraille"),
            @ApiResponse(responseCode = "422", description = "Dimensions invalides (négatives ou zéro)")
    })
    public ResponseEntity<ApiResponseDTO<MurailleResponseDTO>> placerMuraille(
            @PathVariable Long partieId,
            @RequestBody MurailleRequestDTO dto) {
        MurailleResponseDTO muraille = murailleService.placerMuraille(partieId, dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Muraille placée en position " + muraille.getPosition() + ".", muraille));
    }

    @GetMapping
    @Operation(summary = "Lister les murailles d'une partie", description = "Retourne toutes les murailles placées dans une partie avec leurs PV et coût.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "ID de partie invalide")
    })
    public ResponseEntity<ApiResponseDTO<List<MurailleResponseDTO>>> getMurailles(@PathVariable Long partieId) {
        List<MurailleResponseDTO> murailles = murailleService.getMurailles(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Murailles de la partie " + partieId + " récupérées.", murailles));
    }

    @DeleteMapping("/{murailleId}")
    @Operation(summary = "Supprimer une muraille", description = "Supprime une muraille. Vérifie qu'elle appartient bien à la partie spécifiée.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Muraille supprimée"),
            @ApiResponse(responseCode = "400", description = "ID invalide"),
            @ApiResponse(responseCode = "404", description = "Muraille introuvable"),
            @ApiResponse(responseCode = "409", description = "La muraille n'appartient pas à cette partie")
    })
    public ResponseEntity<ApiResponseDTO<Void>> supprimerMuraille(
            @PathVariable Long partieId,
            @PathVariable Long murailleId) {
        murailleService.supprimerMuraille(partieId, murailleId);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Muraille supprimée avec succès."));
    }
}