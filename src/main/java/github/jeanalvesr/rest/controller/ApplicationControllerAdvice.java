package github.jeanalvesr.rest.controller;

import github.jeanalvesr.exception.AtributoFaltanteException;
import github.jeanalvesr.exception.DataException;
import github.jeanalvesr.exception.VendedorNaoExistenteException;
import github.jeanalvesr.rest.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(DataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleDataException(DataException de){
        String mensagemErro = de.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(AtributoFaltanteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleAtributoFaltanteException(AtributoFaltanteException afe){
        String mensagemErro = afe.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(VendedorNaoExistenteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleVendedorExistenteException(VendedorNaoExistenteException vee){
        String mensagemErro = vee.getMessage();
        return new ApiErrors(mensagemErro);
    }

}
