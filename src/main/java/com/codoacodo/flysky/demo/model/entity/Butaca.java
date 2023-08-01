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

    private String posicion;

    private String nombrePasajero;
    //Podemos sustituir por una relacion OneToOne con private Pasajero pasajero para incluir más datos del pasajero

    @ManyToOne()
    @JoinColumn(name = "vuelo_id", referencedColumnName="id", nullable = false)
    private Vuelo vuelo;

    @ManyToOne()
    @JoinColumn(name = "reserva_id", referencedColumnName="id")
    private Reserva reserva;
}
