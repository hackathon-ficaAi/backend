// package com.ficaai.backend.dto;

// import jakarta.validation.ConstraintViolation;
// import jakarta.validation.Validation;
// import jakarta.validation.Validator;
// import jakarta.validation.ValidatorFactory;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;

// import java.util.Set;

// import static org.junit.jupiter.api.Assertions.*;

// /**
//  * Testes para validações do ClienteInputDTO
//  * 
//  * Testa se as anotações de validação (@NotNull, @Min, @Positive, etc.) 
//  * estão funcionando corretamente
//  */
// class ClienteInputDTOTest {

//     private Validator validator;
//     private ClienteInputDTO cliente;

//     @BeforeEach
//     void setUp() {
//         ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//         validator = factory.getValidator();

//         cliente = new ClienteInputDTO();
//         cliente.setTempoContratoMeses(12);
//         cliente.setAtrasosPagamento(2);
//         cliente.setUsoMensal(150.0);
//         cliente.setPlano("Premium");
//     }

//     @Test
//     @DisplayName("Deve validar DTO com todos os campos corretos")
//     void deveValidarDTOComTodosCamposCorretos() {
//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertTrue(violations.isEmpty(), "Não deveria ter erros de validação");
//     }

//     @Test
//     @DisplayName("Deve invalidar quando tempo_contrato_meses for nulo")
//     void deveInvalidarQuandoTempoContratoForNulo() {
//         cliente.setTempoContratoMeses(null);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertFalse(violations.isEmpty());
//         assertEquals(1, violations.size());
//         assertTrue(violations.stream()
//             .anyMatch(v -> v.getMessage().contains("tempo de contrato é obrigatório")));
//     }

//     @Test
//     @DisplayName("Deve invalidar quando tempo_contrato_meses for menor que 1")
//     void deveInvalidarQuandoTempoContratoMenorQue1() {
//         cliente.setTempoContratoMeses(0);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertFalse(violations.isEmpty());
//         assertTrue(violations.stream()
//             .anyMatch(v -> v.getMessage().contains("pelo menos 1 mês")));
//     }

//     @Test
//     @DisplayName("Deve invalidar quando atrasos_pagamento for nulo")
//     void deveInvalidarQuandoAtrasosPagamentoForNulo() {
//         cliente.setAtrasosPagamento(null);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertFalse(violations.isEmpty());
//         assertTrue(violations.stream()
//             .anyMatch(v -> v.getMessage().contains("número de atrasos é obrigatório")));
//     }

//     @Test
//     @DisplayName("Deve invalidar quando atrasos_pagamento for negativo")
//     void deveInvalidarQuandoAtrasosPagamentoForNegativo() {
//         cliente.setAtrasosPagamento(-1);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertFalse(violations.isEmpty());
//         assertTrue(violations.stream()
//             .anyMatch(v -> v.getMessage().contains("não podem ser negativos")));
//     }

//     @Test
//     @DisplayName("Deve invalidar quando uso_mensal for nulo")
//     void deveInvalidarQuandoUsoMensalForNulo() {
//         cliente.setUsoMensal(null);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertFalse(violations.isEmpty());
//         assertTrue(violations.stream()
//             .anyMatch(v -> v.getMessage().contains("uso mensal é obrigatório")));
//     }

//     @Test
//     @DisplayName("Deve invalidar quando uso_mensal for zero ou negativo")
//     void deveInvalidarQuandoUsoMensalForZeroOuNegativo() {
//         cliente.setUsoMensal(0.0);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertFalse(violations.isEmpty());
//         assertTrue(violations.stream()
//             .anyMatch(v -> v.getMessage().contains("deve ser um valor positivo")));
//     }

//     @Test
//     @DisplayName("Deve invalidar quando plano for nulo")
//     void deveInvalidarQuandoPlanoForNulo() {
//         cliente.setPlano(null);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertFalse(violations.isEmpty());
//         assertTrue(violations.stream()
//             .anyMatch(v -> v.getMessage().contains("plano é obrigatório")));
//     }

//     @Test
//     @DisplayName("Deve invalidar quando plano for vazio")
//     void deveInvalidarQuandoPlanoForVazio() {
//         cliente.setPlano("");

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertFalse(violations.isEmpty());
//         assertTrue(violations.stream()
//             .anyMatch(v -> v.getMessage().contains("plano é obrigatório")));
//     }

//     @Test
//     @DisplayName("Deve aceitar valores válidos nos limites")
//     void deveAceitarValoresValidosNosLimites() {
//         cliente.setTempoContratoMeses(1);
//         cliente.setAtrasosPagamento(0);
//         cliente.setUsoMensal(0.01);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertTrue(violations.isEmpty());
//     }

//     @Test
//     @DisplayName("Deve aceitar valores grandes")
//     void deveAceitarValoresGrandes() {
//         cliente.setTempoContratoMeses(999);
//         cliente.setAtrasosPagamento(100);
//         cliente.setUsoMensal(99999.99);

//         Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

//         assertTrue(violations.isEmpty());
//     }
// }

