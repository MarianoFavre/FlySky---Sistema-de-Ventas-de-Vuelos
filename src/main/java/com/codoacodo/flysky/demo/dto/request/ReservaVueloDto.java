package com.codoacodo.flysky.demo.dto.request;

import com.codoacodo.flysky.demo.dto.response.ButacaReservaDto;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
public class ReservaVueloDto {

    @NotBlank(message = "El número de vuelo a reservar no puede ser nulo, vacío o en blanco.")
    @Size(min=1, max=3, message = "El número de vuelo debe poseer entre 1 y 3 caracteres.")
    private String numeroVuelo;

    @NotEmpty(message = "La lista de butacas a reservar no puede ser nula o vacía.")
    @Valid
    private List<ButacaReservaDto> butacas;

    @NotNull(message = "El tipo de pago no puede ser nulo.")
    private TipoPago tipoPago;

}
