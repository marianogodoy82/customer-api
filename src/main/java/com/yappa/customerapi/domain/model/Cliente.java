package com.yappa.customerapi.domain.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Cliente {

    private Long id;
    private String nombre;
    private String apellido;
    private String razonSocial;
    private String cuit;
    private LocalDate fechaNacimiento;
    private String telefonoCelular;
    private String email;
    private OffsetDateTime fechaCreacion;
    private OffsetDateTime fechaModificacion;

    public Cliente() {}

    public Cliente(Long id, String nombre, String apellido, String razonSocial, String cuit,
                   LocalDate fechaNacimiento, String telefonoCelular, String email,
                   OffsetDateTime fechaCreacion, OffsetDateTime fechaModificacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.fechaNacimiento = fechaNacimiento;
        this.telefonoCelular = telefonoCelular;
        this.email = email;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefonoCelular() { return telefonoCelular; }
    public void setTelefonoCelular(String telefonoCelular) { this.telefonoCelular = telefonoCelular; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(OffsetDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public OffsetDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(OffsetDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }
}
