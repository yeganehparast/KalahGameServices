package com.microservice.kalah.assignment.logic.model;

import com.microservice.kalah.assignment.exception.KalahException;

public class InvalidMoveException extends KalahException {
	public InvalidMoveException(String message) {
		super(message);
	}
}
