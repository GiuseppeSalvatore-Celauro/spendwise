package com.celauro.SpendWise.handlers;

import com.celauro.SpendWise.utils.Logger;
import com.celauro.SpendWise.dtos.ErrorDTO;
import com.celauro.SpendWise.dtos.ValidationErrDTO;
import com.celauro.SpendWise.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandlers {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO MethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        List<ValidationErrDTO> validationErr = e.getBindingResult()
                                                .getFieldErrors()
                                                .stream()
                                                .map(err -> new ValidationErrDTO(err.getField(), err.getDefaultMessage()))
                                                .toList();

        ErrorDTO error = errorFormatter(e, request, HttpStatus.BAD_REQUEST);
        error.setMessages(validationErr);
        error.setMessage(null);
        return error;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO NotFoundException(NotFoundException e, HttpServletRequest request){
        Logger.error("Transazione non trovata", e);
        return errorFormatter(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO Exception(Exception e, HttpServletRequest request){
        Logger.error("Errore interno al server", e);
        return errorFormatter(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorDTO errorFormatter(Exception e, HttpServletRequest request, HttpStatus status){
        ErrorDTO error = new ErrorDTO();
        error.setStatus(status.value());
        error.setTimestamp(System.currentTimeMillis());
        error.setError(status.getReasonPhrase());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        error.setMethod(request.getMethod());
        return error;
    }
}
