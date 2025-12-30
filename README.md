# 🤖 FicaAI - Backend Service

Microsserviço responsável pela orquestração e persistência da previsão de Churn (Rotatividade de Clientes Bancários).

Desenvolvido em Java 21 com Spring Boot, este serviço atua como uma API Gateway para o modelo de Machine Learning (Python), gerenciando o histórico de análises e a comunicação entre o Frontend e a Inteligência Artificial.

---

## 🚀 Como Rodar (Jeito Rápido)

![Java](https://img.shields.io/badge/Java-21-orange)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

Este projeto foi desenhado para ser agnóstico ao ambiente, rodando via **Docker**. Você não precisa instalar Java ou Maven localmente.

### Pré-requisitos

* [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução.

### Passo a Passo

1.  Abra o terminal na raiz do projeto.
2.  Execute o comando abaixo para compilar e subir o ambiente:

```bash
docker-compose up --build
```
## Links Úteis

Documentação (Swagger)	http://localhost:8080/swagger-ui/index.html	--> Teste os endpoints visualmente.

Banco de Dados (H2)	http://localhost:8080/h2-console --> Acesse o banco em memória.

## Credenciais do banco H2

Driver Class: org.h2.Driver

JDBC URL: jdbc:h2:mem:ficaaidb

User Name: sa

Password: password

# Contrato de Dados (Mockados atualmente)

1. Prever Churn

Analisa os dados de um cliente e retorna a probabilidade de cancelamento.

    Método: POST

    URL: /api/predict

Exemplo de Entrada (JSON):
```
{
  "credit_score": 650,
  "pais": "France",
  "genero": "Female",
  "idade": 40,
  "tenure": 3,
  "saldo": 60000.0,
  "num_produtos": 2,
  "tem_cartao_credito": true,
  "membro_ativo": true,
  "salario_estimado": 50000.0
}
```

Exemplo de Saída (JSON):
```
{
  "previsao": "Vai continuar",
  "probabilidade": 0.95
}
```
## 2. Estatísticas do Sistema

Retorna métricas gerais sobre as análises realizadas desde a inicialização.

    Método: GET

    URL: /api/historico

## 🛠️ Tecnologias Utilizadas

Linguagem: Java 21 (Eclipse Temurin)

Framework: Spring Boot 3.3.5

Banco de Dados: H2 Database (Em memória, para alta velocidade)

Documentação: SpringDoc OpenApi (Swagger)

Containerização: Docker & Docker Compose

## 📂 Estrutura do Projeto
```
src/main/java/com/ficaai/backend
├── controller   # Pontos de entrada da API (REST)
├── dto          # Objetos de Transferência de Dados (Contrato JSON)
├── model        # Entidades do Banco de Dados
├── repository   # Camada de acesso a dados (JPA)
└── service      # Regras de Negócio e Lógica de IA
