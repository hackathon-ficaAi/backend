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
 *             MockMvc - Permite fazer requisições HTTP simuladas
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
        clienteInput.setPais("Brasil");
        clienteInput.setGenero("masculino");
        clienteInput.setIdade(35);
        clienteInput.setNumProdutos(2);
        clienteInput.setMembroAtivo(true);
        clienteInput.setSaldo(5000.0);
        clienteInput.setSalarioEstimado(8000.0);
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
    @DisplayName("POST /api/predict - Deve retornar 400 quando pais for vazio")
    void deveRetornar400QuandoPaisForVazio() throws Exception {
        clienteInput.setPais("");

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/predict - Deve retornar 400 quando idade for menor que 1")
    void deveRetornar400QuandoIdadeInvalida() throws Exception {
        clienteInput.setIdade(0);

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/predict - Deve processar JSON em snake_case corretamente")
    void deveProcessarJsonSnakeCase() throws Exception {

        PrevisaoOutputDTO previsaoEsperada = new PrevisaoOutputDTO("Vai cancelar", 0.81);

        when(churnService.analisarCliente(any(ClienteInputDTO.class)))
                .thenReturn(previsaoEsperada);

        String json = """
                {
                "pais": "brasil",
                "genero": "masculino",
                "idade": 40,
                "num_produtos": 1,
                "membro_ativo": 0,
                "saldo": 300.0,
                "salario_estimado": 2000.0
                }
                """;

        mockMvc.perform(post("/api/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previsao", is("Vai cancelar")))
                .andExpect(jsonPath("$.probabilidade", is(0.81)));
    }

}