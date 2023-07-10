package com.codoacodo.flysky.demo.dto.response;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
@EqualsAndHashCode
public class VentaDto {

    private LocalDate fecha;
    private Integer cantidadVenta;
    private Double ingreso;

}
