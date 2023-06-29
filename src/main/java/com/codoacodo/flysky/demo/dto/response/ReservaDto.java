package com.codoacodo.flysky.demo.dto.response;


import com.codoacodo.flysky.demo.model.enums.TipoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
public class ReservaDto {

    private TipoPago tipoPago;

    private double montoPagar;

    private LocalDateTime fechaHoraReserva;

    private UsuarioDto usuario;
}
