package co.edu.fcv.citas.adapter.http;

import co.edu.fcv.citas.application.IdentityException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiErrors {
    @ExceptionHandler(IdentityException.class)
    ResponseEntity<ApiError> identity(IdentityException ex) {
        HttpStatus status = ex.code().endsWith("EXISTS") ? HttpStatus.CONFLICT : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(new ApiError(ex.code(), ex.getMessage(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> invalid(MethodArgumentNotValidException ex) {
        var fields = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getField()).distinct().sorted().toList();
        return ResponseEntity.badRequest().body(new ApiError("VALIDATION_ERROR", "Datos inválidos", fields));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiError> malformed(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ApiError("INVALID_JSON", "Solicitud inválida", List.of()));
    }

    record ApiError(String code, String message, List<String> fields) {}
}
