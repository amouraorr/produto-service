package com.fiap.produto.usecase.service;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.gateway.ProdutoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarProdutoServiceUseCase {

    private final ProdutoGateway gateway;

    public Produto execute(Produto produto) {
        return gateway.atualizar(produto);
    }
}
