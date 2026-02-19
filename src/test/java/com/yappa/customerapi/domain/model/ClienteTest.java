package com.yappa.customerapi.domain.model;

import com.yappa.customerapi.TestFixtures;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClienteTest {

    // ---- crear() ----

    @Test
    void crear_conDatosValidos_retornaCliente() {
        Cliente cliente = Cliente.crear(
                "Juan", "Pérez", "Juan Pérez SRL",
                new Cuit("20-12345678-9"),
                LocalDate.of(1990, 1, 1),
                new Telefono("1234567890"),
                new Email("juan@example.com")
        );

        assertThat(cliente.getNombre()).isEqualTo("Juan");
        assertThat(cliente.getApellido()).isEqualTo("Pérez");
        assertThat(cliente.getRazonSocial()).isEqualTo("Juan Pérez SRL");
        assertThat(cliente.getCuit().value()).isEqualTo("20-12345678-9");
        assertThat(cliente.getFechaNacimiento()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(cliente.getTelefonoCelular().value()).isEqualTo("1234567890");
        assertThat(cliente.getEmail().value()).isEqualTo("juan@example.com");
        assertThat(cliente.getId()).isNull();
        assertThat(cliente.getFechaCreacion()).isNull();
        assertThat(cliente.getFechaModificacion()).isNull();
    }

    @Test
    void crear_conNombreNull_lanzaExcepcion() {
        assertThatThrownBy(() -> Cliente.crear(
                null, "Pérez", "Empresa",
                new Cuit("20-12345678-9"), LocalDate.now(),
                new Telefono("123"), new Email("a@b.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void crear_conNombreBlanco_lanzaExcepcion() {
        assertThatThrownBy(() -> Cliente.crear(
                "   ", "Pérez", "Empresa",
                new Cuit("20-12345678-9"), LocalDate.now(),
                new Telefono("123"), new Email("a@b.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void crear_conApellidoNull_lanzaExcepcion() {
        assertThatThrownBy(() -> Cliente.crear(
                "Juan", null, "Empresa",
                new Cuit("20-12345678-9"), LocalDate.now(),
                new Telefono("123"), new Email("a@b.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("apellido");
    }

    @Test
    void crear_conApellidoBlanco_lanzaExcepcion() {
        assertThatThrownBy(() -> Cliente.crear(
                "Juan", "  ", "Empresa",
                new Cuit("20-12345678-9"), LocalDate.now(),
                new Telefono("123"), new Email("a@b.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("apellido");
    }

    @Test
    void crear_conRazonSocialNull_lanzaExcepcion() {
        assertThatThrownBy(() -> Cliente.crear(
                "Juan", "Pérez", null,
                new Cuit("20-12345678-9"), LocalDate.now(),
                new Telefono("123"), new Email("a@b.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("razón social");
    }

    @Test
    void crear_conRazonSocialBlanca_lanzaExcepcion() {
        assertThatThrownBy(() -> Cliente.crear(
                "Juan", "Pérez", "  ",
                new Cuit("20-12345678-9"), LocalDate.now(),
                new Telefono("123"), new Email("a@b.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("razón social");
    }

    @Test
    void crear_conFechaNacimientoNull_lanzaExcepcion() {
        assertThatThrownBy(() -> Cliente.crear(
                "Juan", "Pérez", "Empresa",
                new Cuit("20-12345678-9"), null,
                new Telefono("123"), new Email("a@b.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fecha de nacimiento");
    }

    // ---- reconstituir() ----

    @Test
    void reconstituir_asignaTodosLosCampos() {
        OffsetDateTime creacion = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        OffsetDateTime modificacion = OffsetDateTime.parse("2024-06-01T00:00:00Z");

        Cliente cliente = Cliente.reconstituir(
                99L, "Ana", "Gómez", "Ana SRL",
                new Cuit("30-98765432-1"),
                LocalDate.of(1985, 3, 20),
                new Telefono("987654321"),
                new Email("ana@test.com"),
                creacion,
                modificacion
        );

        assertThat(cliente.getId()).isEqualTo(99L);
        assertThat(cliente.getNombre()).isEqualTo("Ana");
        assertThat(cliente.getApellido()).isEqualTo("Gómez");
        assertThat(cliente.getRazonSocial()).isEqualTo("Ana SRL");
        assertThat(cliente.getCuit().value()).isEqualTo("30-98765432-1");
        assertThat(cliente.getFechaNacimiento()).isEqualTo(LocalDate.of(1985, 3, 20));
        assertThat(cliente.getTelefonoCelular().value()).isEqualTo("987654321");
        assertThat(cliente.getEmail().value()).isEqualTo("ana@test.com");
        assertThat(cliente.getFechaCreacion()).isEqualTo(creacion);
        assertThat(cliente.getFechaModificacion()).isEqualTo(modificacion);
    }

    // ---- actualizarNombre() ----

    @Test
    void actualizarNombre_conValorValido_actualizaElCampo() {
        Cliente cliente = TestFixtures.clienteValido();
        cliente.actualizarNombre("NuevoNombre");
        assertThat(cliente.getNombre()).isEqualTo("NuevoNombre");
    }

    @Test
    void actualizarNombre_conNull_lanzaExcepcion() {
        Cliente cliente = TestFixtures.clienteValido();
        assertThatThrownBy(() -> cliente.actualizarNombre(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void actualizarNombre_conBlanco_lanzaExcepcion() {
        Cliente cliente = TestFixtures.clienteValido();
        assertThatThrownBy(() -> cliente.actualizarNombre("  "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ---- actualizarApellido() ----

    @Test
    void actualizarApellido_conValorValido_actualizaElCampo() {
        Cliente cliente = TestFixtures.clienteValido();
        cliente.actualizarApellido("NuevoApellido");
        assertThat(cliente.getApellido()).isEqualTo("NuevoApellido");
    }

    @Test
    void actualizarApellido_conNull_lanzaExcepcion() {
        Cliente cliente = TestFixtures.clienteValido();
        assertThatThrownBy(() -> cliente.actualizarApellido(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("apellido");
    }

    // ---- actualizarTelefono() ----

    @Test
    void actualizarTelefono_conValorValido_actualizaElCampo() {
        Cliente cliente = TestFixtures.clienteValido();
        Telefono nuevoTelefono = new Telefono("9999999999");
        cliente.actualizarTelefono(nuevoTelefono);
        assertThat(cliente.getTelefonoCelular().value()).isEqualTo("9999999999");
    }

    @Test
    void actualizarTelefono_conNull_lanzaExcepcion() {
        Cliente cliente = TestFixtures.clienteValido();
        assertThatThrownBy(() -> cliente.actualizarTelefono(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("teléfono no puede ser nulo");
    }

    // ---- actualizarEmail() ----

    @Test
    void actualizarEmail_conValorValido_actualizaElCampo() {
        Cliente cliente = TestFixtures.clienteValido();
        Email nuevoEmail = new Email("nuevo@example.com");
        cliente.actualizarEmail(nuevoEmail);
        assertThat(cliente.getEmail().value()).isEqualTo("nuevo@example.com");
    }

    @Test
    void actualizarEmail_conNull_lanzaExcepcion() {
        Cliente cliente = TestFixtures.clienteValido();
        assertThatThrownBy(() -> cliente.actualizarEmail(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email no puede ser nulo");
    }
}
