package com.fiap.cliente.usecase;

import com.fiap.cliente.domain.Produto;
import com.fiap.cliente.gateway.ProdutoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuscarProdutoPorSkuUseCase {

    private final ProdutoGateway gateway;

    public Optional<Produto> execute(String sku) {
        return gateway.buscarPorSku(sku);
    }
}
