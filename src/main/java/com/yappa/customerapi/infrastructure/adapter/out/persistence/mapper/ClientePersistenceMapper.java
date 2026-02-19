package com.yappa.customerapi.infrastructure.adapter.out.persistence.mapper;

import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.model.Cuit;
import com.yappa.customerapi.domain.model.Email;
import com.yappa.customerapi.domain.model.Telefono;
import com.yappa.customerapi.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceMapper {

    public Cliente toDomain(ClienteEntity entity) {
        return Cliente.reconstituir(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getRazonSocial(),
                new Cuit(entity.getCuit()),
                entity.getFechaNacimiento(),
                new Telefono(entity.getTelefonoCelular()),
                new Email(entity.getEmail()),
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
        entity.setCuit(domain.getCuit().value());
        entity.setFechaNacimiento(domain.getFechaNacimiento());
        entity.setTelefonoCelular(domain.getTelefonoCelular().value());
        entity.setEmail(domain.getEmail().value());
        return entity;
    }
}
