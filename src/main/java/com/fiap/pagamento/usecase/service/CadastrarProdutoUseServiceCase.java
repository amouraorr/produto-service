package com.fiap.pagamento.usecase.service;

import com.fiap.pagamento.domain.Produto;
import com.fiap.pagamento.gateway.ProdutoGateway;
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