package com.microservice.kalah.assignment;

import com.microservice.kalah.assignment.logic.manager.BoardManager;
import com.microservice.kalah.assignment.logic.manager.BoardManagerImpl;
import com.microservice.kalah.assignment.logic.model.Board;
import com.microservice.kalah.assignment.logic.model.InvalidMoveException;
import com.microservice.kalah.assignment.logic.model.Move;
import com.microservice.kalah.assignment.logic.model.MoveResult;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.microservice.kalah.assignment.logic.model.MoveResult.*;
import static com.microservice.kalah.assignment.logic.model.PlayerEnum.PLAYER1;
import static com.microservice.kalah.assignment.logic.model.PlayerEnum.PLAYER2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BoardTest {

		private BoardManager boardManager;

		@BeforeEach
		public void init() {
			boardManager = new BoardManagerImpl();
		}

		@Test
		@DisplayName("Normal sow is done in new board correctly")
		public void sow() throws InvalidMoveException {
			// Given a board
			Board board = new Board();

			// When Player2 select the cell
			Move move = new Move(PLAYER1, board.getCellByIndex(1), board);
			MoveResult moveResult = boardManager.makeMove(move);

			// Expect
			// moveResult is ordinary
			assertEquals(ORDINARY, moveResult);
			//There selectedCell is empty
			assertEquals(0, board.getCellByIndex(1).getStoneCount());
			//The next 4 cells have 7 stones
			assertEquals(7, board.getCellByIndex(2).getStoneCount());
			assertEquals(7, board.getCellByIndex(3).getStoneCount());
			assertEquals(7, board.getCellByIndex(4).getStoneCount());
			assertEquals(7, board.getCellByIndex(5).getStoneCount());
			//The Home-1 Cell has 1 stone
			assertEquals(1, board.getHome(PLAYER1).getStoneCount());
			//One stone has been added to the first cell of Player2
			assertEquals(7, board.getCellByIndex(7).getStoneCount());
		}

		@Test
		@DisplayName("No stones are put in the opponents' big pit.")
		public void sowShouldNotPutStoneOnTheOpponentsHome() throws InvalidMoveException {
			// Given a board
			ArrayList<Integer> cells = Lists.newArrayList(0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 9, 0);
			Board board = new Board(cells, PLAYER2.toString());

			// When Player1 select the cell-1
			Move move = new Move(PLAYER2, board.getCellByIndex(12), board);
			MoveResult moveResult = boardManager.makeMove(move);

			// Expect
			// moveResult is ordinary
			assertEquals(ORDINARY, moveResult);
			//There is no stone in the Home1
			assertEquals(0, board.getHome(PLAYER1).getStoneCount());

		}

		@Test
		@DisplayName("If the player's last stone lands in his own big pit, he gets another turn.")
		void anotherTurnWhenLastCellIsHome() throws InvalidMoveException {
			// Given a board
			Board board = new Board();

			// When Player1 select cell-0
			Move move = new Move(PLAYER1, board.getCellByIndex(0), board);
			MoveResult moveResult = boardManager.makeMove(move);

			// Expect
			// moveResult is additional move
			assertEquals(ADDITIONAL_MOVE, moveResult);
			//Turn is still player1
			assertEquals(PLAYER1, board.getTurn());
			//There selectedCell is empty
			assertEquals(0, board.getCellByIndex(0).getStoneCount());
			//The next 5 cells have 7 stones
			assertEquals(7, board.getCellByIndex(1).getStoneCount());
			assertEquals(7, board.getCellByIndex(2).getStoneCount());
			assertEquals(7, board.getCellByIndex(3).getStoneCount());
			assertEquals(7, board.getCellByIndex(4).getStoneCount());
			assertEquals(7, board.getCellByIndex(5).getStoneCount());
			//The Home-1 Cell has 1 stone
			assertEquals(1, board.getHome(PLAYER1).getStoneCount());
		}


		@Test
		@DisplayName("Another turn can be repeated several times before it's the other player's turn.")
		void getANewBoard() throws InvalidMoveException {
			// Given a board
			ArrayList<Integer> cells = Lists.newArrayList(0, 0, 0, 0, 2, 1, 0, 1, 0, 0, 0, 1, 9, 0);
			Board board = new Board(cells, PLAYER1.toString());

			// When Player1 select the cell-1
			Move move = new Move(PLAYER1, board.getCellByIndex(5), board);
			MoveResult moveResult = boardManager.makeMove(move);
			//Player1 select the cell-2
			move = new Move(PLAYER1, board.getCellByIndex(4), board);
			moveResult = boardManager.makeMove(move);
			//Player1 select the cell-1
			move = new Move(PLAYER1, board.getCellByIndex(5), board);
			moveResult = boardManager.makeMove(move);

			// Expect
			// moveResult is another turn
			assertEquals(ADDITIONAL_MOVE, moveResult);
			//There is 3 stones in the Home1
			assertEquals(3, board.getHome(PLAYER1).getStoneCount());
			//The turn is still Player1
			assertEquals(board.getTurn(), PLAYER1);
		}

		@Test
		@DisplayName("Always when the last stone lands in an own empty pit, the player captures his own stone and all stones in the opposite pit(if it is not empty)")
		public void capturingTest() throws InvalidMoveException {
			// Given a board
			ArrayList<Integer> cells = Lists.newArrayList(0, 0, 0, 0, 1, 0, 0, 9, 0, 0, 0, 1, 9, 0);
			Board board = new Board(cells, PLAYER1.toString());

			// When Player1 select the cell-1
			Move move = new Move(PLAYER1, board.getCellByIndex(4), board);
			MoveResult moveResult = boardManager.makeMove(move);


			// Expect
			//Cell-4 & Cell-5 are empty
			assertTrue(board.getCellByIndex(4).isEmpty());
			assertTrue(board.getCellByIndex(5).isEmpty());
			//Cell-7 is also empty
			assertTrue(board.getCellByIndex(7).isEmpty());

			// moveResult is Captured
			assertEquals(CAPTURED, moveResult);
			//There is 10 stones in the Home1
			assertEquals(10, board.getHome(PLAYER1).getStoneCount());
			//The turn is Player2
			assertEquals(board.getTurn(), PLAYER2);
		}


		@Test
		@DisplayName("When opposite side cell is empty there is no capturing")
		public void noCapturingTest() throws InvalidMoveException {
			// Given a board
			ArrayList<Integer> cells = Lists.newArrayList(0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0);
			Board board = new Board(cells, PLAYER1.toString());

			// When Player1 select the cell-1
			Move move = new Move(PLAYER1, board.getCellByIndex(4), board);
			MoveResult moveResult = boardManager.makeMove(move);


			// Expect
			//Cell-4 is empty
			assertTrue(board.getCellByIndex(4).isEmpty());
			//Cell-5 has a stone
			assertEquals(1, board.getCellByIndex(5).getStoneCount());
			// moveResult is ordinary
			assertEquals(ORDINARY, moveResult);
			//There is no stone in the Home1
			assertTrue(board.getHome(PLAYER1).isEmpty());
			//The turn is Player2
			assertEquals(board.getTurn(), PLAYER2);
		}

}
