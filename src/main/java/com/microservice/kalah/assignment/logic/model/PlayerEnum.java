package com.microservice.kalah.assignment.logic.model;

public enum PlayerEnum {
	PLAYER1("Player1"),
	PLAYER2("Player2");

	private final String value;

	private PlayerEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.getValue();
	}

	public PlayerEnum toggle() {
		if (this == PLAYER2) {
			return PLAYER1;
		}
		return PLAYER2;
	}
}
