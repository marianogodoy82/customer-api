package com.yappa.customerapi.domain.port.in;

import com.yappa.customerapi.domain.model.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> getAll();
    Cliente getById(Long id);
    List<Cliente> searchByNombre(String q);
    Cliente create(Cliente cliente);
    Cliente update(Long id, Cliente cliente);
    void delete(Long id);
}
