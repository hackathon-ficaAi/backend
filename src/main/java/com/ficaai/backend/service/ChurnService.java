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
import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Service // Diz ao Spring: "Isso aqui contém lógica de negócio"
public class ChurnService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HistoricoRepository repository;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    @Value("${python.api.usar-mock}")
    private boolean usarMock;


    // URL do Python (O time de Data Science vai te passar isso depois)
    // Por enquanto aponta para localhost na porta 5000 (padrão do Flask/Python)
    //private final String PYTHON_API_URL = "http://localhost:5000/predict";

    // Mude para TRUE se o Python cair ou não estiver pronto
    //private final boolean USAR_MOCK = true; 

    // Novo método para salvar no histórico
    public PrevisaoOutputDTO analisarCliente(ClienteInputDTO dados) {

        long inicioTotal = System.currentTimeMillis();
        log.info("Iniciando análise de churn");

        PrevisaoOutputDTO resultado;

        // 1. Obtém a previsão (Mock ou Python)
        if (usarMock) {
            resultado = gerarPrevisaoMock(dados);
        } else {
            resultado = chamarApiPython(dados);
        }

        // 2. SALVAR NO BANCO (Passo Novo)
        salvarNoHistorico(dados, resultado);

        long tempoTotal = System.currentTimeMillis() - inicioTotal;
        log.info(
                "Análise de churn finalizada | previsao={} | tempoTotal={}ms",
                resultado.getPrevisao(),
                tempoTotal
        );

        return resultado;
    }
    // Método que faz a chamada real HTTP
    private PrevisaoOutputDTO chamarApiPython(ClienteInputDTO dados) {

        long inicio = System.currentTimeMillis();
        log.info("Chamando API Python de previsão");

        try {
            PrevisaoOutputDTO resposta =
                    restTemplate.postForObject(pythonApiUrl, dados, PrevisaoOutputDTO.class);

            long duracao = System.currentTimeMillis() - inicio;
            log.info(
                    "Resposta da API Python recebida com sucesso | tempoResposta={}ms",
                    duracao
            );

            return resposta;

        } catch (ResourceAccessException e) {
            // Erro de conexão (Python API down, timeout, connection refused)
            long duracao = System.currentTimeMillis() - inicio;
            log.error("Não foi possível conectar à API Python em {}: {}", pythonApiUrl, e.getMessage());
            throw new ExternalServiceException("API de previsão está indisponível", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Python API retornou erro HTTP (4xx ou 5xx)
            long duracao = System.currentTimeMillis() - inicio;

            log.error(
                    "Erro retornado pela API Python | tempoResposta={}ms | status={} | body={}",
                    duracao,
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            throw new ExternalServiceException("Erro ao processar previsão no serviço externo", e);
        }
    }

    // Método que gera uma previsão MOCK
private PrevisaoOutputDTO gerarPrevisaoMock(ClienteInputDTO dados) {

    long inicio = System.currentTimeMillis();
    log.info("Gerando previsão MOCK para: {}", dados);

    double prob = 0.2;

    prob += dados.getAtrasosPagamento() * 0.15;
    prob += dados.getTempoContratoMeses() < 12 ? 0.25 : 0;
    prob += "basico".equalsIgnoreCase(dados.getPlano()) ? 0.2 : 0;

    prob = Math.min(prob, 0.99);

    String label = prob >= 0.5 ? "Vai cancelar" : "Vai continuar";

    long duracao = System.currentTimeMillis() - inicio;
    log.info(
            "Previsão MOCK gerada | previsao={} | probabilidade={} | tempoResposta={}ms",
            label,
            prob,
            duracao
    );

    return new PrevisaoOutputDTO(label, prob);
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