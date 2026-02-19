package com.yappa.customerapi.domain.model;

public record Email(String value) {

    private static final int MAX_LENGTH = 150;
    private static final String PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("El email no puede superar los " + MAX_LENGTH + " caracteres");
        }
        if (!value.matches(PATTERN)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
    }
}
