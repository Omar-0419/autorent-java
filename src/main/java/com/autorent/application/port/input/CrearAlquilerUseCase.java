
package com.autorent.application.port.input;

import com.autorent.domain.model.Alquiler;
import java.time.LocalDate;

public interface CrearAlquilerUseCase {

    Alquiler crear(
            Long clienteId,
            Long autoId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}
