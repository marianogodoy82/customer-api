package com.yappa.customerapi.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteNotFoundExceptionTest {

    @Test
    void constructor_conMensaje_guardaMensaje() {
        ClienteNotFoundException ex = new ClienteNotFoundException("Cliente no encontrado");
        assertThat(ex.getMessage()).isEqualTo("Cliente no encontrado");
    }

    @Test
    void esRuntimeException() {
        ClienteNotFoundException ex = new ClienteNotFoundException("test");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }
}
