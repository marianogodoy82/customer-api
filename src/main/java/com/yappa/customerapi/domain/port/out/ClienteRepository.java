package com.yappa.customerapi.domain.port.out;

import com.yappa.customerapi.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    List<Cliente> searchByNombre(String q);
    Cliente save(Cliente cliente);
    boolean existsById(Long id);
    void deleteById(Long id);
}
