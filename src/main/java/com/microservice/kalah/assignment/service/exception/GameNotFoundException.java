package com.microservice.kalah.assignment.service.exception;

import com.microservice.kalah.assignment.exception.KalahException;

public class GameNotFoundException extends KalahException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
