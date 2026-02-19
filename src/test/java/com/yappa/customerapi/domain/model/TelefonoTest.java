package com.yappa.customerapi.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelefonoTest {

    @Test
    void telefonoValido_noLanzaExcepcion() {
        Telefono telefono = new Telefono("+54-11-1234-5678");
        assertThat(telefono.value()).isEqualTo("+54-11-1234-5678");
    }

    @Test
    void telefonoNull_lanzaExcepcion() {
        assertThatThrownBy(() -> new Telefono(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("teléfono no puede estar vacío");
    }

    @Test
    void telefonoBlanco_lanzaExcepcion() {
        assertThatThrownBy(() -> new Telefono("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("teléfono no puede estar vacío");
    }

    @Test
    void telefonoVacio_lanzaExcepcion() {
        assertThatThrownBy(() -> new Telefono(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void telefonoMayorA30Caracteres_lanzaExcepcion() {
        String telefonoLargo = "1".repeat(31);
        assertThatThrownBy(() -> new Telefono(telefonoLargo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("30 caracteres");
    }

    @Test
    void telefonoExactamente30Caracteres_esValido() {
        String telefono30 = "1".repeat(30);
        Telefono telefono = new Telefono(telefono30);
        assertThat(telefono.value().length()).isEqualTo(30);
    }
}
