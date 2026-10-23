
package com.autorent.application.service;

import com.autorent.application.port.input.CrearAlquilerUseCase;
import com.autorent.application.port.input.FinalizarAlquilerUseCase;
import com.autorent.application.port.output.AlquilerRepositoryPort;
import com.autorent.application.port.output.AutoRepositoryPort;
import com.autorent.application.port.output.ClienteRepositoryPort;
import com.autorent.domain.model.Alquiler;
import com.autorent.domain.model.Auto;
import com.autorent.domain.model.Cliente;
import com.autorent.domain.model.EstadoAlquiler;
import com.autorent.domain.model.Auto.EstadoAuto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AlquilerService implements CrearAlquilerUseCase, FinalizarAlquilerUseCase {

        private final AlquilerRepositoryPort alquilerRepositoryPort;
        private final AutoRepositoryPort autoRepositoryPort;
        private final ClienteRepositoryPort clienteRepositoryPort;

        public AlquilerService(
                        AlquilerRepositoryPort alquilerRepositoryPort,
                        AutoRepositoryPort autoRepositoryPort,
                        ClienteRepositoryPort clienteRepositoryPort) {
                this.alquilerRepositoryPort = alquilerRepositoryPort;
                this.autoRepositoryPort = autoRepositoryPort;
                this.clienteRepositoryPort = clienteRepositoryPort;
        }

        @Override
        public Alquiler crear(
                        Long clienteId,
                        Long autoId,
                        LocalDate fechaInicio,
                        LocalDate fechaFin) {
                if (clienteId == null || autoId == null) {
                        throw new IllegalArgumentException(
                                        "El cliente y el auto son obligatorios");
                }

                if (fechaInicio == null || fechaFin == null) {
                        throw new IllegalArgumentException(
                                        "Las fechas son obligatorias");
                }

                if (!fechaFin.isAfter(fechaInicio)) {
                        throw new IllegalArgumentException(
                                        "La fecha final debe ser posterior a la inicial");
                }

                Cliente cliente = clienteRepositoryPort
                                .buscarPorId(clienteId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El cliente no existe"));

                Auto auto = autoRepositoryPort
                                .buscarPorId(autoId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El auto no existe"));

                if (auto.getEstado() != EstadoAuto.DISPONIBLE) {
                        throw new IllegalStateException(
                                        "El auto no está disponible para alquilar");
                }

                long dias = ChronoUnit.DAYS.between(
                                fechaInicio,
                                fechaFin);

                if (dias <= 0 || dias > Integer.MAX_VALUE) {
                        throw new IllegalArgumentException(
                                        "La cantidad de días no es válida");
                }

                BigDecimal valorTotal = auto.getPrecioPorDia()
                                .multiply(BigDecimal.valueOf(dias));

                Alquiler alquiler = new Alquiler(
                                null,
                                cliente.getId(),
                                auto.getId(),
                                fechaInicio,
                                fechaFin,
                                (int) dias,
                                valorTotal,
                                EstadoAlquiler.ACTIVO);

                auto.cambiarEstado(EstadoAuto.ALQUILADO);
                autoRepositoryPort.guardar(auto);

                return alquilerRepositoryPort.guardar(alquiler);
        }

        public Alquiler finalizar(Long alquilerId) {
                Alquiler alquiler = alquilerRepositoryPort.buscarPorId(alquilerId)
                                .orElseThrow(() -> new IllegalArgumentException("El alquiler no existe"));

                if (alquiler.getEstado() == EstadoAlquiler.FINALIZADO) {
                        throw new IllegalStateException("El alquiler ya está finalizado");
                }

                Auto auto = autoRepositoryPort.buscarPorId(alquiler.getAutoId())
                                .orElseThrow(() -> new IllegalArgumentException("El auto no existe"));

                alquiler.finalizar();
                auto.cambiarEstado(EstadoAuto.DISPONIBLE);

                autoRepositoryPort.guardar(auto);
                return alquilerRepositoryPort.guardar(alquiler);
        }
}
