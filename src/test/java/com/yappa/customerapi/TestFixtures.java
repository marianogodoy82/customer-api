package com.yappa.customerapi;

import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.model.Cuit;
import com.yappa.customerapi.domain.model.Email;
import com.yappa.customerapi.domain.model.Telefono;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteCreateRequest;
import com.yappa.customerapi.infrastructure.adapter.out.persistence.entity.ClienteEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public final class TestFixtures {

    private TestFixtures() {}

    public static final Long ID = 1L;
    public static final String NOMBRE = "Juan";
    public static final String APELLIDO = "Pérez";
    public static final String RAZON_SOCIAL = "Juan Pérez SRL";
    public static final String CUIT_VALUE = "20-12345678-9";
    public static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1990, 5, 15);
    public static final String TELEFONO_VALUE = "+54-11-1234-5678";
    public static final String EMAIL_VALUE = "juan@example.com";
    public static final OffsetDateTime FECHA_CREACION = OffsetDateTime.parse("2024-01-01T10:00:00Z");
    public static final OffsetDateTime FECHA_MODIFICACION = OffsetDateTime.parse("2024-06-01T10:00:00Z");

    public static Cliente clienteValido() {
        return Cliente.reconstituir(
                ID,
                NOMBRE,
                APELLIDO,
                RAZON_SOCIAL,
                new Cuit(CUIT_VALUE),
                FECHA_NACIMIENTO,
                new Telefono(TELEFONO_VALUE),
                new Email(EMAIL_VALUE),
                FECHA_CREACION,
                FECHA_MODIFICACION
        );
    }

    public static Cliente clienteSinId() {
        return Cliente.crear(
                NOMBRE,
                APELLIDO,
                RAZON_SOCIAL,
                new Cuit(CUIT_VALUE),
                FECHA_NACIMIENTO,
                new Telefono(TELEFONO_VALUE),
                new Email(EMAIL_VALUE)
        );
    }

    public static ClienteEntity clienteEntityValida() {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(ID);
        entity.setNombre(NOMBRE);
        entity.setApellido(APELLIDO);
        entity.setRazonSocial(RAZON_SOCIAL);
        entity.setCuit(CUIT_VALUE);
        entity.setFechaNacimiento(FECHA_NACIMIENTO);
        entity.setTelefonoCelular(TELEFONO_VALUE);
        entity.setEmail(EMAIL_VALUE);
        entity.setFechaCreacion(FECHA_CREACION);
        entity.setFechaModificacion(FECHA_MODIFICACION);
        return entity;
    }

    public static ClienteCreateRequest clienteCreateRequestValido() {
        return new ClienteCreateRequest(
                NOMBRE,
                APELLIDO,
                RAZON_SOCIAL,
                CUIT_VALUE,
                FECHA_NACIMIENTO,
                TELEFONO_VALUE,
                EMAIL_VALUE
        );
    }
}
