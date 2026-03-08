package com.kcdformes.controller;

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
    public ResponseEntity<MurailleResponseDTO> placerMuraille(
            @PathVariable Long partieId,
            @RequestBody MurailleRequestDTO dto) {
        return ResponseEntity.ok(murailleService.placerMuraille(partieId, dto));
    }

    @GetMapping
    public ResponseEntity<List<MurailleResponseDTO>> getMurailles(@PathVariable Long partieId) {
        return ResponseEntity.ok(murailleService.getMurailles(partieId));
    }

    @DeleteMapping("/{murailleId}")
    public ResponseEntity<Void> supprimerMuraille(
            @PathVariable Long partieId,
            @PathVariable Long murailleId) {
        murailleService.supprimerMuraille(partieId, murailleId);
        return ResponseEntity.noContent().build();
    }
}