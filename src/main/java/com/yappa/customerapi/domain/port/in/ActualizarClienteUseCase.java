package com.yappa.customerapi.domain.port.in;

import com.yappa.customerapi.domain.model.Cliente;

public interface ActualizarClienteUseCase {
    Cliente update(Long id, Cliente updateData);
}
