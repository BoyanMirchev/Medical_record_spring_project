package com.example.Medical_record_project_Final.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.example.Medical_record_project_Final.exception.DoctorExaminationAccessDeniedException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.example.Medical_record_project_Final.web.api")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Object> handleDuplicateEntityException(
            DuplicateEntityException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage() != null ? ex.getMessage() : "Unexpected error occurred.",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "This record cannot be deleted because it is linked to other records.",
                request.getRequestURI()
        );
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DoctorExaminationAccessDeniedException.class)
    public ResponseEntity<Object> handleDoctorExaminationAccessDeniedException(
            DoctorExaminationAccessDeniedException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            org.springframework.http.HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> validationErrors = new LinkedHashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("messages", validationErrors);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);

        return new ResponseEntity<>(body, status);
    }
}