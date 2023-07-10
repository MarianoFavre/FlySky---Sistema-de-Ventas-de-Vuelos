package com.codoacodo.flysky.demo.dto.response;


import com.codoacodo.flysky.demo.model.enums.TipoUsuario;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
@EqualsAndHashCode
public class UsuarioDto {

    private String nombreUsuario;
    private Integer telefono;
}
