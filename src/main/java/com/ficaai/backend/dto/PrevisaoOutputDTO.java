package com.ficaai.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Gera construtor com todos os argumentos
public class PrevisaoOutputDTO {
    private String previsao;      // "Vai cancelar" ou "Vai continuar"
    private Double probabilidade; // Ex: 0.89
}