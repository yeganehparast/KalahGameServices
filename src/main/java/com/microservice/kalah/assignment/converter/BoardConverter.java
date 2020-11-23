package com.microservice.kalah.assignment.converter;



import com.microservice.kalah.assignment.dto.BoardDTO;
import com.microservice.kalah.assignment.logic.model.Board;

import java.util.List;


public class BoardConverter {

	public BoardDTO toResource(Board board) {
		BoardDTO resource = new BoardDTO(
				board.getCells(),
				board.getTurn().toString(),
				board.isFinished()
		);
		return resource;
	}

	public Board toModel(BoardDTO boardDTO) {
		List<Integer> cells = boardDTO.getCells();
		String turn = boardDTO.getTurn();
		return new Board(cells, turn);
	}

}
