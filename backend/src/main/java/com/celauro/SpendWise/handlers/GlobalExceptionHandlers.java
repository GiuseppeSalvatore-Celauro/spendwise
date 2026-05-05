package com.celauro.SpendWise.handlers;

import com.celauro.SpendWise.dtos.ErrorDTO;
import com.celauro.SpendWise.dtos.ValidationErrDTO;
import com.celauro.SpendWise.exceptions.ErrorDateException;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.exceptions.NotValidType;
import com.celauro.SpendWise.exceptions.UnauthorizedUserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
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

        log.error("not valid arguments in the request", e);
        return error;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO NotFoundException(NotFoundException e, HttpServletRequest request){
        log.error(e.getMessage());
        return errorFormatter(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ErrorDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO ErrorDateException(ErrorDateException e, HttpServletRequest request){
        log.error(e.getMessage());
        return errorFormatter(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotValidType.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO ErrorDateException(NotValidType e, HttpServletRequest request){
        log.error(e.getMessage());
        return errorFormatter(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO ErrorDateException(UnauthorizedUserException e, HttpServletRequest request){
        log.error(e.getMessage());
        return errorFormatter(e, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO Exception(Exception e, HttpServletRequest request){
        log.error("internal server error", e);
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
