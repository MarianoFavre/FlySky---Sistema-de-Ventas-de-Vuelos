package com.codoacodo.flysky.demo.dto.request;

import com.codoacodo.flysky.demo.dto.response.ButacaDto;
import com.codoacodo.flysky.demo.dto.response.ReservaDto;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
public class ReservaVueloDto {

    //private Integer numeroVuelo;

    private String aerolinea;
    private LocalDateTime fechaHoraPartida;
    private LocalDateTime fechaHoraLlegada;
    private String origen;
    private String destino;

    private String posicionButaca;

    //En la solicitud no se envía ningún dato. Se utiliza solo para la respuesta.
    private LocalDateTime fechaHoraReserva;

    //User Story 3
    private TipoPago tipoPago;

}
