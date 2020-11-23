package com.microservice.kalah.assignment.logic.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Move {

	private final PlayerEnum player;

	private final BoardCell selectedCell;

	private final Board board;

	public Move(PlayerEnum player, BoardCell selectedCell, Board board) throws InvalidMoveException {
		this.player = player;
		this.selectedCell = selectedCell;
		this.board = board;
		checkMoveIsValid();
	}

	private void checkMoveIsValid() throws InvalidMoveException {
		raiseInvalidMoveExceptionIf(board.getTurn() != player, String.format("It is not %s turn", player));

		raiseInvalidMoveExceptionIf(selectedCell.isHome(), "Can not choose home cell");

		raiseInvalidMoveExceptionIf(selectedCell.getStoneCount() == 0, "Can not select empty cell");

		raiseInvalidMoveExceptionIf(selectedCell.getOwner() != player, String.format("%s choose an invalid cell", player));

	}

	private void raiseInvalidMoveExceptionIf(boolean errorCondition, String message) throws InvalidMoveException {
		if (errorCondition) {
			log.error(message);
			throw new InvalidMoveException(message);
		}
	}
}
