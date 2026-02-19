package com.yappa.customerapi.domain.port.in;

import com.yappa.customerapi.domain.model.Cliente;

import java.util.List;

public interface ObtenerClienteUseCase {
    List<Cliente> getAll();
    Cliente getById(Long id);
}
