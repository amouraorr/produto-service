package com.fiap.produto.usecase.service;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.gateway.ProdutoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarProdutosServiceUseCase {

    private final ProdutoGateway gateway;

    public List<Produto> execute() {
        return gateway.listarTodos();
    }
}
