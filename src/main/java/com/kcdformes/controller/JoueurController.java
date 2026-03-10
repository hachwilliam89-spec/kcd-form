package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.JoueurRequestDTO;
import com.kcdformes.dto.JoueurResponseDTO;
import com.kcdformes.service.JoueurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/joueurs")
@Tag(name = "Joueurs", description = "Gestion des joueurs. Chaque joueur a un nom, un budget en or, un score et des vies.")
public class JoueurController {

    private final JoueurService joueurService;

    public JoueurController(JoueurService joueurService) {
        this.joueurService = joueurService;
    }

    @PostMapping
    @Operation(summary = "Créer un joueur", description = "Crée un nouveau joueur avec un nom (min 2 caractères), un budget (0-10000) et des vies (1-10). Le score démarre à 0.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Joueur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Nom manquant ou vide"),
            @ApiResponse(responseCode = "422", description = "Nom trop court, budget négatif ou vies invalides")
    })
    public ResponseEntity<ApiResponseDTO<JoueurResponseDTO>> creerJoueur(@RequestBody JoueurRequestDTO dto) {
        JoueurResponseDTO joueur = joueurService.creerJoueur(dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Joueur '" + joueur.getNom() + "' créé avec succès.", joueur));
    }

    @GetMapping
    @Operation(summary = "Lister tous les joueurs", description = "Retourne la liste de tous les joueurs enregistrés.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public ResponseEntity<ApiResponseDTO<List<JoueurResponseDTO>>> listerJoueurs() {
        List<JoueurResponseDTO> joueurs = joueurService.listerJoueurs();
        return ResponseEntity.ok(ApiResponseDTO.ok("Liste des joueurs récupérée.", joueurs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un joueur par ID", description = "Retourne un joueur avec son budget, score et vies.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Joueur trouvé"),
            @ApiResponse(responseCode = "404", description = "Joueur introuvable avec cet ID")
    })
    public ResponseEntity<ApiResponseDTO<JoueurResponseDTO>> getJoueur(@PathVariable Long id) {
        JoueurResponseDTO joueur = joueurService.getJoueur(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Joueur avec l'id " + id + " introuvable."));
        return ResponseEntity.ok(ApiResponseDTO.ok("Joueur récupéré.", joueur));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un joueur", description = "Met à jour le nom, budget et vies d'un joueur existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Joueur modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Joueur introuvable"),
            @ApiResponse(responseCode = "422", description = "Valeurs incohérentes")
    })
    public ResponseEntity<ApiResponseDTO<JoueurResponseDTO>> modifierJoueur(
            @PathVariable Long id, @RequestBody JoueurRequestDTO dto) {
        JoueurResponseDTO joueur = joueurService.modifierJoueur(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.ok("Joueur modifié avec succès.", joueur));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un joueur", description = "Supprime définitivement un joueur par son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Joueur supprimé"),
            @ApiResponse(responseCode = "400", description = "ID invalide"),
            @ApiResponse(responseCode = "404", description = "Joueur introuvable")
    })
    public ResponseEntity<ApiResponseDTO<Void>> supprimerJoueur(@PathVariable Long id) {
        joueurService.supprimerJoueur(id);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Joueur supprimé avec succès."));
    }
}