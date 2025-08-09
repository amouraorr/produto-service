package com.fiap.produto.gateway;

import com.fiap.produto.domain.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoGateway {
    Produto salvar(Produto produto);
    Produto atualizar(Produto produto);
    Optional<Produto> buscarPorSku(String sku);
    Optional<Produto> buscarPorId(Long id);
    List<Produto> listarTodos();
}
