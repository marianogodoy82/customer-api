package com.yappa.customerapi.infrastructure.adapter.in.rest.mapper;

import com.yappa.customerapi.TestFixtures;
import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteCreateRequest;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteResponse;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteRestMapperTest {

    private ClienteRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ClienteRestMapper();
    }

    // ---- toDomain(ClienteCreateRequest) ----

    @Test
    void toDomain_createRequest_mapsTodosLosCampos() {
        ClienteCreateRequest req = TestFixtures.clienteCreateRequestValido();

        Cliente cliente = mapper.toDomain(req);

        assertThat(cliente.getNombre()).isEqualTo(TestFixtures.NOMBRE);
        assertThat(cliente.getApellido()).isEqualTo(TestFixtures.APELLIDO);
        assertThat(cliente.getRazonSocial()).isEqualTo(TestFixtures.RAZON_SOCIAL);
        assertThat(cliente.getCuit().value()).isEqualTo(TestFixtures.CUIT_VALUE);
        assertThat(cliente.getFechaNacimiento()).isEqualTo(TestFixtures.FECHA_NACIMIENTO);
        assertThat(cliente.getTelefonoCelular().value()).isEqualTo(TestFixtures.TELEFONO_VALUE);
        assertThat(cliente.getEmail().value()).isEqualTo(TestFixtures.EMAIL_VALUE);
        assertThat(cliente.getId()).isNull();
    }

    // ---- toDomain(ClienteUpdateRequest) ----

    @Test
    void toDomain_updateRequest_conTodosLosCampos_mapsCorrectamente() {
        ClienteUpdateRequest req = new ClienteUpdateRequest(
                "NuevoNombre", "NuevoApellido", "5555555555", "nuevo@test.com"
        );

        Cliente cliente = mapper.toDomain(req);

        assertThat(cliente.getNombre()).isEqualTo("NuevoNombre");
        assertThat(cliente.getApellido()).isEqualTo("NuevoApellido");
        assertThat(cliente.getTelefonoCelular().value()).isEqualTo("5555555555");
        assertThat(cliente.getEmail().value()).isEqualTo("nuevo@test.com");
        assertThat(cliente.getId()).isNull();
        assertThat(cliente.getCuit()).isNull();
        assertThat(cliente.getFechaNacimiento()).isNull();
    }

    @Test
    void toDomain_updateRequest_conTelefonoNull_mapsNullTelefono() {
        ClienteUpdateRequest req = new ClienteUpdateRequest(
                "NuevoNombre", null, null, "nuevo@test.com"
        );

        Cliente cliente = mapper.toDomain(req);

        assertThat(cliente.getNombre()).isEqualTo("NuevoNombre");
        assertThat(cliente.getTelefonoCelular()).isNull();
        assertThat(cliente.getEmail().value()).isEqualTo("nuevo@test.com");
    }

    @Test
    void toDomain_updateRequest_conEmailNull_mapsNullEmail() {
        ClienteUpdateRequest req = new ClienteUpdateRequest(
                null, "NuevoApellido", "6666666666", null
        );

        Cliente cliente = mapper.toDomain(req);

        assertThat(cliente.getApellido()).isEqualTo("NuevoApellido");
        assertThat(cliente.getTelefonoCelular().value()).isEqualTo("6666666666");
        assertThat(cliente.getEmail()).isNull();
    }

    // ---- toResponse(Cliente) ----

    @Test
    void toResponse_mapsTodosLosCamposDeCliente() {
        Cliente cliente = TestFixtures.clienteValido();

        ClienteResponse response = mapper.toResponse(cliente);

        assertThat(response.id()).isEqualTo(TestFixtures.ID);
        assertThat(response.nombre()).isEqualTo(TestFixtures.NOMBRE);
        assertThat(response.apellido()).isEqualTo(TestFixtures.APELLIDO);
        assertThat(response.razonSocial()).isEqualTo(TestFixtures.RAZON_SOCIAL);
        assertThat(response.cuit()).isEqualTo(TestFixtures.CUIT_VALUE);
        assertThat(response.fechaNacimiento()).isEqualTo(TestFixtures.FECHA_NACIMIENTO);
        assertThat(response.telefonoCelular()).isEqualTo(TestFixtures.TELEFONO_VALUE);
        assertThat(response.email()).isEqualTo(TestFixtures.EMAIL_VALUE);
        assertThat(response.fechaCreacion()).isEqualTo(TestFixtures.FECHA_CREACION);
        assertThat(response.fechaModificacion()).isEqualTo(TestFixtures.FECHA_MODIFICACION);
    }
}
