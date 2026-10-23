
package com.autorent.infrastructure.adapter.output.persistence.adapter;

import com.autorent.application.port.output.AutoRepositoryPort;
import com.autorent.domain.model.Auto;
import com.autorent.domain.model.Auto.EstadoAuto;
import com.autorent.infrastructure.adapter.output.persistence.entity.AutoEntity;
import com.autorent.infrastructure.adapter.output.persistence.repository.AutoJpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AutoPersistenceAdapter implements AutoRepositoryPort {

    private final AutoJpaRepository repository;

    public AutoPersistenceAdapter(AutoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Auto guardar(Auto auto) {
        AutoEntity entity = new AutoEntity();

        entity.setId(auto.getId());
        entity.setPlaca(auto.getPlaca());
        entity.setMarca(auto.getMarca());
        entity.setModelo(auto.getModelo());
        entity.setAnio(auto.getAnio());
        entity.setPrecioPorDia(auto.getPrecioPorDia());
        entity.setEstado(auto.getEstado());

        AutoEntity guardado = repository.save(entity);

        return convertirADominio(guardado);
    }

    @Override
    public Optional<Auto> buscarPorId(Long id) {
        return repository.findById(id).map(this::convertirADominio);
    }

    @Override
    public List<Auto> buscarDisponibles() {
        return repository.findByEstado(EstadoAuto.DISPONIBLE)
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    private Auto convertirADominio(AutoEntity entity) {
        return new Auto(
                entity.getId(),
                entity.getPlaca(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAnio(),
                entity.getPrecioPorDia(),
                entity.getEstado()
        );
    }
}
