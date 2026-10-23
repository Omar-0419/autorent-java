
package com.autorent.infrastructure.adapter.output.persistence.entity;

import com.autorent.domain.model.Auto.EstadoAuto;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "autos")
public class AutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private int anio;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioPorDia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAuto estado;

    public AutoEntity() {
    }

    public Long getId() { return id; }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnio() { return anio; }
    public BigDecimal getPrecioPorDia() { return precioPorDia; }
    public EstadoAuto getEstado() { return estado; }

    public void setId(Long id) { this.id = id; }
    public void setPlaca(String placa) { this.placa = placa; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setAnio(int anio) { this.anio = anio; }
    public void setPrecioPorDia(BigDecimal precioPorDia) {
        this.precioPorDia = precioPorDia;
    }
    public void setEstado(EstadoAuto estado) { this.estado = estado; }
}
