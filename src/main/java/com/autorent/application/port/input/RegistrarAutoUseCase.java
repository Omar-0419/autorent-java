
package com.autorent.application.port.input;

import com.autorent.domain.model.Auto;
import java.math.BigDecimal;

public interface RegistrarAutoUseCase {

    Auto registrar(
        String placa,
        String marca,
        String modelo,
        int anio,
        BigDecimal precioPorDia
    );
}
