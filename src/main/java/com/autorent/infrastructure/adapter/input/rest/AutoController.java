
package com.autorent.infrastructure.adapter.input.rest;

import com.autorent.application.port.input.RegistrarAutoUseCase;
import com.autorent.application.port.output.AutoRepositoryPort;
import com.autorent.domain.model.Auto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/autos")
public class AutoController {

    private final RegistrarAutoUseCase registrarAuto;
    private final AutoRepositoryPort autoRepository;

    public AutoController(
            RegistrarAutoUseCase registrarAuto,
            AutoRepositoryPort autoRepository) {
        this.registrarAuto = registrarAuto;
        this.autoRepository = autoRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Auto crear(@RequestBody CrearAutoRequest request) {
        return registrarAuto.registrar(
                request.placa(),
                request.marca(),
                request.modelo(),
                request.anio(),
                request.precioPorDia()
        );
    }

    @GetMapping("/{id}")
    public Auto buscarPorId(@PathVariable Long id) {
        return autoRepository.buscarPorId(id)
                .orElseThrow(() ->
                        new AutoNoEncontradoException(id));
    }

    @GetMapping("/disponibles")
    public List<Auto> buscarDisponibles() {
        return autoRepository.buscarDisponibles();
    }

    public record CrearAutoRequest(
            String placa,
            String marca,
            String modelo,
            int anio,
            BigDecimal precioPorDia) {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class AutoNoEncontradoException
            extends RuntimeException {
        public AutoNoEncontradoException(Long id) {
            super("No se encontró el auto con ID " + id);
        }
    }
}
