package com.yappa.customerapi.domain.model;

public record Cuit(String value) {

    private static final String PATTERN = "^[0-9]{2}-[0-9]{8}-[0-9]$";

    public Cuit {
        if (value == null || !value.matches(PATTERN)) {
            throw new IllegalArgumentException("CUIT inválido, formato esperado: XX-XXXXXXXX-X");
        }
    }
}
