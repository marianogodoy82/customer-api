package com.yappa.customerapi.application.service;

import java.util.List;

import com.yappa.customerapi.domain.exception.ClienteNotFoundException;
import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.port.in.ActualizarClienteUseCase;
import com.yappa.customerapi.domain.port.in.BuscarClienteUseCase;
import com.yappa.customerapi.domain.port.in.CrearClienteUseCase;
import com.yappa.customerapi.domain.port.in.EliminarClienteUseCase;
import com.yappa.customerapi.domain.port.in.ObtenerClienteUseCase;
import com.yappa.customerapi.domain.port.out.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ObtenerClienteUseCase, BuscarClienteUseCase,
        CrearClienteUseCase, ActualizarClienteUseCase, EliminarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente getById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado"));
    }

    @Override
    public List<Cliente> searchByNombre(String q) {
        return clienteRepository.searchByNombre(q);
    }

    @Override
    @Transactional
    public Cliente create(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente update(Long id, Cliente updateData) {
        Cliente existing = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado"));

        if (updateData.getNombre() != null) existing.actualizarNombre(updateData.getNombre());
        if (updateData.getApellido() != null) existing.actualizarApellido(updateData.getApellido());
        if (updateData.getTelefonoCelular() != null) existing.actualizarTelefono(updateData.getTelefonoCelular());
        if (updateData.getEmail() != null) existing.actualizarEmail(updateData.getEmail());

        return clienteRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }
}
