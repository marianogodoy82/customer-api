package com.yappa.customerapi.application.service;

import java.util.List;

import com.yappa.customerapi.domain.exception.ClienteNotFoundException;
import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.port.in.ClienteService;
import com.yappa.customerapi.domain.port.out.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);

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
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            log.warn("Create conflict", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Cliente update(Long id, Cliente updateData) {
        Cliente existing = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado"));

        if (updateData.getNombre() != null) existing.setNombre(updateData.getNombre());
        if (updateData.getApellido() != null) existing.setApellido(updateData.getApellido());
        if (updateData.getTelefonoCelular() != null) existing.setTelefonoCelular(updateData.getTelefonoCelular());
        if (updateData.getEmail() != null) existing.setEmail(updateData.getEmail());

        try {
            return clienteRepository.save(existing);
        } catch (DataIntegrityViolationException e) {
            log.warn("Update conflict", e);
            throw e;
        }
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
