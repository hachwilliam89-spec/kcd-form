package com.kcdformes.controller;

import com.kcdformes.dto.JoueurRequestDTO;
import com.kcdformes.dto.JoueurResponseDTO;
import com.kcdformes.service.JoueurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/joueurs")
public class JoueurController {

    private final JoueurService joueurService;

    public JoueurController(JoueurService joueurService) {
        this.joueurService = joueurService;
    }

    @PostMapping
    public ResponseEntity<JoueurResponseDTO> creerJoueur(@RequestBody JoueurRequestDTO dto) {
        return ResponseEntity.ok(joueurService.creerJoueur(dto));
    }

    @GetMapping
    public ResponseEntity<List<JoueurResponseDTO>> listerJoueurs() {
        return ResponseEntity.ok(joueurService.listerJoueurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JoueurResponseDTO> getJoueur(@PathVariable Long id) {
        return joueurService.getJoueur(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}