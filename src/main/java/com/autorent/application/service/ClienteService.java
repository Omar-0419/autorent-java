
package com.autorent.application.service;

import com.autorent.application.port.input.RegistrarClienteUseCase;
import com.autorent.application.port.output.ClienteRepositoryPort;
import com.autorent.domain.model.Cliente;

public class ClienteService implements RegistrarClienteUseCase {

    private final ClienteRepositoryPort clienteRepository;

    public ClienteService(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente registrar(
            String nombre,
            String documento,
            String telefono,
            String email
    ) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento es obligatorio");
        }

        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }

        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }

        nombre = nombre.trim();
        documento = documento.trim();
        telefono = telefono.trim();
        email = email.trim().toLowerCase();

        if (clienteRepository.existePorDocumento(documento)) {
            throw new IllegalStateException(
                    "Ya existe un cliente con ese documento"
            );
        }

        Cliente cliente = new Cliente(
                null,
                nombre,
                documento,
                telefono,
                email
        );

        return clienteRepository.guardar(cliente);
    }
}
