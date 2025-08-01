package com.fiap.produto.usecase.service;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.gateway.ProdutoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarProdutoServiceUseCaseTest {

    @Mock
    private ProdutoGateway gateway;

    @InjectMocks
    private AtualizarProdutoServiceUseCase atualizarProdutoServiceUseCase;

    private Produto produto;
    private Produto produtoAtualizado;

    @BeforeEach
    void setUp() {
        produto = criarProduto();
        produtoAtualizado = criarProdutoAtualizado();
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        when(gateway.atualizar(any(Produto.class))).thenReturn(produtoAtualizado);

        var resultado = atualizarProdutoServiceUseCase.execute(produto);

        assertNotNull(resultado);
        assertEquals(produtoAtualizado.getId(), resultado.getId());
        assertEquals(produtoAtualizado.getNome(), resultado.getNome());
        assertEquals(produtoAtualizado.getSku(), resultado.getSku());
        assertEquals(produtoAtualizado.getPreco(), resultado.getPreco());
        verify(gateway, times(1)).atualizar(produto);
    }

    @Test
    void deveChamarGatewayComProdutoCorreto() {
        when(gateway.atualizar(produto)).thenReturn(produtoAtualizado);

        atualizarProdutoServiceUseCase.execute(produto);

        verify(gateway).atualizar(produto);
    }

    @Test
    void deveLancarExcecaoQuandoGatewayFalhar() {
        var mensagemErro = "Erro ao atualizar produto";
        when(gateway.atualizar(any(Produto.class))).thenThrow(new RuntimeException(mensagemErro));

        var exception = assertThrows(RuntimeException.class, () ->
                atualizarProdutoServiceUseCase.execute(produto));

        assertEquals(mensagemErro, exception.getMessage());
        verify(gateway, times(1)).atualizar(produto);
    }

    @Test
    void deveRetornarNullQuandoGatewayRetornarNull() {
        when(gateway.atualizar(any(Produto.class))).thenReturn(null);

        var resultado = atualizarProdutoServiceUseCase.execute(produto);

        assertNull(resultado);
        verify(gateway, times(1)).atualizar(produto);
    }

    @Test
    void devePassarProdutoNullParaGateway() {
        when(gateway.atualizar(null)).thenReturn(null);

        var resultado = atualizarProdutoServiceUseCase.execute(null);

        assertNull(resultado);
        verify(gateway, times(1)).atualizar(null);
    }

    private Produto criarProduto() {
        return Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .sku("SKU001")
                .preco(100.0)
                .build();
    }

    private Produto criarProdutoAtualizado() {
        return Produto.builder()
                .id(1L)
                .nome("Produto Teste Atualizado")
                .sku("SKU001")
                .preco(150.0)
                .build();
    }
}