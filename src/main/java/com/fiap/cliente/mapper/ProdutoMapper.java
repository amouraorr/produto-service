package com.fiap.cliente.mapper;

import com.fiap.cliente.domain.Produto;
import com.fiap.cliente.dto.ProdutoDTO;
import com.fiap.cliente.entity.ProdutoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoEntity toEntity(Produto produto);
    Produto toDomain(ProdutoEntity entity);
    ProdutoDTO toDTO(Produto produto);
    Produto toDomain(ProdutoDTO dto);
}
