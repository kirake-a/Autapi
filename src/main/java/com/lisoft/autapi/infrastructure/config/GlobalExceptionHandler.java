package com.lisoft.autapi.infrastructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lisoft.autapi.application.dtos.ResponseWrapper;
import com.lisoft.autapi.domain.exceptions.ConflictWithExistingResourcesException;
import com.lisoft.autapi.domain.exceptions.InvalidArgumentException;
import com.lisoft.autapi.domain.exceptions.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle custom InvalidArgumentException
     * @param exception The exception to handle
     * @return ResponseEntity with error details included in the ResponseWrapper
     */
    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleInvalidArgument(InvalidArgumentException exception) {
        return new ResponseEntity<>(
            new ResponseWrapper<>(
                false,
                exception.getMessage(),
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle custom ConflictWithExistingResourcesException
     * @param exception
     * @return ResponseEntity with error details included in the ResponseWrapper
     */
    @ExceptionHandler(ConflictWithExistingResourcesException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleConflictWithExistingResources(ConflictWithExistingResourcesException exception) {
        return new ResponseEntity<>(
            new ResponseWrapper<>(
                false,
                exception.getMessage(),
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    /**
     * Handle custom ResourceNotFoundException
     * @param exception The exception to handle
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleResourceNotFound(ResourceNotFoundException exception) {
        return new ResponseEntity<>(
            new ResponseWrapper<>(
                false,
                exception.getMessage(),
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handle validation errors for @Valid annotated request bodies
     * @param exception The exception to handle
     * @return ResponseEntity with error details included in the ResponseWrapper
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult()
            .getAllErrors()
            .get(0)
            .getDefaultMessage();

        return new ResponseEntity<>(
            new ResponseWrapper<>(
                false,
                errorMessage,
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle ConstraintViolationException with @Validated on method parameters
     * @param exception The exception to handle
     * @return ResponseEntity with error details included in the ResponseWrapper
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleConstraintViolation(ConstraintViolationException exception) {
        return new ResponseEntity<>(
            new ResponseWrapper<>(
                false,
                exception.getMessage(),
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle all other exceptions
     * @param exception The exception to handle
     * @return ResponseEntity with error details included in the ResponseWrapper
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<Object>> handleGenericException(Exception exception) {
        String packageName = exception.getStackTrace().length > 0
            ? exception.getStackTrace()[0].getClassName()
            : "";

        // Ignora excepciones internas de Swagger o SpringDoc
        if (packageName.contains("springdoc") || packageName.contains("swagger")) {
            throw new RuntimeException(exception);
        }

        return new ResponseEntity<>(
            new ResponseWrapper<>(
                false,
                "An unexpected error occurred: " + exception.getMessage(),
                null
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
