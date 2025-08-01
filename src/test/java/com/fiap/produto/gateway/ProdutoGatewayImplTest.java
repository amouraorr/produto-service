package com.fiap.produto.gateway;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.entity.ProdutoEntity;
import com.fiap.produto.mapper.ProdutoMapper;
import com.fiap.produto.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoGatewayImplTest {

    @InjectMocks
    private ProdutoGatewayImpl produtoGateway;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoMapper mapper;

    @Test
    void deveSalvarProdutoComSucesso() {
        var produto = criarProduto();
        var produtoEntity = criarProdutoEntity();
        var produtoEntitySalva = criarProdutoEntityComId();
        var produtoSalvo = criarProdutoComId();

        when(mapper.toEntity(produto)).thenReturn(produtoEntity);
        when(repository.save(produtoEntity)).thenReturn(produtoEntitySalva);
        when(mapper.toDomain(produtoEntitySalva)).thenReturn(produtoSalvo);

        var resultado = produtoGateway.salvar(produto);

        assertEquals(produtoSalvo, resultado);
        verify(mapper).toEntity(produto);
        verify(repository).save(produtoEntity);
        verify(mapper).toDomain(produtoEntitySalva);
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        var produto = criarProdutoComId();
        var produtoEntity = criarProdutoEntityComId();
        var produtoEntityAtualizada = criarProdutoEntityComId();
        var produtoAtualizado = criarProdutoComId();

        when(mapper.toEntity(produto)).thenReturn(produtoEntity);
        when(repository.save(produtoEntity)).thenReturn(produtoEntityAtualizada);
        when(mapper.toDomain(produtoEntityAtualizada)).thenReturn(produtoAtualizado);

        var resultado = produtoGateway.atualizar(produto);

        assertEquals(produtoAtualizado, resultado);
        verify(mapper).toEntity(produto);
        verify(repository).save(produtoEntity);
        verify(mapper).toDomain(produtoEntityAtualizada);
    }

    @Test
    void deveBuscarProdutoPorSkuComSucesso() {
        var sku = "SKU123";
        var produtoEntity = criarProdutoEntityComId();
        var produto = criarProdutoComId();

        when(repository.findBySku(sku)).thenReturn(Optional.of(produtoEntity));
        when(mapper.toDomain(produtoEntity)).thenReturn(produto);

        var resultado = produtoGateway.buscarPorSku(sku);

        assertTrue(resultado.isPresent());
        assertEquals(produto, resultado.get());
        verify(repository).findBySku(sku);
        verify(mapper).toDomain(produtoEntity);
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarProdutoPorSku() {
        var sku = "SKU_INEXISTENTE";

        when(repository.findBySku(sku)).thenReturn(Optional.empty());

        var resultado = produtoGateway.buscarPorSku(sku);

        assertTrue(resultado.isEmpty());
        verify(repository).findBySku(sku);
        verify(mapper, never()).toDomain(any(ProdutoEntity.class));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        var id = 1L;
        var produtoEntity = criarProdutoEntityComId();
        var produto = criarProdutoComId();

        when(repository.findById(id)).thenReturn(Optional.of(produtoEntity));
        when(mapper.toDomain(produtoEntity)).thenReturn(produto);

        var resultado = produtoGateway.buscarPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(produto, resultado.get());
        verify(repository).findById(id);
        verify(mapper).toDomain(produtoEntity);
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarProdutoPorId() {
        var id = 999L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        var resultado = produtoGateway.buscarPorId(id);

        assertTrue(resultado.isEmpty());
        verify(repository).findById(id);
        verify(mapper, never()).toDomain(any(ProdutoEntity.class));
    }

    @Test
    void deveListarTodosProdutosComSucesso() {
        var produtoEntities = Arrays.asList(criarProdutoEntityComId(), criarProdutoEntityComIdDois());
        var produto1 = criarProdutoComId();
        var produto2 = criarProdutoComIdDois();

        when(repository.findAll()).thenReturn(produtoEntities);
        when(mapper.toDomain(produtoEntities.get(0))).thenReturn(produto1);
        when(mapper.toDomain(produtoEntities.get(1))).thenReturn(produto2);

        var resultado = produtoGateway.listarTodos();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(produto1));
        assertTrue(resultado.contains(produto2));
        verify(repository).findAll();
        verify(mapper, times(2)).toDomain(any(ProdutoEntity.class));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverProdutos() {
        when(repository.findAll()).thenReturn(List.of());

        var resultado = produtoGateway.listarTodos();

        assertTrue(resultado.isEmpty());
        verify(repository).findAll();
        verify(mapper, never()).toDomain(any(ProdutoEntity.class));
    }

    private Produto criarProduto() {
        return Produto.builder()
                .nome("Notebook Dell")
                .sku("SKU123")
                .preco(2500.00)
                .build();
    }

    private Produto criarProdutoComId() {
        return Produto.builder()
                .id(1L)
                .nome("Notebook Dell")
                .sku("SKU123")
                .preco(2500.00)
                .build();
    }

    private Produto criarProdutoComIdDois() {
        return Produto.builder()
                .id(2L)
                .nome("Mouse Logitech")
                .sku("SKU456")
                .preco(150.00)
                .build();
    }

    private ProdutoEntity criarProdutoEntity() {
        return ProdutoEntity.builder()
                .nome("Notebook Dell")
                .sku("SKU123")
                .preco(2500.00)
                .build();
    }

    private ProdutoEntity criarProdutoEntityComId() {
        return ProdutoEntity.builder()
                .id(1L)
                .nome("Notebook Dell")
                .sku("SKU123")
                .preco(2500.00)
                .build();
    }

    private ProdutoEntity criarProdutoEntityComIdDois() {
        return ProdutoEntity.builder()
                .id(2L)
                .nome("Mouse Logitech")
                .sku("SKU456")
                .preco(150.00)
                .build();
    }
}