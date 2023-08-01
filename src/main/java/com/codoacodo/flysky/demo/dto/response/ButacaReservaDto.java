package com.codoacodo.flysky.demo.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
@EqualsAndHashCode
@ToString
public class ButacaReservaDto {

    private String posicion;
    private String nombrePasajero;
}
