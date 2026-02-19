package com.yappa.customerapi.infrastructure.adapter.in.rest.mapper;

import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.model.Cuit;
import com.yappa.customerapi.domain.model.Email;
import com.yappa.customerapi.domain.model.Telefono;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteCreateRequest;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteResponse;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class ClienteRestMapper {

    public Cliente toDomain(ClienteCreateRequest req) {
        return Cliente.crear(
                req.nombre(),
                req.apellido(),
                req.razonSocial(),
                new Cuit(req.cuit()),
                req.fechaNacimiento(),
                new Telefono(req.telefonoCelular()),
                new Email(req.email())
        );
    }

    public Cliente toDomain(ClienteUpdateRequest req) {
        return Cliente.reconstituir(
                null,
                req.nombre(),
                req.apellido(),
                null,
                null,
                null,
                req.telefonoCelular() != null ? new Telefono(req.telefonoCelular()) : null,
                req.email() != null ? new Email(req.email()) : null,
                null,
                null
        );
    }

    public ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNombre(),
                c.getApellido(),
                c.getRazonSocial(),
                c.getCuit().value(),
                c.getFechaNacimiento(),
                c.getTelefonoCelular().value(),
                c.getEmail().value(),
                c.getFechaCreacion(),
                c.getFechaModificacion()
        );
    }
}
