
package com.autorent.domain.model;

public class Cliente {

    private Long id;
    private String nombre;
    private String documento;
    private String telefono;
    private String email;

    public Cliente(
            Long id,
            String nombre,
            String documento,
            String telefono,
            String email
    ) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }
}
    