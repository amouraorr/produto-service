package com.fiap.produto.mapper;

import com.fiap.produto.domain.Produto;
import com.fiap.produto.dto.request.ProdutoRequestDTO;
import com.fiap.produto.dto.response.ProdutoResponseDTO;
import com.fiap.produto.entity.ProdutoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoEntity toEntity(Produto produto);
    Produto toDomain(ProdutoEntity entity);
    ProdutoResponseDTO toResponseDTO(Produto produto);
    Produto toDomain(ProdutoRequestDTO dto);
}
