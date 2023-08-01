package com.codoacodo.flysky.demo;

import com.codoacodo.flysky.demo.model.enums.TipoPago;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Util {

    public static Double montoAPagar(TipoPago tipoPago, Double precio) {

        Double montoPago = precio;

        switch (tipoPago) {
            case TARJETA_CREDITO:
                montoPago *= 1.53;
                break;
            case TARJETA_DEBITO:
                montoPago *= 1;
                break;
            case TRANSFERENCIA_BANCARIA:
                montoPago *= 1;
                break;
            case PAGO_EN_LINEA:
                montoPago *= 0.9;
                break;
            //default:
            // code block
        }

        return montoPago;
    }

    public static String fechaString(LocalDate fecha) {

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

        return formatter.format(fecha);

    }
}
