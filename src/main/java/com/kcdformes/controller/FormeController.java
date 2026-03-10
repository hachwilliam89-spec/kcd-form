package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.FormeDTO;
import com.kcdformes.dto.FormeResponseDTO;
import com.kcdformes.service.FormeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formes")
public class FormeController {

    private final FormeService formeService;

    public FormeController(FormeService formeService) {
        this.formeService = formeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<FormeResponseDTO>>> listerFormes() {
        List<FormeResponseDTO> formes = formeService.listerFormes();
        return ResponseEntity.ok(ApiResponseDTO.ok("Liste des formes récupérée.", formes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FormeResponseDTO>> getForme(@PathVariable Long id) {
        FormeResponseDTO forme = formeService.getForme(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Forme récupérée.", forme));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<FormeResponseDTO>> creerForme(@RequestBody FormeDTO dto) {
        FormeResponseDTO forme = formeService.creerForme(dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Forme " + forme.getType() + " créée avec succès.", forme));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FormeResponseDTO>> modifierForme(
            @PathVariable Long id, @RequestBody FormeDTO dto) {
        FormeResponseDTO forme = formeService.modifierForme(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.ok("Forme modifiée avec succès.", forme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> supprimerForme(@PathVariable Long id) {
        formeService.supprimerForme(id);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Forme supprimée avec succès."));
    }
}