package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.FormeDTO;
import com.kcdformes.dto.FormeResponseDTO;
import com.kcdformes.service.FormeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formes")
@Tag(name = "Formes", description = "CRUD des formes géométriques (Triangle, Cercle, Rectangle). Chaque forme calcule automatiquement son aire, périmètre, DPS et coût.")
public class FormeController {

    private final FormeService formeService;

    public FormeController(FormeService formeService) {
        this.formeService = formeService;
    }

    @GetMapping
    @Operation(summary = "Lister toutes les formes", description = "Retourne la liste de toutes les formes enregistrées avec leurs stats calculées.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public ResponseEntity<ApiResponseDTO<List<FormeResponseDTO>>> listerFormes() {
        List<FormeResponseDTO> formes = formeService.listerFormes();
        return ResponseEntity.ok(ApiResponseDTO.ok("Liste des formes récupérée.", formes));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une forme par ID", description = "Retourne une forme avec toutes ses stats calculées (aire, périmètre, DPS, coût).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forme trouvée"),
            @ApiResponse(responseCode = "400", description = "ID invalide (négatif ou null)"),
            @ApiResponse(responseCode = "404", description = "Forme introuvable avec cet ID")
    })
    public ResponseEntity<ApiResponseDTO<FormeResponseDTO>> getForme(@PathVariable Long id) {
        FormeResponseDTO forme = formeService.getForme(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Forme récupérée.", forme));
    }

    @PostMapping
    @Operation(summary = "Créer une forme", description = "Crée une forme géométrique. Types valides : TRIANGLE (valeur1=base, valeur2=hauteur), CERCLE (valeur1=rayon), RECTANGLE (valeur1=largeur, valeur2=longueur). Les stats sont calculées automatiquement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Forme créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Type manquant, couleur manquante, ou type inconnu"),
            @ApiResponse(responseCode = "422", description = "Dimensions invalides (négatives ou zéro)")
    })
    public ResponseEntity<ApiResponseDTO<FormeResponseDTO>> creerForme(@RequestBody FormeDTO dto) {
        FormeResponseDTO forme = formeService.creerForme(dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Forme " + forme.getType() + " créée avec succès.", forme));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une forme", description = "Remplace une forme existante. Les stats sont recalculées automatiquement.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forme modifiée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Forme introuvable"),
            @ApiResponse(responseCode = "422", description = "Dimensions invalides")
    })
    public ResponseEntity<ApiResponseDTO<FormeResponseDTO>> modifierForme(
            @PathVariable Long id, @RequestBody FormeDTO dto) {
        FormeResponseDTO forme = formeService.modifierForme(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.ok("Forme modifiée avec succès.", forme));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une forme", description = "Supprime définitivement une forme par son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forme supprimée"),
            @ApiResponse(responseCode = "400", description = "ID invalide"),
            @ApiResponse(responseCode = "404", description = "Forme introuvable")
    })
    public ResponseEntity<ApiResponseDTO<Void>> supprimerForme(@PathVariable Long id) {
        formeService.supprimerForme(id);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Forme supprimée avec succès."));
    }
}