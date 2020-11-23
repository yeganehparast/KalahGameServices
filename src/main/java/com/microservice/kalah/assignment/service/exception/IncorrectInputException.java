package com.microservice.kalah.assignment.service.exception;

import com.microservice.kalah.assignment.exception.KalahException;

public class IncorrectInputException extends KalahException {
    public IncorrectInputException(String message) {
        super(message);
    }
}
