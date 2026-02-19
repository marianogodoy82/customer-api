package com.yappa.customerapi.domain.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Cliente {

    private Long id;
    private String nombre;
    private String apellido;
    private String razonSocial;
    private Cuit cuit;
    private LocalDate fechaNacimiento;
    private Telefono telefonoCelular;
    private Email email;
    private OffsetDateTime fechaCreacion;
    private OffsetDateTime fechaModificacion;

    private Cliente() {}

    public static Cliente crear(String nombre, String apellido, String razonSocial,
                                Cuit cuit, LocalDate fechaNacimiento,
                                Telefono telefonoCelular, Email email) {
        validarNoBlank(nombre, "nombre");
        validarNoBlank(apellido, "apellido");
        validarNoBlank(razonSocial, "razón social");
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }

        Cliente cliente = new Cliente();
        cliente.nombre = nombre;
        cliente.apellido = apellido;
        cliente.razonSocial = razonSocial;
        cliente.cuit = cuit;
        cliente.fechaNacimiento = fechaNacimiento;
        cliente.telefonoCelular = telefonoCelular;
        cliente.email = email;
        return cliente;
    }

    public static Cliente reconstituir(Long id, String nombre, String apellido, String razonSocial,
                                       Cuit cuit, LocalDate fechaNacimiento,
                                       Telefono telefonoCelular, Email email,
                                       OffsetDateTime fechaCreacion, OffsetDateTime fechaModificacion) {
        Cliente cliente = new Cliente();
        cliente.id = id;
        cliente.nombre = nombre;
        cliente.apellido = apellido;
        cliente.razonSocial = razonSocial;
        cliente.cuit = cuit;
        cliente.fechaNacimiento = fechaNacimiento;
        cliente.telefonoCelular = telefonoCelular;
        cliente.email = email;
        cliente.fechaCreacion = fechaCreacion;
        cliente.fechaModificacion = fechaModificacion;
        return cliente;
    }

    public void actualizarNombre(String nombre) {
        validarNoBlank(nombre, "nombre");
        this.nombre = nombre;
    }

    public void actualizarApellido(String apellido) {
        validarNoBlank(apellido, "apellido");
        this.apellido = apellido;
    }

    public void actualizarTelefono(Telefono telefonoCelular) {
        if (telefonoCelular == null) {
            throw new IllegalArgumentException("El teléfono no puede ser nulo");
        }
        this.telefonoCelular = telefonoCelular;
    }

    public void actualizarEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("El email no puede ser nulo");
        }
        this.email = email;
    }

    private static void validarNoBlank(String value, String campo) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " no puede estar vacío");
        }
    }

    // Getters (sin setters publicos)

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getRazonSocial() { return razonSocial; }
    public Cuit getCuit() { return cuit; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public Telefono getTelefonoCelular() { return telefonoCelular; }
    public Email getEmail() { return email; }
    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public OffsetDateTime getFechaModificacion() { return fechaModificacion; }
}
