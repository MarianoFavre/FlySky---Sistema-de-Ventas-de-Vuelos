package com.codoacodo.flysky.demo.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
@EqualsAndHashCode
@ToString
public class ButacaReservaDto {

    @NotBlank (message = "La posición de la butaca a reservar no puede ser nula, vacía o con espacio en blanco.")
    private String posicion;
    @NotBlank (message = "El nombre del pasajero de la butaca a reservar no puede ser nulo, vacío o con espacio en blanco.")
    private String nombrePasajero;
}
