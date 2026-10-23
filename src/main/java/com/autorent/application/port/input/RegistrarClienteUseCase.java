
package com.autorent.application.port.input;

import com.autorent.domain.model.Cliente;

public interface RegistrarClienteUseCase {

    Cliente registrar(
            String nombre,
            String documento,
            String telefono,
            String email
    );
}
        