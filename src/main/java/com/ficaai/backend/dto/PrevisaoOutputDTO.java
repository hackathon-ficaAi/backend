package com.ficaai.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Gera construtor com todos os argumentos
public class PrevisaoOutputDTO {

    @JsonProperty("previsao_churn")
    private String previsao;      // "Vai cancelar" ou "Vai continuar"

    @JsonProperty("probabilidade_churn")
    private Double probabilidade; // Ex: 0.89
}