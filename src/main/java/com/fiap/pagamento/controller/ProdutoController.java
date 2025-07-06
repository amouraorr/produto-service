package com.fiap.pagamento.controller;

import com.fiap.pagamento.dto.request.ProdutoRequestDTO;
import com.fiap.pagamento.dto.response.ProdutoResponseDTO;
import com.fiap.pagamento.mapper.ProdutoMapper;
import com.fiap.pagamento.usecase.service.AtualizarProdutoServiceUseCase;
import com.fiap.pagamento.usecase.service.BuscarProdutoPorSkuServiceUseCase;
import com.fiap.pagamento.usecase.service.CadastrarProdutoUseServiceCase;
import com.fiap.pagamento.usecase.service.ListarProdutosServiceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ProdutoResponseDTO cadastrar(@RequestBody ProdutoRequestDTO dto) {
        // Endpoint para cadastrar novo produto via REST
        var produto = mapper.toDomain(dto);
        return mapper.toResponseDTO(cadastrarUseCase.execute(produto));
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto) {
        // Endpoint para atualizar produto via REST
        var produto = mapper.toDomain(dto);
        produto.setId(id);
        return mapper.toResponseDTO(atualizarUseCase.execute(produto));
    }

    @GetMapping("/{sku}")
    public ProdutoResponseDTO buscarPorSku(@PathVariable String sku) {
        // Endpoint para buscar produto por SKU via REST
        return buscarPorSkuUseCase.execute(sku)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @GetMapping
    public List<ProdutoResponseDTO> listar() {
        // Endpoint para listar todos os produtos via REST
        return listarUseCase.execute().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }
}

