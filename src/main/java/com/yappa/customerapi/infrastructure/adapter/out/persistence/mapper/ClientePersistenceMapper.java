package com.yappa.customerapi.infrastructure.adapter.out.persistence.mapper;

import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceMapper {

    public Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getRazonSocial(),
                entity.getCuit(),
                entity.getFechaNacimiento(),
                entity.getTelefonoCelular(),
                entity.getEmail(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }

    public ClienteEntity toEntity(Cliente domain) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setApellido(domain.getApellido());
        entity.setRazonSocial(domain.getRazonSocial());
        entity.setCuit(domain.getCuit());
        entity.setFechaNacimiento(domain.getFechaNacimiento());
        entity.setTelefonoCelular(domain.getTelefonoCelular());
        entity.setEmail(domain.getEmail());
        return entity;
    }
}
