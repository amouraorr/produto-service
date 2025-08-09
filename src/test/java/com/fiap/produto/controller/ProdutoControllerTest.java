package com.fiap.produto.controller;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.dto.request.ProdutoRequestDTO;
import com.fiap.produto.dto.response.ProdutoResponseDTO;
import com.fiap.produto.mapper.ProdutoMapper;
import com.fiap.produto.usecase.service.AtualizarProdutoServiceUseCase;
import com.fiap.produto.usecase.service.BuscarProdutoPorSkuServiceUseCase;
import com.fiap.produto.usecase.service.CadastrarProdutoUseServiceCase;
import com.fiap.produto.usecase.service.ListarProdutosServiceUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private CadastrarProdutoUseServiceCase cadastrarUseCase;

    @Mock
    private AtualizarProdutoServiceUseCase atualizarUseCase;

    @Mock
    private BuscarProdutoPorSkuServiceUseCase buscarPorSkuUseCase;

    @Mock
    private ListarProdutosServiceUseCase listarUseCase;

    @Mock
    private ProdutoMapper mapper;

    @Test
    void deveCadastrarProdutoComSucesso() {
        var requestDTO = criarProdutoRequestDTO();
        var produto = criarProduto();
        var produtoSalvo = criarProdutoComId();
        var responseDTO = criarProdutoResponseDTO();

        when(mapper.toDomain(requestDTO)).thenReturn(produto);
        when(cadastrarUseCase.execute(produto)).thenReturn(produtoSalvo);
        when(mapper.toResponseDTO(produtoSalvo)).thenReturn(responseDTO);

        var response = produtoController.cadastrar(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(mapper).toDomain(requestDTO);
        verify(cadastrarUseCase).execute(produto);
        verify(mapper).toResponseDTO(produtoSalvo);
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        var id = 1L;
        var requestDTO = criarProdutoRequestDTO();
        var produto = criarProduto();
        var produtoAtualizado = criarProdutoComId();
        var responseDTO = criarProdutoResponseDTO();

        when(mapper.toDomain(requestDTO)).thenReturn(produto);
        when(atualizarUseCase.execute(any(Produto.class))).thenReturn(produtoAtualizado);
        when(mapper.toResponseDTO(produtoAtualizado)).thenReturn(responseDTO);

        var response = produtoController.atualizar(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(mapper).toDomain(requestDTO);
        verify(atualizarUseCase).execute(any(Produto.class));
        verify(mapper).toResponseDTO(produtoAtualizado);
    }

    @Test
    void deveBuscarProdutoPorSkuComSucesso() {
        var sku = "SKU123";
        var produto = criarProdutoComId();
        var responseDTO = criarProdutoResponseDTO();

        when(buscarPorSkuUseCase.execute(sku)).thenReturn(Optional.of(produto));
        when(mapper.toResponseDTO(produto)).thenReturn(responseDTO);

        var response = produtoController.buscarPorSku(sku);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(buscarPorSkuUseCase).execute(sku);
        verify(mapper).toResponseDTO(produto);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontradoPorSku() {
        var sku = "SKU_NAO_EXISTE";

        when(buscarPorSkuUseCase.execute(sku)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> produtoController.buscarPorSku(sku));

        assertEquals("Produto não encontrado para SKU: " + sku, exception.getMessage());
        verify(buscarPorSkuUseCase).execute(sku);
        verify(mapper, never()).toResponseDTO(any());
    }

    @Test
    void deveListarTodosProdutosComSucesso() {
        var produtos = Arrays.asList(criarProdutoComId(), criarProdutoComId());
        var responseDTO1 = criarProdutoResponseDTO();
        var responseDTO2 = criarProdutoResponseDTODois();

        when(listarUseCase.execute()).thenReturn(produtos);
        when(mapper.toResponseDTO(produtos.get(0))).thenReturn(responseDTO1);
        when(mapper.toResponseDTO(produtos.get(1))).thenReturn(responseDTO2);

        var response = produtoController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(listarUseCase).execute();
        verify(mapper, times(2)).toResponseDTO(any(Produto.class));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverProdutos() {
        when(listarUseCase.execute()).thenReturn(List.of());

        var response = produtoController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(listarUseCase).execute();
        verify(mapper, never()).toResponseDTO(any());
    }

    @Test
    void deveDefinirIdDoProdutoAoAtualizar() {
        var id = 5L;
        var requestDTO = criarProdutoRequestDTO();
        var produto = criarProduto();
        var produtoAtualizado = criarProdutoComId();
        var responseDTO = criarProdutoResponseDTO();

        when(mapper.toDomain(requestDTO)).thenReturn(produto);
        when(atualizarUseCase.execute(any(Produto.class))).thenReturn(produtoAtualizado);
        when(mapper.toResponseDTO(produtoAtualizado)).thenReturn(responseDTO);

        produtoController.atualizar(id, requestDTO);

        ArgumentCaptor<Produto> produtoCaptor = ArgumentCaptor.forClass(Produto.class);
        verify(atualizarUseCase).execute(produtoCaptor.capture());

        assertEquals(id, produtoCaptor.getValue().getId());
    }

    private ProdutoRequestDTO criarProdutoRequestDTO() {
        return ProdutoRequestDTO.builder()
                .nome("Notebook Dell")
                .sku("SKU123")
                .preco(2500.00)
                .build();
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

    private ProdutoResponseDTO criarProdutoResponseDTO() {
        return ProdutoResponseDTO.builder()
                .id(1L)
                .nome("Notebook Dell")
                .sku("SKU123")
                .preco(2500.00)
                .build();
    }

    private ProdutoResponseDTO criarProdutoResponseDTODois() {
        return ProdutoResponseDTO.builder()
                .id(2L)
                .nome("Mouse Logitech")
                .sku("SKU456")
                .preco(150.00)
                .build();
    }
}