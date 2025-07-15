package com.fiap.produto.usecase.service;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.gateway.ProdutoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuscarProdutoPorSkuServiceUseCase {

    private final ProdutoGateway gateway;

    public Optional<Produto> execute(String sku) {
        return gateway.buscarPorSku(sku);
    }
}
