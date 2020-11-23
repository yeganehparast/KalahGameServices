package com.microservice.kalah.assignment.dto;

import com.microservice.kalah.assignment.domainvalue.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class GameMessageDTO {

	private MessageType type;

	private String message;

}
