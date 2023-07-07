package com.codoacodo.flysky.demo.dto.response;


import com.codoacodo.flysky.demo.model.entity.VueloEntity;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
public class ReservaDto {

    private TipoPago tipoPago;

    private double montoPago;

    private LocalDate fechaReserva;

    private UsuarioDto usuario;

    private VueloReservaDto vuelo;

    private String posicionButaca;
}
