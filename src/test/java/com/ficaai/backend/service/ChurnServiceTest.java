// package com.ficaai.backend.service;

// import com.ficaai.backend.dto.ClienteInputDTO;
// import com.ficaai.backend.dto.PrevisaoOutputDTO;
// import com.ficaai.backend.model.HistoricoAnalise;
// import com.ficaai.backend.repository.HistoricoRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class ChurnServiceTest {

//     @Mock
//     private RestTemplate restTemplate;

    @Mock
    private HistoricoRepository historicoRepository;

//     @InjectMocks
//     private ChurnService churnService;

//     private ClienteInputDTO clienteInput;

    @BeforeEach
    void setUp() {
        clienteInput = new ClienteInputDTO();
        clienteInput.setPais("Brasil");
        clienteInput.setGenero("masculino");
        clienteInput.setIdade(30);
        clienteInput.setNumProdutos(2);
        clienteInput.setMembroAtivo(true);
        clienteInput.setSaldo(1000.0);
        clienteInput.setSalarioEstimado(5000.0);
    }

    @Test
    @DisplayName("Deve retornar previsão e probabilidade")
    void deveRetornarPrevisaoEProbabilidade() {
        when(historicoRepository.save(any(HistoricoAnalise.class)))
                .thenReturn(new HistoricoAnalise());

//         PrevisaoOutputDTO resultado = churnService.analisarCliente(clienteInput);

        assertNotNull(resultado);
        assertNotNull(resultado.getPrevisao());
        assertNotNull(resultado.getProbabilidade());
    }

    @Test
    @DisplayName("Deve salvar histórico com dados do cliente")
    void deveSalvarHistoricoComDadosCorretos() {
        when(historicoRepository.save(any(HistoricoAnalise.class)))
                .thenReturn(new HistoricoAnalise());

//         churnService.analisarCliente(clienteInput);

        verify(historicoRepository).save(argThat(h ->
                h.getPais().equals("Brasil") &&
                h.getGenero().equals("masculino") &&
                h.getIdade().equals(30) &&
                h.getNumProdutos().equals(2) &&
                h.getMembroAtivo().equals(true) &&
                h.getSaldo().equals(1000.0) &&
                h.getSalarioEstimado().equals(5000.0) &&
                h.getPrevisao() != null &&
                h.getProbabilidade() != null
        ));
    }

    @Test
    @DisplayName("Deve gerar churn para cliente de maior risco")
    void deveGerarChurnParaClienteDeRisco() {
        clienteInput.setIdade(65);
        clienteInput.setSaldo(0.0);
        clienteInput.setNumProdutos(1);
        clienteInput.setMembroAtivo(false);

        when(historicoRepository.save(any(HistoricoAnalise.class)))
                .thenReturn(new HistoricoAnalise());

        PrevisaoOutputDTO resultado = churnService.analisarCliente(clienteInput);

        assertEquals("Vai cancelar", resultado.getPrevisao());
    }

    @Test
    @DisplayName("Não deve quebrar se falhar ao salvar histórico")
    void naoDeveQuebrarSeSalvarFalhar() {
        when(historicoRepository.save(any(HistoricoAnalise.class)))
                .thenThrow(new RuntimeException("Erro no banco"));

        assertDoesNotThrow(() -> {
            PrevisaoOutputDTO resultado = churnService.analisarCliente(clienteInput);
            assertNotNull(resultado);
        });
    }
}

