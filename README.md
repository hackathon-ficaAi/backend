# 🤖 FicaAI - Backend Service

Microsserviço responsável pela inteligência de predição de Churn (Rotatividade de Clientes). Desenvolvido em **Java 21** com **Spring Boot**, utilizando arquitetura de containers para fácil deploy.

---

## 🚀 Como Rodar (Jeito Rápido)

### Passo a Passo

1.  Abra o terminal na raiz do projeto.
2.  Execute o comando abaixo para compilar e subir o ambiente:

```bash
docker-compose up --build
```
## Links Úteis

**[💻 Aplicação Web (Frontend)](http://localhost:5173/frontend/)**

Documentação (Swagger)	http://localhost:8080/swagger-ui/index.html	--> Teste os endpoints visualmente.

Banco de Dados (H2)	http://localhost:8080/h2-console --> Acesse o banco em memória.

## Credenciais do banco H2

Driver Class: org.h2.Driver

JDBC URL: jdbc:h2:mem:ficaaidb

User Name: sa

Password: password

# Contrato de Dados

Exemplo de Entrada (JSON):
{
  "pais": "France",
  "genero": "Female",
  "idade": 40,
  "saldo": 60000.0,
  "num_produtos": 2,
  "membro_ativo": true,
  "salario_estimado": 50000.0
}

Acredito que só na documentação esteja errado, mas deixando avisado para vocês

Exemplo de Saída (JSON):

{
  "previsao": "Vai continuar",
  "probabilidade": 0.95
}

## 2. Estatísticas do Sistema

Prever Churn

Analisa os dados de um cliente e retorna a probabilidade de cancelamento.

    Método: POST

    URL: /api/predict

Histórico

Retorna métricas gerais sobre as análises realizadas desde a inicialização.

    Método: GET

    URL: /api/stats

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
