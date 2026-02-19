package com.yappa.customerapi.infrastructure.adapter.in.rest;

import java.util.List;

import com.yappa.customerapi.domain.port.in.ActualizarClienteUseCase;
import com.yappa.customerapi.domain.port.in.BuscarClienteUseCase;
import com.yappa.customerapi.domain.port.in.CrearClienteUseCase;
import com.yappa.customerapi.domain.port.in.EliminarClienteUseCase;
import com.yappa.customerapi.domain.port.in.ObtenerClienteUseCase;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteCreateRequest;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteResponse;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteUpdateRequest;
import com.yappa.customerapi.infrastructure.adapter.in.rest.mapper.ClienteRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ObtenerClienteUseCase obtenerCliente;
    private final BuscarClienteUseCase buscarCliente;
    private final CrearClienteUseCase crearCliente;
    private final ActualizarClienteUseCase actualizarCliente;
    private final EliminarClienteUseCase eliminarCliente;
    private final ClienteRestMapper mapper;

    public ClienteController(ObtenerClienteUseCase obtenerCliente,
                             BuscarClienteUseCase buscarCliente,
                             CrearClienteUseCase crearCliente,
                             ActualizarClienteUseCase actualizarCliente,
                             EliminarClienteUseCase eliminarCliente,
                             ClienteRestMapper mapper) {
        this.obtenerCliente = obtenerCliente;
        this.buscarCliente = buscarCliente;
        this.crearCliente = crearCliente;
        this.actualizarCliente = actualizarCliente;
        this.eliminarCliente = eliminarCliente;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ClienteResponse> getAll() {
        return obtenerCliente.getAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse getById(@PathVariable Long id) {
        return mapper.toResponse(obtenerCliente.getById(id));
    }

    @GetMapping("/search")
    public List<ClienteResponse> search(@RequestParam String q) {
        return buscarCliente.searchByNombre(q).stream().map(mapper::toResponse).toList();
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteCreateRequest req) {
        var created = crearCliente.create(mapper.toDomain(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ClienteResponse update(@PathVariable Long id, @Valid @RequestBody ClienteUpdateRequest req) {
        if (req.isEmpty()) {
            throw new IllegalArgumentException("Debe enviar al menos un campo para actualizar");
        }
        return mapper.toResponse(actualizarCliente.update(id, mapper.toDomain(req)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        eliminarCliente.delete(id);
    }
}
