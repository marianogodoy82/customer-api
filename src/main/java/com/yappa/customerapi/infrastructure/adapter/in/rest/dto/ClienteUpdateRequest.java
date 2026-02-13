package com.yappa.customerapi.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequest(
        @Size(max = 100) String nombre,
        @Size(max = 100) String apellido,
        @Size(max = 30) String telefonoCelular,
        @Email @Size(max = 150) String email
) {
    public boolean isEmpty() {
        return nombre == null && apellido == null && telefonoCelular == null && email == null;
    }
}
