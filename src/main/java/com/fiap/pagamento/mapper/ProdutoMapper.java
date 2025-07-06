package com.fiap.pagamento.mapper;

import com.fiap.pagamento.domain.Produto;
import com.fiap.pagamento.dto.request.ProdutoRequestDTO;
import com.fiap.pagamento.dto.response.ProdutoResponseDTO;
import com.fiap.pagamento.entity.ProdutoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoEntity toEntity(Produto produto);
    Produto toDomain(ProdutoEntity entity);
    ProdutoResponseDTO toResponseDTO(Produto produto);
    Produto toDomain(ProdutoRequestDTO dto);
}
