package com.ficaai.backend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para validações do ClienteInputDTO
 * 
 * Testa se as anotações de validação (@NotNull, @Min, @Positive, etc.)
 * estão funcionando corretamente
 */
class ClienteInputDTOTest {

    private Validator validator;
    private ClienteInputDTO cliente;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        cliente = new ClienteInputDTO();
        cliente.setPais("Brasil");
        cliente.setGenero("masculino");
        cliente.setIdade(30);
        cliente.setNumProdutos(2);
        cliente.setMembroAtivo(true);
        cliente.setSaldo(1000.0);
        cliente.setSalarioEstimado(5000.0);
    }

    @Test
    @DisplayName("Deve validar DTO com todos os campos corretos")
    void deveValidarDTOComTodosCamposCorretos() {
        Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);

        assertTrue(violations.isEmpty(), "Não deveria ter erros de validação");
    }

    @Test
    @DisplayName("Deve invalidar quando pais for vazio")
    void deveInvalidarQuandoPaisForVazio() {
        cliente.setPais("");

        Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve invalidar quando idade for menor que 1")
    void deveInvalidarQuandoIdadeForInvalida() {
        cliente.setIdade(0);

        Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve invalidar quando numProdutos for menor que 1")
    void deveInvalidarQuandoNumProdutosForInvalido() {
        cliente.setNumProdutos(0);

        Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve invalidar quando saldo for negativo")
    void deveInvalidarQuandoSaldoForNegativo() {
        cliente.setSaldo(-10.0);

        Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve invalidar quando salarioEstimado for negativo")
    void deveInvalidarQuandoSalarioEstimadoForNegativo() {
        cliente.setSalarioEstimado(-1.0);

        Set<ConstraintViolation<ClienteInputDTO>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
    }
}