package com.codoacodo.flysky.demo.model.entity;

import com.codoacodo.flysky.demo.model.enums.TipoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reserva")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;

    private double montoPago;

    private LocalDateTime fechaReserva;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id", referencedColumnName="id", nullable = false)
    private Usuario usuario;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "vuelo_id", referencedColumnName="id", nullable = false)
    private Vuelo vuelo;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Butaca> butacas;

}
