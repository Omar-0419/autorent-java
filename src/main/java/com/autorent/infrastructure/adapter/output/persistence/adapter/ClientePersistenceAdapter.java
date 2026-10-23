
package com.autorent.infrastructure.adapter.output.persistence.adapter;

import com.autorent.application.port.output.ClienteRepositoryPort;
import com.autorent.domain.model.Cliente;
import com.autorent.infrastructure.adapter.output.persistence.entity.ClienteEntity;
import com.autorent.infrastructure.adapter.output.persistence.repository.ClienteJpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientePersistenceAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository repository;

    public ClientePersistenceAdapter(ClienteJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();

        entity.setNombre(cliente.getNombre());
        entity.setDocumento(cliente.getDocumento());
        entity.setTelefono(cliente.getTelefono());
        entity.setEmail(cliente.getEmail());

        ClienteEntity guardado = repository.save(entity);

        return convertirADominio(guardado);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::convertirADominio);
    }

    @Override
    public boolean existePorDocumento(String documento) {
        return repository.existsByDocumento(documento);
    }

    private Cliente convertirADominio(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNombre(),
                entity.getDocumento(),
                entity.getTelefono(),
                entity.getEmail()
        );
    }
}
