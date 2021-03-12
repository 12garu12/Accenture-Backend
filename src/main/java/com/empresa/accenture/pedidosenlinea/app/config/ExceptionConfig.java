package com.empresa.accenture.pedidosenlinea.app.config;


import com.empresa.accenture.pedidosenlinea.app.config.exception.BadRequestException;
import com.empresa.accenture.pedidosenlinea.app.config.exception.ErrorResponse;
import com.empresa.accenture.pedidosenlinea.app.config.exception.NotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Date;
import java.util.List;

@ControllerAdvice //(annotations = RestController.class)
public class ExceptionConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exc) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(httpStatus, exc);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestException(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse>  handleException(DataAccessException e){
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
        error.setTimestamp(new Date());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus httpStatus, Exception exc) {
        return buildResponseEntity(httpStatus, exc, null);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus httpStatus, Exception exc, List<String> errors) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage("USRMSG-" + exc.getMessage());
        error.setStatus(httpStatus.value());
        error.setTimestamp(new Date());
        error.setErrors(errors);
        return new ResponseEntity<>(error, httpStatus);

    }

}
