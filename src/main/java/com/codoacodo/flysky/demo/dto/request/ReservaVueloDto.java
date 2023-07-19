package com.codoacodo.flysky.demo.dto.request;

import com.codoacodo.flysky.demo.dto.response.ButacaReservaDto;
import com.codoacodo.flysky.demo.model.entity.Butaca;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
public class ReservaVueloDto {

    private Long numeroVuelo;
    private List<ButacaReservaDto> butacas;
    private TipoPago tipoPago;

}
