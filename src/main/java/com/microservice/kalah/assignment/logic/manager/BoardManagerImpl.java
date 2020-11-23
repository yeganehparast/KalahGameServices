package com.microservice.kalah.assignment.logic.manager;

import com.microservice.kalah.assignment.logic.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;


@Slf4j
@Component
public class BoardManagerImpl implements BoardManager {


    @Override
    public MoveResult makeMove(Move move) {

        BoardCell lastCell = sowTheStonesAngGetLastCell(move);
        MoveResult moveResult = MoveResult.ORDINARY;
        moveResult = checkAndPerformCapturing(move, lastCell, moveResult);
        moveResult = specifyNextTurn(move, lastCell, moveResult);
        checkIfTheGameIsFinished(move);

        return moveResult;
    }

    @Override
    public Board getANewBoard() {
        return new Board();
    }

    private void checkIfTheGameIsFinished(Move move) {
        Board board = move.getBoard();
        if (board.isFinished()) {
            board.terminateBoard();
        }
    }

    private MoveResult checkAndPerformCapturing(Move move, BoardCell lastCell, MoveResult moveResult) {
        Board board = move.getBoard();
        PlayerEnum player = move.getPlayer();
        BoardCell oppositeCell = lastCell.getOppositeCell();

        if (!lastCell.isHome() && lastCell.isSingletone() && !oppositeCell.isEmpty() && lastCell.getOwner() == move.getPlayer()) {
            int winningStonesCount = lastCell.getStoneCount() + oppositeCell.getStoneCount();
            board.getHome(player).incrementStoneBy(winningStonesCount);
            oppositeCell.emptyStones();
            lastCell.emptyStones();
            return MoveResult.CAPTURED;
        }
        return moveResult;
    }

    private MoveResult specifyNextTurn(Move move, BoardCell lastCell, MoveResult moveResult) {
        Board board = move.getBoard();
        PlayerEnum player = move.getPlayer();
        if (!lastCell.isHome() || lastCell.getOwner() != player) {
            board.toggleTurn();
            return moveResult;
        }
        return MoveResult.ADDITIONAL_MOVE;

    }

    private BoardCell sowTheStonesAngGetLastCell(Move move) {
        Board board = move.getBoard();
        BoardCell selectedCell = move.getSelectedCell();
        PlayerEnum player = move.getPlayer();
        int stoneCount = selectedCell.getStoneCount();
        selectedCell.emptyStones();
        Stream<BoardCell> stream = board.getNextFillableBordCellsStreamForPlayer(selectedCell, player, stoneCount);
        return stream
                .peek(BoardCell::incrementStoneCountByOne)
                .skip(stoneCount - 1).findFirst().get();

    }

}
