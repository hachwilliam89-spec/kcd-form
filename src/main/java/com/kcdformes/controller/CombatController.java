package com.kcdformes.controller;

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
    public ResponseEntity<Void> demarrer(@PathVariable Long partieId) {
        combatService.demarrerCombat(partieId);
        return ResponseEntity.ok().build();
    }
}