package com.ficaai.backend.service;

import com.ficaai.backend.dto.ClienteInputDTO;
import com.ficaai.backend.dto.PrevisaoOutputDTO;
import com.ficaai.backend.exception.ExternalServiceException;
import com.ficaai.backend.model.HistoricoAnalise;
import com.ficaai.backend.repository.HistoricoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service // Diz ao Spring: "Isso aqui contém lógica de negócio"
public class ChurnService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HistoricoRepository repository;

    // URL do Python (O time de Data Science vai te passar isso depois)
    // Por enquanto aponta para localhost na porta 5000 (padrão do Flask/Python)
    private final String PYTHON_API_URL = "http://localhost:5000/predict";

    // Mude para TRUE se o Python cair ou não estiver pronto
    private final boolean USAR_MOCK = true; 

    // Novo método para salvar no histórico
    public PrevisaoOutputDTO analisarCliente(ClienteInputDTO dados) {
        PrevisaoOutputDTO resultado;

        // 1. Obtém a previsão (Mock ou Python)
        if (USAR_MOCK) {
            resultado = gerarPrevisaoMock(dados);
        } else {
            resultado = chamarApiPython(dados);
        }

        // 2. SALVAR NO BANCO (Passo Novo)
        salvarNoHistorico(dados, resultado);

        return resultado;
    }
    // Método que faz a chamada real HTTP
    private PrevisaoOutputDTO chamarApiPython(ClienteInputDTO dados) {
        try {
            // O postForObject envia o 'dados' como JSON e espera receber um PrevisaoOutputDTO de volta
            return restTemplate.postForObject(PYTHON_API_URL, dados, PrevisaoOutputDTO.class);
        } catch (ResourceAccessException e) {
            // Erro de conexão (Python API down, timeout, connection refused)
            log.error("Não foi possível conectar à API Python em {}: {}", PYTHON_API_URL, e.getMessage());
            throw new ExternalServiceException("API de previsão está indisponível", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Python API retornou erro HTTP (4xx ou 5xx)
            log.error("API Python retornou erro: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalServiceException("Erro ao processar previsão no serviço externo", e);
        }
    }

    // Método que gera uma previsão MOCK
    private PrevisaoOutputDTO gerarPrevisaoMock(ClienteInputDTO dados) {
        log.info("Gerando previsão MOCK para: {}", dados);
        
        // Nova regra baseada nos campos da imagem:
        // Se tiver muitos atrasos (mais de 3) ele cancela.
        if (dados.getAtrasosPagamento() > 3) {
            return new PrevisaoOutputDTO("Vai cancelar", 0.81);
        } else {
            return new PrevisaoOutputDTO("Vai continuar", 0.95);
        }
    }

    private void salvarNoHistorico(ClienteInputDTO dados, PrevisaoOutputDTO resultado) {
        try {
            HistoricoAnalise historico = new HistoricoAnalise();
            
            // Copia os dados do Input para o Histórico (automaticamente, pois tem nomes iguais)
            BeanUtils.copyProperties(dados, historico);
            
            // Define os dados da Saída
            historico.setPrevisao(resultado.getPrevisao());
            historico.setProbabilidade(resultado.getProbabilidade());
            
            // Salva no H2
            repository.save(historico);
            log.info("Histórico salvo com sucesso! ID: {}", historico.getId());

        } catch (Exception e) {
            log.error("Erro ao salvar histórico: {}", e.getMessage(), e);
            // Não lançamos erro aqui para não travar a resposta pro usuário
        }
    }
}