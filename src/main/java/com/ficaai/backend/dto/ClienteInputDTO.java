package com.ficaai.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ClienteInputDTO {

    // A imagem pede "tempo_contrato_meses"
    @NotNull(message = "O tempo de contrato é obrigatório")
    @Min(value = 1, message = "O contrato deve ter pelo menos 1 mês")
    @JsonProperty("tempo_contrato_meses") // Isso mapeia o JSON com underline para a variável Java
    private Integer tempoContratoMeses;

    // A imagem pede "atrasos_pagamento"
    @NotNull(message = "O número de atrasos é obrigatório")
    @Min(value = 0, message = "Atrasos não podem ser negativos")
    @JsonProperty("atrasos_pagamento")
    private Integer atrasosPagamento;

    // A imagem pede "uso_mensal"
    @NotNull(message = "O uso mensal é obrigatório")
    @Positive(message = "O uso mensal deve ser um valor positivo")
    @JsonProperty("uso_mensal")
    private Double usoMensal;

    // A imagem pede "plano" (Ex: Premium)
    @NotBlank(message = "O plano é obrigatório")
    @JsonProperty("plano")
    private String plano;
}