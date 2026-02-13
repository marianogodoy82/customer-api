package com.yappa.customerapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientesApiIT {

    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("yappa")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void start() {
        postgres.start();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    int port;

    final TestRestTemplate rest = new TestRestTemplate();

    @Test
    void health() {
        var res = rest.getForEntity("http://localhost:" + port + "/api/clientes", String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void create_get_update_delete() {
        String base = "http://localhost:" + port + "/api/clientes";

        var createBody = Map.of(
                "nombre", "Ana",
                "apellido", "Ruiz",
                "razonSocial", "AR Servicios",
                "cuit", "27-11112222-3",
                "fechaNacimiento", "1995-02-10",
                "telefonoCelular", "1160000000",
                "email", "ana.ruiz@example.com"
        );

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var created = rest.postForEntity(base, new HttpEntity<>(createBody, headers), Map.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long id = ((Number) created.getBody().get("id")).longValue();

        var got = rest.getForEntity(base + "/" + id, Map.class);
        assertThat(got.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(got.getBody().get("email")).isEqualTo("ana.ruiz@example.com");

        var updBody = Map.of("nombre", "Ana Updated");
        var upd = rest.exchange(base + "/" + id, HttpMethod.PUT, new HttpEntity<>(updBody, headers), Map.class);
        assertThat(upd.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(upd.getBody().get("nombre")).isEqualTo("Ana Updated");

        var del = rest.exchange(base + "/" + id, HttpMethod.DELETE, new HttpEntity<>(null, headers), Void.class);
        assertThat(del.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var after = rest.getForEntity(base + "/" + id, Map.class);
        assertThat(after.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void search_uses_function() {
        String url = "http://localhost:" + port + "/api/clientes/search?q=an";
        var res = rest.getForEntity(url, Object.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
