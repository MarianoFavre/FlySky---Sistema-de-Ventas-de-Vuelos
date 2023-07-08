package com.codoacodo.flysky.demo.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@NoArgsConstructor Al contener un atributo final no puede tener un constructor vacío.
@AllArgsConstructor
@Getter
public enum TipoUsuario {
    CLIENTE("Cliente"),
    ADMINISTRADOR("Administrador"),
    AGENTE_DE_VENTAS("Agente de ventas");

    private final String descripcion;
/* El Constructor no debe ser público.
    TipoUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
 */
}

