package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.dto.VagueResponseDTO;
import com.kcdformes.service.VagueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parties/{partieId}/vague")
public class VagueController {

    private final VagueService vagueService;

    public VagueController(VagueService vagueService) {
        this.vagueService = vagueService;
    }

    @PostMapping("/suivante")
    public ResponseEntity<ApiResponseDTO<VagueResponseDTO>> vagueSuivante(@PathVariable Long partieId) {
        VagueResponseDTO vague = vagueService.vaguesuivante(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Vague " + vague.getVagueActuelle() + " lancée.", vague));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<VagueResponseDTO>> getVagueActuelle(@PathVariable Long partieId) {
        VagueResponseDTO vague = vagueService.getVagueActuelle(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Vague actuelle récupérée.", vague));
    }
}