package com.yappa.customerapi.infrastructure.adapter.in.rest.exception;

import com.yappa.customerapi.domain.exception.ClienteNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void notFound_retorna404ConMensaje() {
        ClienteNotFoundException ex = new ClienteNotFoundException("Cliente no encontrado");

        ResponseEntity<ApiError> response = handler.notFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("Cliente no encontrado");
        assertThat(response.getBody().details()).isEmpty();
        assertThat(response.getBody().timestamp()).isNotNull();
    }

    @Test
    void badRequest_retorna400ConMensaje() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<ApiError> response = handler.badRequest(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().message()).isEqualTo("Argumento inválido");
    }

    @Test
    void validation_retorna400ConDetallesDeCampos() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "nombre", "no puede estar vacío");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ApiError> response = handler.validation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).isEqualTo("Validación fallida");
        assertThat(response.getBody().details()).containsKey("nombre");
        assertThat(response.getBody().details().get("nombre")).isEqualTo("no puede estar vacío");
    }

    @Test
    void validation_fieldErrorConMensajeNull_usaInvalid() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "campo", null, false, null, null, null);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ApiError> response = handler.validation(ex);

        assertThat(response.getBody().details().get("campo")).isEqualTo("invalid");
    }

    @Test
    void constraint_retorna400() {
        ConstraintViolationException ex = new ConstraintViolationException("violación", Set.<ConstraintViolation<?>>of());

        ResponseEntity<ApiError> response = handler.constraint(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().message()).isEqualTo("Validación fallida");
        assertThat(response.getBody().details()).containsKey("violations");
    }

    @Test
    void conflict_retorna409() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("unique constraint");

        ResponseEntity<ApiError> response = handler.conflict(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().message()).contains("CUIT o Email");
    }

    @Test
    void generic_retorna500() {
        Exception ex = new RuntimeException("Error inesperado");

        ResponseEntity<ApiError> response = handler.generic(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().message()).isEqualTo("Internal Server Error");
    }
}
