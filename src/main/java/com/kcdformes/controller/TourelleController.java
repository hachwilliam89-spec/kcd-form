package com.kcdformes.controller;

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
    public ResponseEntity<TourelleResponseDTO> ajouterTourelle(
            @PathVariable Long partieId,
            @RequestBody TourelleRequestDTO dto) {
        return ResponseEntity.ok(tourelleService.ajouterTourelle(partieId, dto));
    }

    @GetMapping
    public ResponseEntity<List<TourelleResponseDTO>> getTourelles(@PathVariable Long partieId) {
        return ResponseEntity.ok(tourelleService.getTourelles(partieId));
    }

    @DeleteMapping("/{tourelleId}")
    public ResponseEntity<Void> supprimerTourelle(
            @PathVariable Long partieId,
            @PathVariable Long tourelleId) {
        tourelleService.supprimerTourelle(partieId, tourelleId);
        return ResponseEntity.noContent().build();
    }

}