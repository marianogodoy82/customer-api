package com.yappa.customerapi.infrastructure.adapter.in.rest.mapper;

import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteCreateRequest;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteResponse;
import com.yappa.customerapi.infrastructure.adapter.in.rest.dto.ClienteUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class ClienteRestMapper {

    public Cliente toDomain(ClienteCreateRequest req) {
        Cliente c = new Cliente();
        c.setNombre(req.nombre());
        c.setApellido(req.apellido());
        c.setRazonSocial(req.razonSocial());
        c.setCuit(req.cuit());
        c.setFechaNacimiento(req.fechaNacimiento());
        c.setTelefonoCelular(req.telefonoCelular());
        c.setEmail(req.email());
        return c;
    }

    public Cliente toDomain(ClienteUpdateRequest req) {
        Cliente c = new Cliente();
        c.setNombre(req.nombre());
        c.setApellido(req.apellido());
        c.setTelefonoCelular(req.telefonoCelular());
        c.setEmail(req.email());
        return c;
    }

    public ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNombre(),
                c.getApellido(),
                c.getRazonSocial(),
                c.getCuit(),
                c.getFechaNacimiento(),
                c.getTelefonoCelular(),
                c.getEmail(),
                c.getFechaCreacion(),
                c.getFechaModificacion()
        );
    }
}
