package com.yappa.customerapi.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    @Test
    void openAPI_retornaBeanNoNull() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI).isNotNull();
    }

    @Test
    void openAPI_tieneTituloEsperado() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).contains("Customers");
    }

    @Test
    void openAPI_tieneVersionEsperada() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
    }
}
