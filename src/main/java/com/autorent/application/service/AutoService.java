
package com.autorent.application.service;

import com.autorent.application.port.input.RegistrarAutoUseCase;
import com.autorent.application.port.output.AutoRepositoryPort;
import com.autorent.domain.model.Auto;
import com.autorent.domain.model.Auto.EstadoAuto;

import java.math.BigDecimal;

public class AutoService implements RegistrarAutoUseCase {

    private final AutoRepositoryPort autoRepository;

    public AutoService(AutoRepositoryPort autoRepository) {
        this.autoRepository = autoRepository;
    }

    @Override
    public Auto registrar(
            String placa,
            String marca,
            String modelo,
            int anio,
            BigDecimal precioPorDia) {

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }

        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }

        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }

        if (precioPorDia == null ||
                precioPorDia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio por día debe ser mayor que cero");
        }

        Auto auto = new Auto(
                null,
                placa.trim().toUpperCase(),
                marca.trim(),
                modelo.trim(),
                anio,
                precioPorDia,
                EstadoAuto.DISPONIBLE
        );

        return autoRepository.guardar(auto);
    }
}
