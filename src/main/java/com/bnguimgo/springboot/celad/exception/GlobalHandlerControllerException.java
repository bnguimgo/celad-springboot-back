package com.bnguimgo.springboot.celad.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackages = {"com.bnguimgo.springboot.celad"})
public class GlobalHandlerControllerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)//toutes les autres erreurs non gérées sont interceptées ici
    public ResponseEntity<BusinessResourceExceptionDTO> uncatchError(HttpServletRequest req, Exception ex) {
        BusinessResourceExceptionDTO response = new BusinessResourceExceptionDTO();
        response.setErrorCode("Technical Error");
        response.setErrorMessage(ex.getMessage());
        response.setRequestURL(req.getRequestURL().toString());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessResourceException.class)
    public ResponseEntity<BusinessResourceExceptionDTO> businessResourceError(HttpServletRequest req, BusinessResourceException ex) {
        BusinessResourceExceptionDTO businessResourceExceptionDTO = new BusinessResourceExceptionDTO();
        businessResourceExceptionDTO.setStatus(ex.getStatus());
        businessResourceExceptionDTO.setErrorCode(ex.getErrorCode());
        businessResourceExceptionDTO.setErrorMessage(ex.getMessage());
        businessResourceExceptionDTO.setRequestURL(req.getRequestURL().toString());
        return new ResponseEntity<>(businessResourceExceptionDTO, ex.getStatus());
    }

}