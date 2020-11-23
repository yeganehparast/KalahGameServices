package com.microservice.kalah.assignment.logic.manager;


import com.microservice.kalah.assignment.logic.model.Board;
import com.microservice.kalah.assignment.logic.model.Move;
import com.microservice.kalah.assignment.logic.model.MoveResult;

public interface BoardManager {

	MoveResult makeMove(Move move);

	Board getANewBoard();
}
