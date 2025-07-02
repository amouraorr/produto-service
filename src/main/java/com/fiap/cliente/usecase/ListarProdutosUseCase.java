package com.fiap.cliente.usecase;

import com.fiap.cliente.domain.Produto;
import com.fiap.cliente.gateway.ProdutoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarProdutosUseCase {

    private final ProdutoGateway gateway;

    public List<Produto> execute() {
        return gateway.listarTodos();
    }
}
