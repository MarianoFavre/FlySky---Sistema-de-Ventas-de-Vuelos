package com.codoacodo.flysky.demo.dto.response;


import com.codoacodo.flysky.demo.model.enums.TipoPago;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
@EqualsAndHashCode
public class ReservaDto {

    private TipoPago tipoPago;

    private double montoPago;

    private LocalDate fechaReserva;

    //private UsuarioDto usuario;

    private VueloReservaDto vuelo;

    private List<ButacaReservaDto> butacas;
}
