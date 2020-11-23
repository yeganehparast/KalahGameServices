package com.microservice.kalah.assignment.dto;

import lombok.Data;

@Data
public class MoveDTO {

	private String player;

	private String gameId;

	private int selectedCellIndex;

}
