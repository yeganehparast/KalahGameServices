package com.microservice.kalah.assignment.domainvalue;

public enum MessageType {
	INFO("info"), ERROR("error");

	private final String value;

	MessageType(String type) {
		this.value = type;
	}

	@Override
	public String toString() {
		return value;
	}
}
