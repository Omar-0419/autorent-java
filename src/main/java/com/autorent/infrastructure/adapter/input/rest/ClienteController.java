
package com.autorent.infrastructure.adapter.input.rest;

import com.autorent.application.port.input.RegistrarClienteUseCase;
import com.autorent.application.port.output.ClienteRepositoryPort;
import com.autorent.domain.model.Cliente;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final RegistrarClienteUseCase registrarCliente;
    private final ClienteRepositoryPort clienteRepository;

    public ClienteController(
            RegistrarClienteUseCase registrarCliente,
            ClienteRepositoryPort clienteRepository
    ) {
        this.registrarCliente = registrarCliente;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@RequestBody CrearClienteRequest request) {
        try {
            return registrarCliente.registrar(
                    request.nombre(),
                    request.documento(),
                    request.telefono(),
                    request.email()
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage()
            );
        }
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el cliente con ID " + id
                ));
    }

    public record CrearClienteRequest(
            String nombre,
            String documento,
            String telefono,
            String email
    ) {}

    
}
