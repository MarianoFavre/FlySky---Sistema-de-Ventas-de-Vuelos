package com.codoacodo.flysky.demo.dto.response;

import com.codoacodo.flysky.demo.model.entity.UsuarioEntity;
import com.codoacodo.flysky.demo.model.entity.VueloEntity;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
public class ReservaDto {

    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;
}
