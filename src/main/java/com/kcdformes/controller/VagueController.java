package com.kcdformes.controller;

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
    public ResponseEntity<VagueResponseDTO> vagueSuivante(@PathVariable Long partieId) {
        return ResponseEntity.ok(vagueService.vaguesuivante(partieId));
    }

    @GetMapping
    public ResponseEntity<VagueResponseDTO> getVagueActuelle(@PathVariable Long partieId) {
        return ResponseEntity.ok(vagueService.getVagueActuelle(partieId));
    }

}