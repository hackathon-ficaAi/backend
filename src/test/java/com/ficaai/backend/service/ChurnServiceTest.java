package com.ficaai.backend.service;

import com.ficaai.backend.dto.ClienteInputDTO;
import com.ficaai.backend.dto.PrevisaoOutputDTO;
import com.ficaai.backend.model.HistoricoAnalise;
import com.ficaai.backend.repository.HistoricoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes Unitários para ChurnService
 *
 * @ExtendWith(MockitoExtension.class) - Habilita o Mockito para criar mocks
 * @Mock - Cria um mock (objeto falso) das dependências
 * @InjectMocks - Injeta os mocks no objeto que queremos testar
 */
@ExtendWith(MockitoExtension.class)
class ChurnServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HistoricoRepository repository;

    @InjectMocks
    private ChurnService churnService;

    private ClienteInputDTO clienteInput;

    /**
     * @BeforeEach - Executa antes de cada teste
     * Preparamos dados de teste que serão usados em vários testes
     */
    @BeforeEach
    void setUp() {
        clienteInput = new ClienteInputDTO();
        clienteInput.setTempoContratoMeses(12);
        clienteInput.setAtrasosPagamento(2);
        clienteInput.setUsoMensal(150.0);
        clienteInput.setPlano("Premium");
    }

    @Test
    @DisplayName("Deve retornar 'Vai continuar' quando atrasos <= 3")
    void deveRetornarVaiContinuarQuandoAtrasosMenorOuIgualTres() {
        clienteInput.setAtrasosPagamento(2);
        
        when(repository.save(any(HistoricoAnalise.class)))
            .thenReturn(new HistoricoAnalise());

        PrevisaoOutputDTO resultado = churnService.analisarCliente(clienteInput);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals("Vai continuar", resultado.getPrevisao());
        assertEquals(0.95, resultado.getProbabilidade());
        
        verify(repository, times(1)).save(any(HistoricoAnalise.class));
    }

    @Test
    @DisplayName("Deve retornar 'Vai cancelar' quando atrasos > 3")
    void deveRetornarVaiCancelarQuandoAtrasosMaiorQueTres() {
        clienteInput.setAtrasosPagamento(5);
        
        when(repository.save(any(HistoricoAnalise.class)))
            .thenReturn(new HistoricoAnalise());

        PrevisaoOutputDTO resultado = churnService.analisarCliente(clienteInput);

        assertNotNull(resultado);
        assertEquals("Vai cancelar", resultado.getPrevisao());
        assertEquals(0.81, resultado.getProbabilidade());
        
        verify(repository, times(1)).save(any(HistoricoAnalise.class));
    }

    @Test
    @DisplayName("Deve salvar histórico no banco com dados corretos")
    void deveSalvarHistoricoComDadosCorretos() {
        HistoricoAnalise historicoCapturado = new HistoricoAnalise();
        historicoCapturado.setId(1L);
        
        when(repository.save(any(HistoricoAnalise.class)))
            .thenReturn(historicoCapturado);

        churnService.analisarCliente(clienteInput);

        verify(repository, times(1)).save(argThat(historico ->
            historico.getTempoContratoMeses().equals(12) &&
            historico.getAtrasosPagamento().equals(2) &&
            historico.getUsoMensal().equals(150.0) &&
            historico.getPlano().equals("Premium") &&
            historico.getPrevisao() != null &&
            historico.getProbabilidade() != null
        ));
    }

    @Test
    @DisplayName("Deve continuar funcionando mesmo se falhar ao salvar no banco")
    void deveContinuarFuncionandoSeRepositoryFalhar() {
        when(repository.save(any(HistoricoAnalise.class)))
            .thenThrow(new RuntimeException("Erro no banco"));

        assertDoesNotThrow(() -> {
            PrevisaoOutputDTO resultado = churnService.analisarCliente(clienteInput);
            assertNotNull(resultado);
            assertEquals("Vai continuar", resultado.getPrevisao());
        });
    }

    @Test
    @DisplayName("Deve processar diferentes valores de atrasos corretamente")
    void deveProcessarDiferentesValoresAtrasos() {
        when(repository.save(any(HistoricoAnalise.class)))
            .thenReturn(new HistoricoAnalise());

        clienteInput.setAtrasosPagamento(3);
        PrevisaoOutputDTO resultado = churnService.analisarCliente(clienteInput);
        assertEquals("Vai continuar", resultado.getPrevisao(), 
            "Com 3 atrasos deve continuar");

        clienteInput.setAtrasosPagamento(4);
        resultado = churnService.analisarCliente(clienteInput);
        assertEquals("Vai cancelar", resultado.getPrevisao(), 
            "Com 4 atrasos deve cancelar");
    }

    @Test
    @DisplayName("Deve processar cliente sem atrasos")
    void deveProcessarClienteSemAtrasos() {
        clienteInput.setAtrasosPagamento(0);
        when(repository.save(any(HistoricoAnalise.class)))
            .thenReturn(new HistoricoAnalise());

        PrevisaoOutputDTO resultado = churnService.analisarCliente(clienteInput);

        assertEquals("Vai continuar", resultado.getPrevisao());
        assertEquals(0.95, resultado.getProbabilidade());
    }
}

