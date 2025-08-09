package com.fiap.produto.usecase.service;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.gateway.ProdutoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarProdutosServiceUseCaseTest {

    @Mock
    private ProdutoGateway gateway;

    @InjectMocks
    private ListarProdutosServiceUseCase listarProdutosServiceUseCase;

    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        produto1 = Produto.builder()
                .id(1L)
                .nome("Produto 1")
                .sku("SKU001")
                .preco(10.99)
                .build();

        produto2 = Produto.builder()
                .id(2L)
                .nome("Produto 2")
                .sku("SKU002")
                .preco(25.50)
                .build();
    }

    @Test
    void deveRetornarListaComProdutosQuandoExistiremProdutosCadastrados() {
        var produtosEsperados = List.of(produto1, produto2);
        when(gateway.listarTodos()).thenReturn(produtosEsperados);

        var resultado = listarProdutosServiceUseCase.execute();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(produtosEsperados, resultado);
        verify(gateway, times(1)).listarTodos();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverProdutosCadastrados() {
        when(gateway.listarTodos()).thenReturn(Collections.emptyList());

        var resultado = listarProdutosServiceUseCase.execute();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
        verify(gateway, times(1)).listarTodos();
    }

    @Test
    void deveRetornarListaComUmProdutoQuandoExistirApenasUmProdutoCadastrado() {
        var produtosEsperados = List.of(produto1);
        when(gateway.listarTodos()).thenReturn(produtosEsperados);

        var resultado = listarProdutosServiceUseCase.execute();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(produto1, resultado.get(0));
        verify(gateway, times(1)).listarTodos();
    }

    @Test
    void deveChamarGatewayApenasumaVezAoExecutar() {
        var produtosEsperados = List.of(produto1, produto2);
        when(gateway.listarTodos()).thenReturn(produtosEsperados);

        listarProdutosServiceUseCase.execute();

        verify(gateway, times(1)).listarTodos();
        verifyNoMoreInteractions(gateway);
    }
}