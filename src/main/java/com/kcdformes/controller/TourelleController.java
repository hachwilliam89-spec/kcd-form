package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.TourelleRequestDTO;
import com.kcdformes.dto.TourelleResponseDTO;
import com.kcdformes.service.TourelleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parties/{partieId}/tourelles")
public class TourelleController {

    private final TourelleService tourelleService;

    public TourelleController(TourelleService tourelleService) {
        this.tourelleService = tourelleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TourelleResponseDTO>> ajouterTourelle(
            @PathVariable Long partieId,
            @RequestBody TourelleRequestDTO dto) {
        TourelleResponseDTO tourelle = tourelleService.ajouterTourelle(partieId, dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Tourelle '" + tourelle.getNom() + "' créée avec succès.", tourelle));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TourelleResponseDTO>>> getTourelles(@PathVariable Long partieId) {
        List<TourelleResponseDTO> tourelles = tourelleService.getTourelles(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Tourelles de la partie " + partieId + " récupérées.", tourelles));
    }

    @DeleteMapping("/{tourelleId}")
    public ResponseEntity<ApiResponseDTO<Void>> supprimerTourelle(
            @PathVariable Long partieId,
            @PathVariable Long tourelleId) {
        tourelleService.supprimerTourelle(partieId, tourelleId);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Tourelle supprimée avec succès."));
    }
}