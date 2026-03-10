package com.kcdformes.controller;

import com.kcdformes.dto.ApiResponseDTO;
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
    public ResponseEntity<ApiResponseDTO<JoueurResponseDTO>> creerJoueur(@RequestBody JoueurRequestDTO dto) {
        JoueurResponseDTO joueur = joueurService.creerJoueur(dto);
        return ResponseEntity.status(201)
                .body(ApiResponseDTO.created("Joueur '" + joueur.getNom() + "' créé avec succès.", joueur));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<JoueurResponseDTO>>> listerJoueurs() {
        List<JoueurResponseDTO> joueurs = joueurService.listerJoueurs();
        return ResponseEntity.ok(ApiResponseDTO.ok("Liste des joueurs récupérée.", joueurs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<JoueurResponseDTO>> getJoueur(@PathVariable Long id) {
        JoueurResponseDTO joueur = joueurService.getJoueur(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Joueur avec l'id " + id + " introuvable."));
        return ResponseEntity.ok(ApiResponseDTO.ok("Joueur récupéré.", joueur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<JoueurResponseDTO>> modifierJoueur(
            @PathVariable Long id, @RequestBody JoueurRequestDTO dto) {
        JoueurResponseDTO joueur = joueurService.modifierJoueur(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.ok("Joueur modifié avec succès.", joueur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> supprimerJoueur(@PathVariable Long id) {
        joueurService.supprimerJoueur(id);
        return ResponseEntity.ok(ApiResponseDTO.noContent("Joueur supprimé avec succès."));
    }
}