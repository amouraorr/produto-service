package com.fiap.produto.usecase.service;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.gateway.ProdutoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarProdutoUseServiceCase {

    private final ProdutoGateway gateway;

    public Produto execute(Produto produto) {
        gateway.buscarPorSku(produto.getSku()).ifPresent(p -> {
            throw new IllegalArgumentException("SKU já cadastrado!");
        });
        return gateway.salvar(produto);
    }
}