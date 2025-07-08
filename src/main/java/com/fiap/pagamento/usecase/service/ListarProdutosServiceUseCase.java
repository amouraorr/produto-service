package com.fiap.pagamento.usecase.service;

import com.fiap.pagamento.domain.Produto;
import com.fiap.pagamento.gateway.ProdutoGateway;
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
