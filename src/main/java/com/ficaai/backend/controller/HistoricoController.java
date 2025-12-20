package com.ficaai.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ficaai.backend.model.HistoricoAnalise;
import com.ficaai.backend.repository.HistoricoRepository;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {

    private final HistoricoRepository repository;

    public HistoricoController(HistoricoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<HistoricoAnalise> listar() {
        return repository.findAll();
    }
}