package com.yappa.customerapi.infrastructure.adapter.out.persistence;

import com.yappa.customerapi.TestFixtures;
import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import com.yappa.customerapi.infrastructure.adapter.out.persistence.mapper.ClientePersistenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientePersistenceAdapterTest {

    @Mock
    private ClienteJpaRepository jpaRepository;

    @Mock
    private ClienteSearchJdbcRepository searchRepository;

    @Mock
    private ClientePersistenceMapper mapper;

    private ClientePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ClientePersistenceAdapter(jpaRepository, searchRepository, mapper);
    }

    // ---- findAll() ----

    @Test
    void findAll_retornaListaMapeada() {
        ClienteEntity entity = TestFixtures.clienteEntityValida();
        Cliente cliente = TestFixtures.clienteValido();

        when(jpaRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(cliente);

        List<Cliente> resultado = adapter.findAll();

        assertThat(resultado).containsExactly(cliente);
        verify(jpaRepository).findAll();
        verify(mapper).toDomain(entity);
    }

    @Test
    void findAll_cuandoRepositorioVacio_retornaListaVacia() {
        when(jpaRepository.findAll()).thenReturn(List.of());

        List<Cliente> resultado = adapter.findAll();

        assertThat(resultado).isEmpty();
    }

    // ---- findById() ----

    @Test
    void findById_clienteExiste_retornaOptionalConCliente() {
        ClienteEntity entity = TestFixtures.clienteEntityValida();
        Cliente cliente = TestFixtures.clienteValido();

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(cliente);

        Optional<Cliente> resultado = adapter.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get()).isEqualTo(cliente);
    }

    @Test
    void findById_clienteNoExiste_retornaOptionalVacio() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = adapter.findById(99L);

        assertThat(resultado).isEmpty();
        verify(mapper, never()).toDomain(any(ClienteEntity.class));
    }

    // ---- searchByNombre() ----

    @Test
    void searchByNombre_delegaAlSearchRepository() {
        Cliente cliente = TestFixtures.clienteValido();
        when(searchRepository.searchByNombre("Juan")).thenReturn(List.of(cliente));

        List<Cliente> resultado = adapter.searchByNombre("Juan");

        assertThat(resultado).containsExactly(cliente);
        verify(searchRepository).searchByNombre("Juan");
    }

    // ---- save() ----

    @Test
    void save_convierteAEntidad_guardaYRetornaDomain() {
        Cliente clienteDomain = TestFixtures.clienteSinId();
        ClienteEntity entityIn = TestFixtures.clienteEntityValida();
        entityIn.setId(null);
        ClienteEntity entityOut = TestFixtures.clienteEntityValida();
        Cliente clienteGuardado = TestFixtures.clienteValido();

        when(mapper.toEntity(clienteDomain)).thenReturn(entityIn);
        when(jpaRepository.save(entityIn)).thenReturn(entityOut);
        when(mapper.toDomain(entityOut)).thenReturn(clienteGuardado);

        Cliente resultado = adapter.save(clienteDomain);

        assertThat(resultado).isEqualTo(clienteGuardado);
        verify(mapper).toEntity(clienteDomain);
        verify(jpaRepository).save(entityIn);
        verify(mapper).toDomain(entityOut);
    }

    // ---- existsById() ----

    @Test
    void existsById_cuandoExiste_retornaTrue() {
        when(jpaRepository.existsById(1L)).thenReturn(true);

        assertThat(adapter.existsById(1L)).isTrue();
    }

    @Test
    void existsById_cuandoNoExiste_retornaFalse() {
        when(jpaRepository.existsById(99L)).thenReturn(false);

        assertThat(adapter.existsById(99L)).isFalse();
    }

    // ---- deleteById() ----

    @Test
    void deleteById_invocaJpaRepository() {
        doNothing().when(jpaRepository).deleteById(1L);

        adapter.deleteById(1L);

        verify(jpaRepository).deleteById(1L);
    }
}
