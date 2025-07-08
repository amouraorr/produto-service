package com.fiap.pagamento.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDTO {
    private Long id;
    private String nome;
    private String sku;
    private Double preco;
}
