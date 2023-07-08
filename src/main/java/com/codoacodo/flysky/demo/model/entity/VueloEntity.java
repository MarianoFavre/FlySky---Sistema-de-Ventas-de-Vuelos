package com.codoacodo.flysky.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vuelo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VueloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numeroVuelo;

    private Boolean disponible;
    private Integer capacidad;
    private String aerolinea;
    private LocalDateTime fechaHoraPartida;
    private LocalDateTime fechaHoraLlegada;
    private Double precio;
    private String origen;
    private String destino;

    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL)
    private List<ReservaEntity> reservas;

    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL)
    private List<ButacaEntity> butacas;
}
