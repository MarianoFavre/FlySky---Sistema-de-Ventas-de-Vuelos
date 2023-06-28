package com.codoacodo.flysky.demo.dto.response;

import com.codoacodo.flysky.demo.model.entity.ButacaEntity;
import com.codoacodo.flysky.demo.model.entity.ReservaEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
public class VueloDto {

    //private Integer numeroVuelo;

    private List<ReservaDto> reservas;

    private List<ButacaDto> butacas;

    private Boolean disponible;
    private Integer capacidad;
    private String aerolinea;
    private LocalDateTime horarioPartida;
    private LocalDateTime horarioLlegada;
    private Double precio;
    private String origen;
    private String destino;
}
