package com.codoacodo.flysky.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExceptionDto {
    private int statusCode; //se lo enviamos por body a pesar de que lo vemos por Postman en la response
    private String message;
}
