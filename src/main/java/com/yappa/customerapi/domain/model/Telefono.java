package com.yappa.customerapi.domain.model;

public record Telefono(String value) {

    private static final int MAX_LENGTH = 30;

    public Telefono {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("El teléfono no puede superar los " + MAX_LENGTH + " caracteres");
        }
    }
}
