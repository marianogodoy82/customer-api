package com.yappa.customerapi.infrastructure.adapter.in.rest.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record ClienteResponse(
        Long id,
        String nombre,
        String apellido,
        String razonSocial,
        String cuit,
        LocalDate fechaNacimiento,
        String telefonoCelular,
        String email,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaModificacion
) {}
