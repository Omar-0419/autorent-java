
package com.autorent.infrastructure.adapter.output.persistence.adapter;

import com.autorent.application.port.output.AlquilerRepositoryPort;
import com.autorent.domain.model.Alquiler;
import com.autorent.infrastructure.adapter.output.persistence.entity.AlquilerEntity;
import com.autorent.infrastructure.adapter.output.persistence.repository.AlquilerJpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlquilerPersistenceAdapter
        implements AlquilerRepositoryPort {

    private final AlquilerJpaRepository repository;

    public AlquilerPersistenceAdapter(
            AlquilerJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Alquiler guardar(Alquiler alquiler) {
        AlquilerEntity entity = new AlquilerEntity();

        entity.setClienteId(alquiler.getClienteId());
        entity.setAutoId(alquiler.getAutoId());
        entity.setFechaInicio(alquiler.getFechaInicio());
        entity.setFechaFin(alquiler.getFechaFin());
        entity.setCantidadDias(alquiler.getCantidadDias());
        entity.setValorTotal(alquiler.getValorTotal());
        entity.setEstado(alquiler.getEstado());

        AlquilerEntity guardado = repository.save(entity);

        return convertirADominio(guardado);
    }

    @Override
    public Optional<Alquiler> buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::convertirADominio);
    }

    @Override
    public List<Alquiler> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    private Alquiler convertirADominio(AlquilerEntity entity) {
        return new Alquiler(
                entity.getId(),
                entity.getClienteId(),
                entity.getAutoId(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getCantidadDias(),
                entity.getValorTotal(),
                entity.getEstado());
    }
}
