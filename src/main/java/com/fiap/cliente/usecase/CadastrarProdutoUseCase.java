package com.fiap.cliente.usecase;

import com.fiap.cliente.domain.Produto;
import com.fiap.cliente.gateway.ProdutoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarProdutoUseCase {

    private final ProdutoGateway gateway;

    public Produto execute(Produto produto) {
        gateway.buscarPorSku(produto.getSku()).ifPresent(p -> {
            throw new IllegalArgumentException("SKU já cadastrado!");
        });
        return gateway.salvar(produto);
    }
}