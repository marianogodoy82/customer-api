package com.yappa.customerapi.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClienteCreateRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String apellido,
        @NotBlank @Size(max = 150) String razonSocial,
        @NotBlank @Pattern(regexp = "^[0-9]{2}-[0-9]{8}-[0-9]{1}$", message = "CUIT inválido (20-12345678-9)") String cuit,
        @NotNull LocalDate fechaNacimiento,
        @NotBlank @Size(max = 30) String telefonoCelular,
        @NotBlank @Email @Size(max = 150) String email
) {}
