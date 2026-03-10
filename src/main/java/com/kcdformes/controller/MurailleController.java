package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.MurailleRequestDTO;
import com.kcdformes.dto.MurailleResponseDTO;
import com.kcdformes.service.MurailleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parties/{partieId}/murailles")
public class MurailleController {

    private final MurailleService murailleService;

    public MurailleController(MurailleService murailleService) {
        this.murailleService = murailleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<MurailleResponseDTO>> placerMuraille(
            @PathVariable Long partieId,
            @RequestBody MurailleRequestDTO dto) {
        MurailleResponseDTO muraille = murailleService.placerMuraille(partieId, dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Muraille placée en position " + muraille.getPosition() + ".", muraille));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MurailleResponseDTO>>> getMurailles(@PathVariable Long partieId) {
        List<MurailleResponseDTO> murailles = murailleService.getMurailles(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Murailles de la partie " + partieId + " récupérées.", murailles));
    }

    @DeleteMapping("/{murailleId}")
    public ResponseEntity<ApiResponseDTO<Void>> supprimerMuraille(
            @PathVariable Long partieId,
            @PathVariable Long murailleId) {
        murailleService.supprimerMuraille(partieId, murailleId);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Muraille supprimée avec succès."));
    }
}