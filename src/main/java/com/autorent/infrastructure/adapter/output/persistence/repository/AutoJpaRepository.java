
package com.autorent.infrastructure.adapter.output.persistence.repository;

import com.autorent.infrastructure.adapter.output.persistence.entity.AutoEntity;
import com.autorent.domain.model.Auto.EstadoAuto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoJpaRepository
        extends JpaRepository<AutoEntity, Long> {

    List<AutoEntity> findByEstado(EstadoAuto estado);
}
