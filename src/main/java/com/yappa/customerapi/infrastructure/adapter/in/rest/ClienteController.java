package com.yappa.customerapi.infrastructure.adapter.in.rest;

import java.util.List;

import com.yappa.customerapi.domain.port.in.ClienteService;
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

    private final ClienteService clienteService;
    private final ClienteRestMapper mapper;

    public ClienteController(ClienteService clienteService, ClienteRestMapper mapper) {
        this.clienteService = clienteService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ClienteResponse> getAll() {
        return clienteService.getAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse getById(@PathVariable Long id) {
        return mapper.toResponse(clienteService.getById(id));
    }

    @GetMapping("/search")
    public List<ClienteResponse> search(@RequestParam String q) {
        return clienteService.searchByNombre(q).stream().map(mapper::toResponse).toList();
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteCreateRequest req) {
        var created = clienteService.create(mapper.toDomain(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ClienteResponse update(@PathVariable Long id, @Valid @RequestBody ClienteUpdateRequest req) {
        if (req.isEmpty()) {
            throw new IllegalArgumentException("Debe enviar al menos un campo para actualizar");
        }
        return mapper.toResponse(clienteService.update(id, mapper.toDomain(req)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clienteService.delete(id);
    }
}
