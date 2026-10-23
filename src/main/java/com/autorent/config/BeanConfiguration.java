
package com.autorent.config;

import com.autorent.application.port.input.CrearAlquilerUseCase;
import com.autorent.application.port.input.FinalizarAlquilerUseCase;
import com.autorent.application.port.input.RegistrarAutoUseCase;
import com.autorent.application.port.input.RegistrarClienteUseCase;

import com.autorent.application.port.output.AlquilerRepositoryPort;
import com.autorent.application.port.output.AutoRepositoryPort;
import com.autorent.application.port.output.ClienteRepositoryPort;

import com.autorent.application.service.AlquilerService;
import com.autorent.application.service.AutoService;
import com.autorent.application.service.ClienteService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RegistrarAutoUseCase registrarAutoUseCase(
            AutoRepositoryPort autoRepositoryPort) {
        return new AutoService(autoRepositoryPort);
    }

    @Bean
    public RegistrarClienteUseCase registrarClienteUseCase(
            ClienteRepositoryPort clienteRepositoryPort) {
        return new ClienteService(clienteRepositoryPort);
    }

    @Bean
    public CrearAlquilerUseCase crearAlquilerUseCase(
            AlquilerRepositoryPort alquilerRepositoryPort,
            AutoRepositoryPort autoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort) {
        return new AlquilerService(
                alquilerRepositoryPort,
                autoRepositoryPort,
                clienteRepositoryPort);
    }

    @Bean
    public FinalizarAlquilerUseCase finalizarAlquilerUseCase(
            AlquilerRepositoryPort alquilerRepositoryPort,
            AutoRepositoryPort autoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort) {
        return new AlquilerService(
                alquilerRepositoryPort,
                autoRepositoryPort,
                clienteRepositoryPort);
    }
}
