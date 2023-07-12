package com.codoacodo.flysky.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "butaca")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ButacaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean disponible;

    //A3, B34 etc posicion del asiento
    private String posicion;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vuelo_id", referencedColumnName="id", nullable = false)
    private VueloEntity vuelo;

    @OneToOne(mappedBy = "butaca")
    private ReservaEntity reserva;

}
