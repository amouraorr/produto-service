package com.fiap.produto.gateway;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.entity.ProdutoEntity;
import com.fiap.produto.mapper.ProdutoMapper;
import com.fiap.produto.repository.ProdutoRepository;
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
        ProdutoEntity entity = mapper.toEntity(produto);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Produto atualizar(Produto produto) {
        ProdutoEntity entity = mapper.toEntity(produto);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Produto> buscarPorSku(String sku) {
        return repository.findBySku(sku).map(mapper::toDomain);
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Produto> listarTodos() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
