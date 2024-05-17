package marpetplace.api.infra;

import jakarta.persistence.EntityNotFoundException;
import marpetplace.api.dto.response.ErrorResponse;
import marpetplace.api.exception.EmailAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleError400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ErrorDataValidation::new).toList());
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity handleEmailAlreadyRegistered(EmailAlreadyRegisteredException ex){
        ErrorResponse errorResponse = new ErrorResponse("", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(List.of(errorResponse));
    }

    private record ErrorDataValidation(String field, String message) {
        public ErrorDataValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
