package com.fiap.produto.controller;

import com.fiap.produto.dto.request.ProdutoRequestDTO;
import com.fiap.produto.dto.response.ProdutoResponseDTO;
import com.fiap.produto.mapper.ProdutoMapper;
import com.fiap.produto.usecase.service.AtualizarProdutoServiceUseCase;
import com.fiap.produto.usecase.service.BuscarProdutoPorSkuServiceUseCase;
import com.fiap.produto.usecase.service.CadastrarProdutoUseServiceCase;
import com.fiap.produto.usecase.service.ListarProdutosServiceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final CadastrarProdutoUseServiceCase cadastrarUseCase;
    private final AtualizarProdutoServiceUseCase atualizarUseCase;
    private final BuscarProdutoPorSkuServiceUseCase buscarPorSkuUseCase;
    private final ListarProdutosServiceUseCase listarUseCase;
    private final ProdutoMapper mapper;

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoRequestDTO dto) {
        log.info("Iniciando cadastro de produto com SKU: {}", dto.getSku());
        var produto = mapper.toDomain(dto);
        var produtoSalvo = cadastrarUseCase.execute(produto);
        log.info("Produto cadastrado com sucesso, ID: {}", produtoSalvo.getId());
        return ResponseEntity.ok(mapper.toResponseDTO(produtoSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        log.info("Iniciando atualização do produto ID: {}", id);
        var produto = mapper.toDomain(dto);
        produto.setId(id);
        var produtoAtualizado = atualizarUseCase.execute(produto);
        log.info("Produto atualizado com sucesso, ID: {}", produtoAtualizado.getId());
        return ResponseEntity.ok(mapper.toResponseDTO(produtoAtualizado));
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorSku(@PathVariable String sku) {
        log.info("Buscando produto por SKU: {}", sku);
        var produtoOpt = buscarPorSkuUseCase.execute(sku);
        return produtoOpt.map(produto -> {
            log.info("Produto encontrado para SKU: {}", sku);
            return ResponseEntity.ok(mapper.toResponseDTO(produto));
        }).orElseThrow(() -> {
            log.warn("Produto não encontrado para SKU: {}", sku);
            return new IllegalArgumentException("Produto não encontrado para SKU: " + sku);
        });
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        log.info("Listando todos os produtos");
        var produtos = listarUseCase.execute().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
        log.info("Total de produtos encontrados: {}", produtos.size());
        return ResponseEntity.ok(produtos);
    }
}

