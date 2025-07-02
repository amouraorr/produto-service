package com.fiap.cliente.usecase;

import com.fiap.cliente.domain.Produto;
import com.fiap.cliente.gateway.ProdutoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtualizarProdutoUseCase {

    private final ProdutoGateway gateway;

    public Produto execute(Produto produto) {
        // Adicionar validações, como garantir que o produto existe antes de atualizar
        return gateway.atualizar(produto);
    }
}
