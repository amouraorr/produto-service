package com.fiap.produto.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarBadRequestParaIllegalArgumentException() {
        var mensagemErro = "Argumento inválido fornecido";
        var exception = new IllegalArgumentException(mensagemErro);

        var response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mensagemErro, response.getBody().get("error"));
    }

    @Test
    void deveRetornarBadRequestComMensagemVaziaParaIllegalArgumentException() {
        var exception = new IllegalArgumentException("");

        var response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().get("error"));
    }

    @Test
    void deveRetornarBadRequestComMensagemNulaParaIllegalArgumentException() {
        var exception = new IllegalArgumentException((String) null);

        var response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().get("error"));
    }

    @Test
    void deveRetornarInternalServerErrorParaRuntimeException() {
        var mensagemErro = "Erro de runtime inesperado";
        var exception = new RuntimeException(mensagemErro);

        var response = globalExceptionHandler.handleRuntimeException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro interno no servidor", response.getBody().get("error"));
    }

    @Test
    void deveRetornarInternalServerErrorComMensagemPadrao() {
        var exception = new RuntimeException();

        var response = globalExceptionHandler.handleRuntimeException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro interno no servidor", response.getBody().get("error"));
    }

    @Test
    void deveRetornarInternalServerErrorParaRuntimeExceptionComCausa() {
        var causa = new IllegalStateException("Estado inválido");
        var exception = new RuntimeException("Erro principal", causa);

        var response = globalExceptionHandler.handleRuntimeException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro interno no servidor", response.getBody().get("error"));
    }

    @Test
    void deveRetornarBadRequestParaMethodArgumentNotValidException() {
        var fieldError1 = new FieldError("produto", "nome", "Nome é obrigatório");
        var fieldError2 = new FieldError("produto", "preco", "Preço deve ser positivo");
        var fieldErrors = Arrays.asList(fieldError1, fieldError2);

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(methodArgumentNotValidException.getMessage()).thenReturn("Validation failed");

        var response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, String> errors = response.getBody();
        assertEquals(2, errors.size());
        assertEquals("Nome é obrigatório", errors.get("nome"));
        assertEquals("Preço deve ser positivo", errors.get("preco"));
    }

    @Test
    void deveRetornarBadRequestComMapaVazioQuandoNaoHouverErrosDeCampo() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList());
        when(methodArgumentNotValidException.getMessage()).thenReturn("Validation failed");

        var response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void deveRetornarBadRequestComUmErroDeValidacao() {
        var fieldError = new FieldError("produto", "categoria", "Categoria inválida");

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));
        when(methodArgumentNotValidException.getMessage()).thenReturn("Validation failed");

        var response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, String> errors = response.getBody();
        assertEquals(1, errors.size());
        assertEquals("Categoria inválida", errors.get("categoria"));
    }

    @Test
    void deveManterUltimaMensagemQuandoHouverCamposDuplicados() {
        var fieldError1 = new FieldError("produto", "nome", "Primeira mensagem");
        var fieldError2 = new FieldError("produto", "nome", "Segunda mensagem");
        var fieldErrors = Arrays.asList(fieldError1, fieldError2);

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(methodArgumentNotValidException.getMessage()).thenReturn("Validation failed");

        var response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, String> errors = response.getBody();
        assertEquals(1, errors.size());
        assertEquals("Segunda mensagem", errors.get("nome"));
    }
}