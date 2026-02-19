package com.yappa.customerapi.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void emailValido_noLanzaExcepcion() {
        Email email = new Email("juan@example.com");
        assertThat(email.value()).isEqualTo("juan@example.com");
    }

    @Test
    void emailNull_lanzaExcepcion() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email no puede estar vacío");
    }

    @Test
    void emailBlanco_lanzaExcepcion() {
        assertThatThrownBy(() -> new Email("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email no puede estar vacío");
    }

    @Test
    void emailVacio_lanzaExcepcion() {
        assertThatThrownBy(() -> new Email(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void emailMayorA150Caracteres_lanzaExcepcion() {
        String emailLargo = "a".repeat(140) + "@example.com"; // > 150 chars
        assertThatThrownBy(() -> new Email(emailLargo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("150 caracteres");
    }

    @Test
    void emailSinArroba_lanzaExcepcion() {
        assertThatThrownBy(() -> new Email("sinArroba.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Formato de email inválido");
    }

    @Test
    void emailSinDominio_lanzaExcepcion() {
        assertThatThrownBy(() -> new Email("usuario@"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void emailConEspacios_lanzaExcepcion() {
        assertThatThrownBy(() -> new Email("user name@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void emailExactamente150Caracteres_esValido() {
        // Construir email de exactamente 150 chars que sea válido
        // formato: X@Y.Z donde X+Y+Z+2 = 150
        String localPart = "a".repeat(100);
        String domain = "b".repeat(44);
        String email150 = localPart + "@" + domain + ".com"; // 100+1+44+1+3 = 149 chars? ajustar
        // local=100, @=1, domain=44, .=1, tld=3 => 149, agregar 1 más al local
        String emailExacto = "a".repeat(101) + "@" + "b".repeat(44) + ".com"; // 150 chars
        Email email = new Email(emailExacto);
        assertThat(email.value().length()).isEqualTo(150);
    }
}
