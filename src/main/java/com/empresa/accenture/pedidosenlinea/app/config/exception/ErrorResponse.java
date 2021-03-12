package com.empresa.accenture.pedidosenlinea.app.config.exception;

import java.util.Date;
import java.util.List;

public class ErrorResponse {
    private int status;

    private String message;

    private Date timestamp;

    List<String> errors;

    ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message, Date timestamp, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
