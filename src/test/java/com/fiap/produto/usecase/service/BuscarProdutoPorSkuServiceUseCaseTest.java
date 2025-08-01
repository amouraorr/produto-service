package com.fiap.produto.usecase.service;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.gateway.ProdutoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarProdutoPorSkuServiceUseCaseTest {

    @Mock
    private ProdutoGateway gateway;

    @InjectMocks
    private BuscarProdutoPorSkuServiceUseCase buscarProdutoPorSkuUseCase;

    private Produto produto;
    private String sku;

    @BeforeEach
    void setUp() {
        sku = "SKU123";
        produto = new Produto(1L, "Produto Teste", sku, 99.99);
    }

    @Test
    void deveRetornarProdutoQuandoEncontrarPorSku() {
        when(gateway.buscarPorSku(sku)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = buscarProdutoPorSkuUseCase.execute(sku);

        assertTrue(resultado.isPresent());
        assertEquals(produto, resultado.get());
        assertEquals(sku, resultado.get().getSku());
        verify(gateway).buscarPorSku(sku);
    }

    @Test
    void deveRetornarOptionalVazioQuandoNaoEncontrarProduto() {
        when(gateway.buscarPorSku(sku)).thenReturn(Optional.empty());

        Optional<Produto> resultado = buscarProdutoPorSkuUseCase.execute(sku);

        assertTrue(resultado.isEmpty());
        verify(gateway).buscarPorSku(sku);
    }

    @Test
    void deveRetornarOptionalVazioQuandoSkuForNulo() {
        when(gateway.buscarPorSku(null)).thenReturn(Optional.empty());

        Optional<Produto> resultado = buscarProdutoPorSkuUseCase.execute(null);

        assertTrue(resultado.isEmpty());
        verify(gateway).buscarPorSku(null);
    }

    @Test
    void deveRetornarOptionalVazioQuandoSkuForVazio() {
        String skuVazio = "";
        when(gateway.buscarPorSku(skuVazio)).thenReturn(Optional.empty());

        Optional<Produto> resultado = buscarProdutoPorSkuUseCase.execute(skuVazio);

        assertTrue(resultado.isEmpty());
        verify(gateway).buscarPorSku(skuVazio);
    }

    @Test
    void deveRetornarOptionalVazioQuandoSkuForEspacoEmBranco() {
        String skuEspaco = "   ";
        when(gateway.buscarPorSku(skuEspaco)).thenReturn(Optional.empty());

        Optional<Produto> resultado = buscarProdutoPorSkuUseCase.execute(skuEspaco);

        assertTrue(resultado.isEmpty());
        verify(gateway).buscarPorSku(skuEspaco);
    }

    @Test
    void deveChamarGatewayApenasUmaVez() {
        when(gateway.buscarPorSku(sku)).thenReturn(Optional.of(produto));

        buscarProdutoPorSkuUseCase.execute(sku);

        verify(gateway, times(1)).buscarPorSku(sku);
    }

    @Test
    void deveRetornarProdutoComDadosCorretos() {
        Produto produtoEsperado = new Produto(2L, "Notebook", "NOTEBOOK001", 2500.00);
        String skuNotebook = "NOTEBOOK001";
        when(gateway.buscarPorSku(skuNotebook)).thenReturn(Optional.of(produtoEsperado));

        Optional<Produto> resultado = buscarProdutoPorSkuUseCase.execute(skuNotebook);

        assertTrue(resultado.isPresent());
        assertEquals(produtoEsperado.getId(), resultado.get().getId());
        assertEquals(produtoEsperado.getNome(), resultado.get().getNome());
        assertEquals(produtoEsperado.getSku(), resultado.get().getSku());
        assertEquals(produtoEsperado.getPreco(), resultado.get().getPreco());
        verify(gateway).buscarPorSku(skuNotebook);
    }

    @Test
    void deveManterComportamentoConsistenteBuscarMultiplasVezes() {
        when(gateway.buscarPorSku(sku)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado1 = buscarProdutoPorSkuUseCase.execute(sku);
        Optional<Produto> resultado2 = buscarProdutoPorSkuUseCase.execute(sku);

        assertTrue(resultado1.isPresent());
        assertTrue(resultado2.isPresent());
        assertEquals(resultado1.get(), resultado2.get());
        verify(gateway, times(2)).buscarPorSku(sku);
    }
}