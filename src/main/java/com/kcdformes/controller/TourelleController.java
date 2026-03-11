package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.TourelleRequestDTO;
import com.kcdformes.dto.TourelleResponseDTO;
import com.kcdformes.service.TourelleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parties/{partieId}/tourelles")
@Tag(name = "Tourelles", description = "Gestion des tourelles d'une partie. Chaque tourelle est composée de 1 à 3 formes (Triangle=archer, Cercle=catapulte). Les stats (DPS, AoE, PV, coût) sont calculées automatiquement depuis les formes.")
public class TourelleController {

    private final TourelleService tourelleService;

    public TourelleController(TourelleService tourelleService) {
        this.tourelleService = tourelleService;
    }

    @PostMapping
    @Operation(summary = "Ajouter une tourelle", description = "Crée une tourelle composée de 1 à 3 formes géométriques. Le modèle métier calcule les stats : DPS total, AoE (si Cercle présent), PV (si Rectangle présent), nombre de tirs (nombre de Triangles).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tourelle créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Nom manquant, aucune forme, ou type de forme inconnu"),
            @ApiResponse(responseCode = "404", description = "Partie introuvable"),
            @ApiResponse(responseCode = "422", description = "Nom trop court, plus de 3 formes, ou dimensions invalides")
    })
    public ResponseEntity<ApiResponseDTO<TourelleResponseDTO>> ajouterTourelle(
            @PathVariable Long partieId,
            @RequestBody TourelleRequestDTO dto) {
        TourelleResponseDTO tourelle = tourelleService.ajouterTourelle(partieId, dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.cree("Tourelle '" + tourelle.getNom() + "' créée avec succès.", tourelle));
    }

    @GetMapping
    @Operation(summary = "Lister les tourelles d'une partie", description = "Retourne toutes les tourelles placées dans une partie avec leurs stats calculées.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "ID de partie invalide")
    })
    public ResponseEntity<ApiResponseDTO<List<TourelleResponseDTO>>> getTourelles(@PathVariable Long partieId) {
        List<TourelleResponseDTO> tourelles = tourelleService.getTourelles(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Tourelles de la partie " + partieId + " récupérées.", tourelles));
    }

    @DeleteMapping("/{tourelleId}")
    @Operation(summary = "Supprimer une tourelle", description = "Supprime une tourelle. Vérifie qu'elle appartient bien à la partie spécifiée.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tourelle supprimée"),
            @ApiResponse(responseCode = "400", description = "ID invalide"),
            @ApiResponse(responseCode = "404", description = "Tourelle introuvable"),
            @ApiResponse(responseCode = "409", description = "La tourelle n'appartient pas à cette partie")
    })
    public ResponseEntity<ApiResponseDTO<Void>> supprimerTourelle(
            @PathVariable Long partieId,
            @PathVariable Long tourelleId) {
        tourelleService.supprimerTourelle(partieId, tourelleId);
        return ResponseEntity.ok(ApiResponseDTO.sansContenu("Tourelle supprimée avec succès."));
    }
}