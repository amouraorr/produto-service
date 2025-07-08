package com.fiap.pagamento.usecase.service;

import com.fiap.pagamento.domain.Produto;
import com.fiap.pagamento.gateway.ProdutoGateway;
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
