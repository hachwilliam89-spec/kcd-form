package com.kcdformes.controller;

import com.kcdformes.dto.PartieRequestDTO;
import com.kcdformes.dto.PartieResponseDTO;
import com.kcdformes.service.PartieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parties")
public class PartieController {

    private final PartieService partieService;

    public PartieController(PartieService partieService) {
        this.partieService = partieService;
    }

    @PostMapping
    public ResponseEntity<PartieResponseDTO> creerPartie(@RequestBody PartieRequestDTO dto) {
        return ResponseEntity.ok(partieService.creerPartie(dto));
    }

    @GetMapping
    public ResponseEntity<List<PartieResponseDTO>> listerParties() {
        return ResponseEntity.ok(partieService.listerParties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartieResponseDTO> getPartie(@PathVariable Long id) {
        return partieService.getPartie(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}