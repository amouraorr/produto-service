package com.fiap.produto.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PÓS GRADUAÇÃO - FIAP 2025 - SERVIÇO DE PRODUTO")
                        .version("1.0.0")
                        .description("Microsserviço responsável pelo cadastro, atualização, consulta e exclusão de produtos. Gerencia informações como nome, SKU e preço, garantindo a unicidade do SKU e a integridade dos dados dos produtos disponíveis para pedidos."));
    }
}