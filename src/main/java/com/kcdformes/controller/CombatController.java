package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
import com.kcdformes.service.CombatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parties/{partieId}/combat")
public class CombatController {

    private final CombatService combatService;

    public CombatController(CombatService combatService) {
        this.combatService = combatService;
    }

    @PostMapping("/demarrer")
    public ResponseEntity<ApiResponseDTO<Void>> demarrer(@PathVariable Long partieId) {
        combatService.demarrerCombat(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Combat démarré pour la partie " + partieId + ".", null));
    }

    @PostMapping("/reprendre")
    public ResponseEntity<ApiResponseDTO<Void>> reprendre(@PathVariable Long partieId) {
        combatService.demarrerCombat(partieId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Combat repris pour la partie " + partieId + ".", null));
    }
}