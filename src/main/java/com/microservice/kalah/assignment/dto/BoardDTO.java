package com.microservice.kalah.assignment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardDTO {

	private List<Integer> cells;

	private String turn;

	private Boolean finished;

	public BoardDTO(List<Integer> cells, String turn, Boolean finished) {
		this.cells = cells;
		this.turn = turn;
		this.finished = finished;
	}

}
