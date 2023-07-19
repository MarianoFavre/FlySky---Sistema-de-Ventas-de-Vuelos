package com.codoacodo.flysky.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "butaca")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Butaca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean disponible;

    //A3, B34 etc posicion del asiento
    private String posicion;

    //private String nombrePasajero;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vuelo_id", referencedColumnName="id", nullable = false)
    private Vuelo vuelo;

    //@OneToOne(mappedBy = "butaca")
    //private Reserva reserva;

    @ManyToOne()
    @JoinColumn(name = "reserva_id", referencedColumnName="id")
    private Reserva reserva;

}
