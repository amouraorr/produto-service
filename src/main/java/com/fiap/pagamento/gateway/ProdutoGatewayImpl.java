package com.fiap.pagamento.gateway;

import com.fiap.pagamento.domain.Produto;
import com.fiap.pagamento.entity.ProdutoEntity;
import com.fiap.pagamento.mapper.ProdutoMapper;
import com.fiap.pagamento.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProdutoGatewayImpl implements ProdutoGateway {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    @Override
    public Produto salvar(Produto produto) {
        // Salva produto no banco de dados
        ProdutoEntity entity = mapper.toEntity(produto);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Produto atualizar(Produto produto) {
        // Atualiza produto no banco de dados
        ProdutoEntity entity = mapper.toEntity(produto);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Produto> buscarPorSku(String sku) {
        // Busca produto por SKU no banco de dados
        return repository.findBySku(sku).map(mapper::toDomain);
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        // Busca produto por ID no banco de dados
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Produto> listarTodos() {
        // Lista todos os produtos do banco de dados
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
