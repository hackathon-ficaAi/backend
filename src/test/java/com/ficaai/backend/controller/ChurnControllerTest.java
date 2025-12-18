package com.ficaai.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ficaai.backend.dto.ClienteInputDTO;
import com.ficaai.backend.dto.PrevisaoOutputDTO;
import com.ficaai.backend.service.ChurnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de Integração para ChurnController
 * 
 * @WebMvcTest - Carrega apenas o contexto web (Controllers)
 * MockMvc - Permite fazer requisições HTTP simuladas
 * @MockBean - Cria um mock do Service (não usa a implementação real)
 */

@WebMvcTest(ChurnController.class)
class ChurnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChurnService churnService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteInputDTO clienteInput;

    @BeforeEach
    void setUp() {
        clienteInput = new ClienteInputDTO();
        clienteInput.setTempoContratoMeses(12);
        clienteInput.setAtrasosPagamento(2);
        clienteInput.setUsoMensal(150.0);
        clienteInput.setPlano("Premium");
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 200 OK com previsão válida")
    void deveRetornarPrevisaoComSucesso() throws Exception {
        PrevisaoOutputDTO previsaoEsperada = new PrevisaoOutputDTO("Vai continuar", 0.95);
        when(churnService.analisarCliente(any(ClienteInputDTO.class)))
            .thenReturn(previsaoEsperada);

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print()) // Imprime os detalhes da requisição/resposta
                .andExpect(status().isOk()) // Verifica se retorna 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica Content-Type
                .andExpect(jsonPath("$.previsao", is("Vai continuar"))) // Verifica campo 'previsao'
                .andExpect(jsonPath("$.probabilidade", is(0.95))); // Verifica campo 'probabilidade'
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 400 quando tempo_contrato_meses for nulo")
    void deveRetornar400QuandoTempoContratoForNulo() throws Exception {
        clienteInput.setTempoContratoMeses(null);

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isBadRequest()); // Deve retornar 400 Bad Request
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 400 quando atrasos_pagamento for nulo")
    void deveRetornar400QuandoAtrasosPagamentoForNulo() throws Exception {
        clienteInput.setAtrasosPagamento(null);

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 400 quando uso_mensal for nulo")
    void deveRetornar400QuandoUsoMensalForNulo() throws Exception {
        clienteInput.setUsoMensal(null);

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 400 quando plano for vazio")
    void deveRetornar400QuandoPlanoForVazio() throws Exception {
        clienteInput.setPlano("");

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 400 quando tempo_contrato_meses for menor que 1")
    void deveRetornar400QuandoTempoContratoMenorQue1() throws Exception {
        clienteInput.setTempoContratoMeses(0);

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 400 quando atrasos_pagamento for negativo")
    void deveRetornar400QuandoAtrasosPagamentoForNegativo() throws Exception {
        clienteInput.setAtrasosPagamento(-1);

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 400 quando uso_mensal for zero ou negativo")
    void deveRetornar400QuandoUsoMensalForZeroOuNegativo() throws Exception {
        clienteInput.setUsoMensal(0.0);

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/predict - Deve processar JSON com underscores corretamente")
    void deveProcessarJsonComUnderscoresCorretamente() throws Exception {
        PrevisaoOutputDTO previsaoEsperada = new PrevisaoOutputDTO("Vai cancelar", 0.81);
        when(churnService.analisarCliente(any(ClienteInputDTO.class)))
            .thenReturn(previsaoEsperada);

        String jsonComUnderscore = """
            {
                "tempo_contrato_meses": 6,
                "atrasos_pagamento": 5,
                "uso_mensal": 80.5,
                "plano": "Básico"
            }
            """;

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonComUnderscore))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previsao", is("Vai cancelar")))
                .andExpect(jsonPath("$.probabilidade", is(0.81)));
    }

    @Test
    @DisplayName("POST /api/predict - Deve aceitar diferentes planos")
    void deveAceitarDiferentesPlanos() throws Exception {
        PrevisaoOutputDTO previsaoEsperada = new PrevisaoOutputDTO("Vai continuar", 0.95);
        when(churnService.analisarCliente(any(ClienteInputDTO.class)))
            .thenReturn(previsaoEsperada);

        clienteInput.setPlano("Básico");

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isOk());

        clienteInput.setPlano("Premium");
        
        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

