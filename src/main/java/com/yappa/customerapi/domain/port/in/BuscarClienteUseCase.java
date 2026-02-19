package com.yappa.customerapi.domain.port.in;

import com.yappa.customerapi.domain.model.Cliente;

import java.util.List;

public interface BuscarClienteUseCase {
    List<Cliente> searchByNombre(String q);
}
