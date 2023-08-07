package com.codoacodo.flysky.demo.exception;

import com.codoacodo.flysky.demo.dto.response.ExceptionDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entidadNoEncontrada(EntityNotFoundException e) {
        ExceptionDto exceptionDto = new ExceptionDto(404, e.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<?> usuarioNoAutorizado(UnAuthorizedException e) {
        ExceptionDto exceptionDto = new ExceptionDto(401, e.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> elementoNoExiste(NoSuchElementException e) {
        ExceptionDto exceptionDto = new ExceptionDto(500, e.getMessage());
        //.replace("No value present", "Usuario no registrado."));
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> argumentoIlegal(IllegalArgumentException e) {
        ExceptionDto exceptionDto = new ExceptionDto(400, e.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    //METODO PARA CAPTURAR VARIOS ERRORES POR VALIDACION DE @PathVariable o @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> validacionErronea(ConstraintViolationException exception) {

        List<ExceptionDto> exceptionDto = new ArrayList<>();
        exception.getConstraintViolations()
                .forEach(error -> exceptionDto.add(new ExceptionDto(500, error.getMessage())));

        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //METODO PARA CAPTURAR VARIOS ERRORES POR VALIDACION DE @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validacionErronea(MethodArgumentNotValidException exception) {

        List<ExceptionDto> exceptionDto = new ArrayList<>();
        exception.getAllErrors()
                .forEach(error -> exceptionDto.add(new ExceptionDto(400, error.getDefaultMessage())));

        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    //Excepción lanzada y manejada por Spring
    //JSON parse error: Cannot deserialize value of type `com.codoacodo.flysky.demo.model.enums.TipoPago` from String.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> mensajeIlegible(HttpMessageNotReadableException e) {
        ExceptionDto exceptionDto = new ExceptionDto(400, e.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    //Excepción lanzada y manejada por Spring
    //Required parameter 'x' (nombreUsuario, fecha) is not present.
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> faltaParametroSolicitudServlet(MissingServletRequestParameterException e) {
        ExceptionDto exceptionDto = new ExceptionDto(400, e.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    //Excepción lanzada y manejada por Spring
    //Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> discrepanciaDeTipoDeArgumentoDeMetodo(MethodArgumentTypeMismatchException e) {
        ExceptionDto exceptionDto = new ExceptionDto(400, e.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }


    //Primero valida @RequestBody y después @RequestParam o @PathVariable. Dentro del @RequestBody valida primero los enum.

}

