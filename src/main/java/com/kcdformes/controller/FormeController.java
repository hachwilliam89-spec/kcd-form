package com.kcdformes.controller;

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
    public ResponseEntity<List<FormeResponseDTO>> listerFormes() {
        return ResponseEntity.ok(formeService.listerFormes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormeResponseDTO> getForme(@PathVariable Long id) {
        return ResponseEntity.ok(formeService.getForme(id));
    }

    @PostMapping
    public ResponseEntity<FormeResponseDTO> creerForme(@RequestBody FormeDTO dto) {
        return ResponseEntity.ok(formeService.creerForme(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormeResponseDTO> modifierForme(@PathVariable Long id, @RequestBody FormeDTO dto) {
        return ResponseEntity.ok(formeService.modifierForme(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerForme(@PathVariable Long id) {
        formeService.supprimerForme(id);
        return ResponseEntity.noContent().build();
    }
}