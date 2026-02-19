package com.yappa.customerapi.application.service;

import com.yappa.customerapi.TestFixtures;
import com.yappa.customerapi.domain.exception.ClienteNotFoundException;
import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.model.Email;
import com.yappa.customerapi.domain.model.Telefono;
import com.yappa.customerapi.domain.port.out.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    private ClienteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClienteServiceImpl(clienteRepository);
    }

    // ---- getAll() ----

    @Test
    void getAll_retornaListaDelRepositorio() {
        List<Cliente> lista = List.of(TestFixtures.clienteValido());
        when(clienteRepository.findAll()).thenReturn(lista);

        List<Cliente> resultado = service.getAll();

        assertThat(resultado).isEqualTo(lista);
        verify(clienteRepository).findAll();
    }

    @Test
    void getAll_cuandoRepositorioRetornaVacio_retornaListaVacia() {
        when(clienteRepository.findAll()).thenReturn(List.of());

        List<Cliente> resultado = service.getAll();

        assertThat(resultado).isEmpty();
    }

    // ---- getById() ----

    @Test
    void getById_clienteExiste_retornaCliente() {
        Cliente cliente = TestFixtures.clienteValido();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = service.getById(1L);

        assertThat(resultado).isEqualTo(cliente);
    }

    @Test
    void getById_clienteNoExiste_lanzaClienteNotFoundException() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    // ---- searchByNombre() ----

    @Test
    void searchByNombre_delegaAlRepositorio() {
        List<Cliente> lista = List.of(TestFixtures.clienteValido());
        when(clienteRepository.searchByNombre("Juan")).thenReturn(lista);

        List<Cliente> resultado = service.searchByNombre("Juan");

        assertThat(resultado).isEqualTo(lista);
        verify(clienteRepository).searchByNombre("Juan");
    }

    // ---- create() ----

    @Test
    void create_llamaAlRepositorioSaveYRetornaGuardado() {
        Cliente nuevoCliente = TestFixtures.clienteSinId();
        Cliente clienteGuardado = TestFixtures.clienteValido();
        when(clienteRepository.save(nuevoCliente)).thenReturn(clienteGuardado);

        Cliente resultado = service.create(nuevoCliente);

        assertThat(resultado).isEqualTo(clienteGuardado);
        verify(clienteRepository).save(nuevoCliente);
    }

    // ---- update() ----

    @Test
    void update_clienteExiste_conTodosLosCampos_actualizaYGuarda() {
        Cliente existente = TestFixtures.clienteValido();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(existente)).thenReturn(existente);

        // updateData con nombre, apellido, teléfono y email
        Cliente updateData = Cliente.reconstituir(
                null, "NuevoNombre", "NuevoApellido", null, null, null,
                new Telefono("0000000000"), new Email("nuevo@example.com"),
                null, null
        );

        Cliente resultado = service.update(1L, updateData);

        assertThat(resultado.getNombre()).isEqualTo("NuevoNombre");
        assertThat(resultado.getApellido()).isEqualTo("NuevoApellido");
        assertThat(resultado.getTelefonoCelular().value()).isEqualTo("0000000000");
        assertThat(resultado.getEmail().value()).isEqualTo("nuevo@example.com");
        verify(clienteRepository).save(existente);
    }

    @Test
    void update_clienteExiste_soloConNombre_soloActualizaNombre() {
        Cliente existente = TestFixtures.clienteValido();
        String apellidoOriginal = existente.getApellido();
        String telefonoOriginal = existente.getTelefonoCelular().value();
        String emailOriginal = existente.getEmail().value();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(existente)).thenReturn(existente);

        Cliente updateData = Cliente.reconstituir(
                null, "NuevoNombre", null, null, null, null, null, null, null, null
        );

        service.update(1L, updateData);

        assertThat(existente.getNombre()).isEqualTo("NuevoNombre");
        assertThat(existente.getApellido()).isEqualTo(apellidoOriginal);
        assertThat(existente.getTelefonoCelular().value()).isEqualTo(telefonoOriginal);
        assertThat(existente.getEmail().value()).isEqualTo(emailOriginal);
    }

    @Test
    void update_clienteNoExiste_lanzaClienteNotFoundException() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        Cliente updateData = Cliente.reconstituir(
                null, "Nombre", null, null, null, null, null, null, null, null
        );

        assertThatThrownBy(() -> service.update(99L, updateData))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");

        verify(clienteRepository, never()).save(any());
    }

    // ---- delete() ----

    @Test
    void delete_clienteExiste_llamaDeleteById() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void delete_clienteNoExiste_lanzaClienteNotFoundException() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");

        verify(clienteRepository, never()).deleteById(any());
    }
}
