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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarProdutoUseServiceCaseTest {

    @Mock
    private ProdutoGateway gateway;

    @InjectMocks
    private CadastrarProdutoUseServiceCase cadastrarProdutoUseServiceCase;

    private Produto produto;
    private Produto produtoExistente;

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .nome("Produto Teste")
                .sku("SKU123")
                .preco(99.99)
                .build();

        produtoExistente = Produto.builder()
                .id(1L)
                .nome("Produto Existente")
                .sku("SKU123")
                .preco(50.00)
                .build();
    }

    @Test
    void deveCadastrarProdutoComSucesso() {
        when(gateway.buscarPorSku(produto.getSku())).thenReturn(Optional.empty());
        when(gateway.salvar(produto)).thenReturn(produto);

        Produto produtoSalvo = cadastrarProdutoUseServiceCase.execute(produto);

        assertNotNull(produtoSalvo);
        assertEquals(produto.getNome(), produtoSalvo.getNome());
        assertEquals(produto.getSku(), produtoSalvo.getSku());
        assertEquals(produto.getPreco(), produtoSalvo.getPreco());

        verify(gateway).buscarPorSku(produto.getSku());
        verify(gateway).salvar(produto);
    }

    @Test
    void deveLancarExcecaoQuandoSkuJaExistir() {
        when(gateway.buscarPorSku(produto.getSku())).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cadastrarProdutoUseServiceCase.execute(produto)
        );

        assertEquals("SKU já cadastrado!", exception.getMessage());

        verify(gateway).buscarPorSku(produto.getSku());
        verify(gateway, never()).salvar(any(Produto.class));
    }

    @Test
    void deveVerificarSeGatewayBuscarPorSkuEChamado() {
        when(gateway.buscarPorSku(produto.getSku())).thenReturn(Optional.empty());
        when(gateway.salvar(produto)).thenReturn(produto);

        cadastrarProdutoUseServiceCase.execute(produto);

        verify(gateway, times(1)).buscarPorSku(produto.getSku());
    }

    @Test
    void deveVerificarSeGatewaySalvarEChamado() {
        when(gateway.buscarPorSku(produto.getSku())).thenReturn(Optional.empty());
        when(gateway.salvar(produto)).thenReturn(produto);

        cadastrarProdutoUseServiceCase.execute(produto);

        verify(gateway, times(1)).salvar(produto);
    }

    @Test
    void deveLancarExcecaoComMensagemCorreta() {
        when(gateway.buscarPorSku(produto.getSku())).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cadastrarProdutoUseServiceCase.execute(produto)
        );

        assertTrue(exception.getMessage().contains("SKU já cadastrado!"));
    }

    @Test
    void deveRetornarProdutoSalvoQuandoSkuNaoExistir() {
        Produto produtoComId = Produto.builder()
                .id(1L)
                .nome(produto.getNome())
                .sku(produto.getSku())
                .preco(produto.getPreco())
                .build();

        when(gateway.buscarPorSku(produto.getSku())).thenReturn(Optional.empty());
        when(gateway.salvar(produto)).thenReturn(produtoComId);

        Produto resultado = cadastrarProdutoUseServiceCase.execute(produto);

        assertNotNull(resultado);
        assertEquals(produtoComId.getId(), resultado.getId());
        assertEquals(produtoComId.getNome(), resultado.getNome());
        assertEquals(produtoComId.getSku(), resultado.getSku());
        assertEquals(produtoComId.getPreco(), resultado.getPreco());
    }

    @Test
    void deveValidarSeSkuEVerificadoCorretamente() {
        String skuTeste = "SKU-TESTE-123";
        produto.setSku(skuTeste);

        when(gateway.buscarPorSku(skuTeste)).thenReturn(Optional.empty());
        when(gateway.salvar(produto)).thenReturn(produto);

        cadastrarProdutoUseServiceCase.execute(produto);

        verify(gateway).buscarPorSku(skuTeste);
    }
}