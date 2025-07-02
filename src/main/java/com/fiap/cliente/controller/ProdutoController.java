package com.fiap.cliente.controller;

import com.fiap.cliente.dto.ProdutoDTO;
import com.fiap.cliente.mapper.ProdutoMapper;
import com.fiap.cliente.usecase.AtualizarProdutoUseCase;
import com.fiap.cliente.usecase.BuscarProdutoPorSkuUseCase;
import com.fiap.cliente.usecase.CadastrarProdutoUseCase;
import com.fiap.cliente.usecase.ListarProdutosUseCase;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final CadastrarProdutoUseCase cadastrarUseCase;
    private final AtualizarProdutoUseCase atualizarUseCase;
    private final BuscarProdutoPorSkuUseCase buscarPorSkuUseCase;
    private final ListarProdutosUseCase listarUseCase;
    private final ProdutoMapper mapper;

    @PostMapping
    public ProdutoDTO cadastrar(@RequestBody ProdutoDTO dto) {
        var produto = mapper.toDomain(dto);
        return mapper.toDTO(cadastrarUseCase.execute(produto));
    }

    @PutMapping("/{id}")
    public ProdutoDTO atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dto) {
        var produto = mapper.toDomain(dto);
        produto.setId(id);
        return mapper.toDTO(atualizarUseCase.execute(produto));
    }

    @GetMapping("/{sku}")
    public ProdutoDTO buscarPorSku(@PathVariable String sku) {
        return buscarPorSkuUseCase.execute(sku)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @GetMapping
    public List<ProdutoDTO> listar() {
        return listarUseCase.execute().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
