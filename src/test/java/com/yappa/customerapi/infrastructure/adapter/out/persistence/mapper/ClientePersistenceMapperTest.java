package com.yappa.customerapi.infrastructure.adapter.out.persistence.mapper;

import com.yappa.customerapi.TestFixtures;
import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClientePersistenceMapperTest {

    private ClientePersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ClientePersistenceMapper();
    }

    // ---- toDomain(ClienteEntity) ----

    @Test
    void toDomain_mapsTodosLosCampos() {
        ClienteEntity entity = TestFixtures.clienteEntityValida();

        Cliente cliente = mapper.toDomain(entity);

        assertThat(cliente.getId()).isEqualTo(TestFixtures.ID);
        assertThat(cliente.getNombre()).isEqualTo(TestFixtures.NOMBRE);
        assertThat(cliente.getApellido()).isEqualTo(TestFixtures.APELLIDO);
        assertThat(cliente.getRazonSocial()).isEqualTo(TestFixtures.RAZON_SOCIAL);
        assertThat(cliente.getCuit().value()).isEqualTo(TestFixtures.CUIT_VALUE);
        assertThat(cliente.getFechaNacimiento()).isEqualTo(TestFixtures.FECHA_NACIMIENTO);
        assertThat(cliente.getTelefonoCelular().value()).isEqualTo(TestFixtures.TELEFONO_VALUE);
        assertThat(cliente.getEmail().value()).isEqualTo(TestFixtures.EMAIL_VALUE);
        assertThat(cliente.getFechaCreacion()).isEqualTo(TestFixtures.FECHA_CREACION);
        assertThat(cliente.getFechaModificacion()).isEqualTo(TestFixtures.FECHA_MODIFICACION);
    }

    // ---- toEntity(Cliente) ----

    @Test
    void toEntity_mapsTodosLosCamposDominio() {
        Cliente cliente = TestFixtures.clienteValido();

        ClienteEntity entity = mapper.toEntity(cliente);

        assertThat(entity.getId()).isEqualTo(TestFixtures.ID);
        assertThat(entity.getNombre()).isEqualTo(TestFixtures.NOMBRE);
        assertThat(entity.getApellido()).isEqualTo(TestFixtures.APELLIDO);
        assertThat(entity.getRazonSocial()).isEqualTo(TestFixtures.RAZON_SOCIAL);
        assertThat(entity.getCuit()).isEqualTo(TestFixtures.CUIT_VALUE);
        assertThat(entity.getFechaNacimiento()).isEqualTo(TestFixtures.FECHA_NACIMIENTO);
        assertThat(entity.getTelefonoCelular()).isEqualTo(TestFixtures.TELEFONO_VALUE);
        assertThat(entity.getEmail()).isEqualTo(TestFixtures.EMAIL_VALUE);
    }

    @Test
    void toEntity_noMappeaTimestamps() {
        Cliente cliente = TestFixtures.clienteValido();

        ClienteEntity entity = mapper.toEntity(cliente);

        // Los timestamps son manejados por la DB (insertable=false, updatable=false)
        assertThat(entity.getFechaCreacion()).isNull();
        assertThat(entity.getFechaModificacion()).isNull();
    }

    @Test
    void toEntity_conIdNull_setIdNull() {
        Cliente clienteSinId = TestFixtures.clienteSinId();

        ClienteEntity entity = mapper.toEntity(clienteSinId);

        assertThat(entity.getId()).isNull();
    }
}
