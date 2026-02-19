package com.yappa.customerapi.domain.port.in;

import com.yappa.customerapi.domain.model.Cliente;

public interface CrearClienteUseCase {
    Cliente create(Cliente cliente);
}
