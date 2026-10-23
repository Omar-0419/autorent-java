
package com.autorent.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "clientes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_cliente_documento",
            columnNames = "documento"
        )
    }
)
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String documento;

    @Column(nullable = false, length = 30)
    private String telefono;

    @Column(nullable = false, length = 150)
    private String email;

    public ClienteEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
