package com.empresa.accenture.pedidosenlinea.app.config.exception;

public class BadRequestException extends RuntimeException{

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
