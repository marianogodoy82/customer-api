package com.yappa.customerapi.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yappa.customerapi.TestFixtures;
import com.yappa.customerapi.domain.exception.ClienteNotFoundException;
import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.port.in.ActualizarClienteUseCase;
import com.yappa.customerapi.domain.port.in.BuscarClienteUseCase;
import com.yappa.customerapi.domain.port.in.CrearClienteUseCase;
import com.yappa.customerapi.domain.port.in.EliminarClienteUseCase;
import com.yappa.customerapi.domain.port.in.ObtenerClienteUseCase;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteCreateRequest;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteResponse;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteUpdateRequest;
import com.yappa.customerapi.infrastructure.adapter.in.rest.mapper.ClienteRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ObtenerClienteUseCase obtenerCliente;
    @MockBean private BuscarClienteUseCase buscarCliente;
    @MockBean private CrearClienteUseCase crearCliente;
    @MockBean private ActualizarClienteUseCase actualizarCliente;
    @MockBean private EliminarClienteUseCase eliminarCliente;
    @MockBean private ClienteRestMapper mapper;

    private ClienteResponse buildResponse() {
        return new ClienteResponse(
                TestFixtures.ID,
                TestFixtures.NOMBRE,
                TestFixtures.APELLIDO,
                TestFixtures.RAZON_SOCIAL,
                TestFixtures.CUIT_VALUE,
                TestFixtures.FECHA_NACIMIENTO,
                TestFixtures.TELEFONO_VALUE,
                TestFixtures.EMAIL_VALUE,
                TestFixtures.FECHA_CREACION,
                TestFixtures.FECHA_MODIFICACION
        );
    }

    // ---- GET /api/clientes ----

    @Test
    void getAll_retorna200ConListaVacia() throws Exception {
        when(obtenerCliente.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAll_retorna200ConElementos() throws Exception {
        Cliente cliente = TestFixtures.clienteValido();
        ClienteResponse response = buildResponse();

        when(obtenerCliente.getAll()).thenReturn(List.of(cliente));
        when(mapper.toResponse(cliente)).thenReturn(response);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value(TestFixtures.NOMBRE));
    }

    // ---- GET /api/clientes/{id} ----

    @Test
    void getById_clienteExiste_retorna200() throws Exception {
        Cliente cliente = TestFixtures.clienteValido();
        ClienteResponse response = buildResponse();

        when(obtenerCliente.getById(1L)).thenReturn(cliente);
        when(mapper.toResponse(cliente)).thenReturn(response);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value(TestFixtures.NOMBRE));
    }

    @Test
    void getById_clienteNoExiste_retorna404() throws Exception {
        when(obtenerCliente.getById(99L)).thenThrow(new ClienteNotFoundException("Cliente no encontrado"));

        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound());
    }

    // ---- GET /api/clientes/search?q=... ----

    @Test
    void search_retorna200ConResultados() throws Exception {
        Cliente cliente = TestFixtures.clienteValido();
        ClienteResponse response = buildResponse();

        when(buscarCliente.searchByNombre("Juan")).thenReturn(List.of(cliente));
        when(mapper.toResponse(cliente)).thenReturn(response);

        mockMvc.perform(get("/api/clientes/search").param("q", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value(TestFixtures.NOMBRE));
    }

    @Test
    void search_retorna200ConListaVacia() throws Exception {
        when(buscarCliente.searchByNombre("NoExiste")).thenReturn(List.of());

        mockMvc.perform(get("/api/clientes/search").param("q", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // ---- POST /api/clientes ----

    @Test
    void create_conDatosValidos_retorna201() throws Exception {
        ClienteCreateRequest req = TestFixtures.clienteCreateRequestValido();
        Cliente clienteDomain = TestFixtures.clienteSinId();
        Cliente clienteGuardado = TestFixtures.clienteValido();
        ClienteResponse response = buildResponse();

        when(mapper.toDomain(any(ClienteCreateRequest.class))).thenReturn(clienteDomain);
        when(crearCliente.create(clienteDomain)).thenReturn(clienteGuardado);
        when(mapper.toResponse(clienteGuardado)).thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value(TestFixtures.NOMBRE));
    }

    @Test
    void create_conNombreBlanco_retorna400() throws Exception {
        ClienteCreateRequest reqInvalido = new ClienteCreateRequest(
                "", "Pérez", "Empresa SRL", "20-12345678-9",
                LocalDate.of(1990, 1, 1), "123456789", "test@test.com"
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_conCuitInvalido_retorna400() throws Exception {
        ClienteCreateRequest reqInvalido = new ClienteCreateRequest(
                "Juan", "Pérez", "Empresa SRL", "CUIT-INVALIDO",
                LocalDate.of(1990, 1, 1), "123456789", "test@test.com"
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_conEmailInvalido_retorna400() throws Exception {
        ClienteCreateRequest reqInvalido = new ClienteCreateRequest(
                "Juan", "Pérez", "Empresa SRL", "20-12345678-9",
                LocalDate.of(1990, 1, 1), "123456789", "no-es-un-email"
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqInvalido)))
                .andExpect(status().isBadRequest());
    }

    // ---- PUT /api/clientes/{id} ----

    @Test
    void update_conCuerpoValido_retorna200() throws Exception {
        ClienteUpdateRequest req = new ClienteUpdateRequest("NuevoNombre", null, null, null);
        Cliente clienteActualizado = TestFixtures.clienteValido();
        ClienteResponse response = buildResponse();

        when(mapper.toDomain(any(ClienteUpdateRequest.class))).thenReturn(
                com.yappa.customerapi.domain.model.Cliente.reconstituir(
                        null, "NuevoNombre", null, null, null, null, null, null, null, null
                )
        );
        when(actualizarCliente.update(eq(1L), any())).thenReturn(clienteActualizado);
        when(mapper.toResponse(clienteActualizado)).thenReturn(response);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void update_conCuerpoVacio_retorna400() throws Exception {
        ClienteUpdateRequest reqVacio = new ClienteUpdateRequest(null, null, null, null);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqVacio)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_clienteNoExiste_retorna404() throws Exception {
        ClienteUpdateRequest req = new ClienteUpdateRequest("NuevoNombre", null, null, null);

        when(mapper.toDomain(any(ClienteUpdateRequest.class))).thenReturn(
                com.yappa.customerapi.domain.model.Cliente.reconstituir(
                        null, "NuevoNombre", null, null, null, null, null, null, null, null
                )
        );
        when(actualizarCliente.update(eq(99L), any()))
                .thenThrow(new ClienteNotFoundException("Cliente no encontrado"));

        mockMvc.perform(put("/api/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    // ---- DELETE /api/clientes/{id} ----

    @Test
    void delete_clienteExiste_retorna204() throws Exception {
        doNothing().when(eliminarCliente).delete(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_clienteNoExiste_retorna404() throws Exception {
        doThrow(new ClienteNotFoundException("Cliente no encontrado")).when(eliminarCliente).delete(99L);

        mockMvc.perform(delete("/api/clientes/99"))
                .andExpect(status().isNotFound());
    }
}
