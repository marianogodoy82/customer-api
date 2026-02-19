package com.yappa.customerapi.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CuitTest {

    @Test
    void cuitValida_noLanzaExcepcion() {
        Cuit cuit = new Cuit("20-12345678-9");
        assertThat(cuit.value()).isEqualTo("20-12345678-9");
    }

    @Test
    void cuitNull_lanzaIllegalArgumentException() {
        assertThatThrownBy(() -> new Cuit(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CUIT inválido");
    }

    @Test
    void cuitSinGuiones_lanzaExcepcion() {
        assertThatThrownBy(() -> new Cuit("20123456789"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cuitConLetras_lanzaExcepcion() {
        assertThatThrownBy(() -> new Cuit("AB-12345678-9"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cuitConDigitosInsuficientes_lanzaExcepcion() {
        assertThatThrownBy(() -> new Cuit("2-12345678-9"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cuitConDigitosExcesivos_lanzaExcepcion() {
        assertThatThrownBy(() -> new Cuit("200-12345678-9"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cuitConDosDigitosFinales_lanzaExcepcion() {
        assertThatThrownBy(() -> new Cuit("20-12345678-99"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cuitVacia_lanzaExcepcion() {
        assertThatThrownBy(() -> new Cuit(""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
