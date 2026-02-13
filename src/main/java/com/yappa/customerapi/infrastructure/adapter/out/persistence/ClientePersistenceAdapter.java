package com.yappa.customerapi.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.port.out.ClienteRepository;
import com.yappa.customerapi.infrastructure.adapter.out.persistence.mapper.ClientePersistenceMapper;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceAdapter implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;
    private final ClienteSearchJdbcRepository searchRepository;
    private final ClientePersistenceMapper mapper;

    public ClientePersistenceAdapter(ClienteJpaRepository jpaRepository,
                                     ClienteSearchJdbcRepository searchRepository,
                                     ClientePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.searchRepository = searchRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Cliente> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Cliente> searchByNombre(String q) {
        return searchRepository.searchByNombre(q);
    }

    @Override
    public Cliente save(Cliente cliente) {
        var entity = mapper.toEntity(cliente);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
