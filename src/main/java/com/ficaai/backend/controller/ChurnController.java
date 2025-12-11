package com.ficaai.backend.controller;

import com.ficaai.backend.dto.ClienteInputDTO;
import com.ficaai.backend.dto.PrevisaoOutputDTO;
import com.ficaai.backend.service.ChurnService; // Importe o Service
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChurnController {

    @Autowired
    private ChurnService churnService; // Injeção de Dependência

    @PostMapping("/predict")
    public ResponseEntity<PrevisaoOutputDTO> preverChurn(@RequestBody @Valid ClienteInputDTO dados) {
        // O Controller apenas repassa a bola
        PrevisaoOutputDTO resultado = churnService.analisarCliente(dados);
        return ResponseEntity.ok(resultado);
    }
}