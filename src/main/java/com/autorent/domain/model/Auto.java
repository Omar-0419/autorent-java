
package com.autorent.domain.model;

import java.math.BigDecimal;

public class Auto {

    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private BigDecimal precioPorDia;
    private EstadoAuto estado;

    public enum EstadoAuto {
        DISPONIBLE,
        ALQUILADO,
        MANTENIMIENTO
    }

    public Auto(Long id, String placa, String marca, String modelo,
                int anio, BigDecimal precioPorDia, EstadoAuto estado) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precioPorDia = precioPorDia;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnio() { return anio; }
    public BigDecimal getPrecioPorDia() { return precioPorDia; }
    public EstadoAuto getEstado() { return estado; }

    public void cambiarEstado(EstadoAuto nuevoEstado) {
        this.estado = nuevoEstado;
    }
}
