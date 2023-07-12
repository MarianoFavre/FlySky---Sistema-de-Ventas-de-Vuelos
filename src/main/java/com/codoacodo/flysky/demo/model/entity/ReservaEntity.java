package com.codoacodo.flysky.demo.model.entity;

import com.codoacodo.flysky.demo.model.enums.TipoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reserva")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;

    private double montoPago;

    private LocalDate fechaReserva;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id", referencedColumnName="id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "vuelo_id", referencedColumnName="id", nullable = false)
    private VueloEntity vuelo;

    //private String posicionButaca;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "butaca_id", nullable = false)
    private ButacaEntity butaca;

}
