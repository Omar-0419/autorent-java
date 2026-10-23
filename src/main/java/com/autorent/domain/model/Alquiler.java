
package com.autorent.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Alquiler {

    private Long id;
    private Long clienteId;
    private Long autoId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cantidadDias;
    private BigDecimal valorTotal;
    private EstadoAlquiler estado;

    public Alquiler(
            Long id,
            Long clienteId,
            Long autoId,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            int cantidadDias,
            BigDecimal valorTotal,
            EstadoAlquiler estado
    ) {
        this.id = id;
        this.clienteId = clienteId;
        this.autoId = autoId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadDias = cantidadDias;
        this.valorTotal = valorTotal;
        this.estado = estado;
    }

    public void finalizar() {
        if (this.estado == EstadoAlquiler.FINALIZADO) {
            throw new IllegalStateException(
                    "El alquiler ya fue finalizado"
            );
        }

        this.estado = EstadoAlquiler.FINALIZADO;
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getAutoId() {
        return autoId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getCantidadDias() {
        return cantidadDias;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public EstadoAlquiler getEstado() {
        return estado;
    }
}
