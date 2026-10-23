
package com.autorent.infrastructure.adapter.input.rest;

import com.autorent.application.port.input.CrearAlquilerUseCase;
import com.autorent.application.port.input.FinalizarAlquilerUseCase;
import com.autorent.application.port.output.AlquilerRepositoryPort;
import com.autorent.domain.model.Alquiler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

        private final CrearAlquilerUseCase crearAlquilerUseCase;
        private final AlquilerRepositoryPort alquilerRepositoryPort;
        private final FinalizarAlquilerUseCase finalizarAlquilerUseCase;

        public AlquilerController(
                        CrearAlquilerUseCase crearAlquilerUseCase,
                        AlquilerRepositoryPort alquilerRepositoryPort,
                        FinalizarAlquilerUseCase finalizarAlquilerUseCase) {
                this.crearAlquilerUseCase = crearAlquilerUseCase;
                this.alquilerRepositoryPort = alquilerRepositoryPort;
                this.finalizarAlquilerUseCase = finalizarAlquilerUseCase;
        }

        @PostMapping
        public ResponseEntity<?> crear(
                        @RequestBody CrearAlquilerRequest request) {
                try {
                        Alquiler alquiler = crearAlquilerUseCase.crear(
                                        request.clienteId(),
                                        request.autoId(),
                                        request.fechaInicio(),
                                        request.fechaFin());

                        return ResponseEntity
                                        .status(HttpStatus.CREATED)
                                        .body(alquiler);

                } catch (IllegalArgumentException e) {
                        return ResponseEntity
                                        .badRequest()
                                        .body(Map.of("error", e.getMessage()));

                } catch (IllegalStateException e) {
                        return ResponseEntity
                                        .status(HttpStatus.CONFLICT)
                                        .body(Map.of("error", e.getMessage()));
                }
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
                return alquilerRepositoryPort.buscarPorId(id)
                                .<ResponseEntity<?>>map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(Map.of(
                                                                "error",
                                                                "El alquiler no existe")));
        }

        public record CrearAlquilerRequest(
                        Long clienteId,
                        Long autoId,
                        LocalDate fechaInicio,
                        LocalDate fechaFin) {
        }

        @PutMapping("/{id}/finalizar")
        public ResponseEntity<?> finalizar(@PathVariable Long id) {
                try {
                        return ResponseEntity.ok(finalizarAlquilerUseCase.finalizar(id));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.status(404)
                                        .body(java.util.Map.of("error", e.getMessage()));
                } catch (IllegalStateException e) {
                        return ResponseEntity.status(409)
                                        .body(java.util.Map.of("error", e.getMessage()));
                }
        }
}
