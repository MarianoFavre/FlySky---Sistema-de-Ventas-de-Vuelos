package com.codoacodo.flysky.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
@EqualsAndHashCode
public class VueloReservaDto {

    private Long numeroVuelo;
    private String aerolinea;
    private LocalDateTime fechaHoraPartida;
    private LocalDateTime fechaHoraLlegada;
    private String origen;
    private String destino;

}
