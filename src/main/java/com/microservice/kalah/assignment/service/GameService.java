package com.microservice.kalah.assignment.service;

import com.microservice.kalah.assignment.converter.BoardConverter;
import com.microservice.kalah.assignment.dao.GameDAO;
import com.microservice.kalah.assignment.domainvalue.MessageType;
import com.microservice.kalah.assignment.dto.BoardDTO;
import com.microservice.kalah.assignment.dto.GameDTO;
import com.microservice.kalah.assignment.dto.GameMessageDTO;
import com.microservice.kalah.assignment.dto.PlayerDTO;
import com.microservice.kalah.assignment.logic.manager.BoardManager;
import com.microservice.kalah.assignment.logic.model.*;
import com.microservice.kalah.assignment.logic.model.*;
import com.microservice.kalah.assignment.service.exception.GameNotFoundException;
import com.microservice.kalah.assignment.service.exception.IncorrectInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class GameService {


    private GameDAO gameDAO;

    private BoardManager boardManager;


    BoardConverter boardConverter = new BoardConverter();

    @Autowired
    public GameService(GameDAO gameDAO, BoardManager boardManager) {
        this.gameDAO = gameDAO;
        this.boardManager = boardManager;
    }

    public GameDTO createGame() {
        Board aNewBoard = boardManager.getANewBoard();

        BoardDTO boardDTO = boardConverter.toResource(aNewBoard);
        PlayerDTO playerDTO1 = new PlayerDTO(PlayerEnum.PLAYER1.getValue());
        PlayerDTO playerDTO2 = new PlayerDTO(PlayerEnum.PLAYER2.getValue());
        boardDTO.setTurn(PlayerEnum.PLAYER1.getValue());

        GameMessageDTO gameMessage = new GameMessageDTO(MessageType.INFO, String.format("Game created. gameId : %s", gameDAO.getId()));
        GameDTO game = new GameDTO(null, playerDTO1, playerDTO2, boardDTO, gameMessage);
        printLog(gameMessage);
        return gameDAO.save(game);
    }

    public Map<String, String> makeMove(String gameId, String pitId) throws GameNotFoundException, InvalidMoveException, IncorrectInputException {

        checkInput(gameId, pitId);
        GameDTO currentGame = gameDAO.findById(gameId);
        if (currentGame != null) {
            PlayerEnum currentPlayer = getCurrentPlayer(Integer.valueOf(pitId) - 1);
            PlayerDTO player = currentGame.getPlayer(currentPlayer.getValue());
            PlayerDTO opponent = currentGame.getOpponent(player);


            Board board = boardConverter.toModel(currentGame.getBoardDTO());
            Move moveModel;
            try {
                moveModel = new Move(currentPlayer,
                        board.getCellByIndex(Integer.valueOf(pitId) - 1),
                        board);
            } catch (InvalidMoveException e) {
                GameMessageDTO gameMessageDTO = new GameMessageDTO(MessageType.ERROR, e.getMessage());
                currentGame.setMessage(gameMessageDTO);
                printLog(gameMessageDTO);
                throw new InvalidMoveException(e.getMessage());
            }

            MoveResult moveResult = boardManager.makeMove(moveModel);
            Board resultedBoard = moveModel.getBoard();

            BoardDTO boardDTOResource = boardConverter.toResource(resultedBoard);
            currentGame.setBoardDTO(boardDTOResource);

            String message = null;
            switch (moveResult) {
                case CAPTURED:
                    message = String.format("%s did a nice capture. It is %s turn.", player.getUsername(), opponent.getUsername());
                    break;
                case ADDITIONAL_MOVE:
                    message = String.format("%s has another move.", player.getUsername());
                    break;
                case ORDINARY:
                    message = String.format("It is %s turn.", opponent.getUsername());
                    break;
            }

            if (resultedBoard.isFinished()) {
                int stoneCount1 = resultedBoard.getHome(PlayerEnum.PLAYER1).getStoneCount();
                int stoneCount2 = resultedBoard.getHome(PlayerEnum.PLAYER2).getStoneCount();
                if (stoneCount1 > stoneCount2) {
                    message = String.format("%s won the logic", currentGame.getPlayerDTO1().getUsername());
                }
                if (stoneCount2 > stoneCount1) {
                    message = String.format("%s won the logic", currentGame.getPlayerDTO2().getUsername());
                }
                if (stoneCount1 == stoneCount2) {
                    message = String.format("The logic was drawn");
                }
            }

            currentGame.setMessage(new GameMessageDTO(MessageType.INFO, message));
            printLog(currentGame.getMessage());
            return resultedBoard.getCellsStatus();
        } else {
            printLog(new GameMessageDTO(MessageType.ERROR, "No game with the input id was found!"));
            throw new GameNotFoundException("No game with the input id was found!");
        }
    }

    private void checkInput(String gameId, String pitId) {
        if (gameId == null || pitId == null) {
            throw new IncorrectInputException("gameId or pitId is null");
        } else if (gameId.isEmpty() || pitId.isEmpty()) {
            throw new IncorrectInputException("gameId or pitId is empty. pitId : " + pitId + " ,gameId : " + gameId);
        } else {
            try {
                int pitIdValue = Integer.parseInt(pitId);
                long gameIdValue = Long.parseLong(gameId);

                if (gameIdValue <= 0) {
                    throw new IncorrectInputException("pitId should be greater than zero. pitId: " + gameId);
                }

                if (pitIdValue <= 0) {
                    throw new IncorrectInputException("pitId should be greater than zero. pitId: " + pitId);
                }
                if (pitIdValue == 7 || pitIdValue == 14) {
                    throw new IncorrectInputException("pitId should not be 7 or 14. pitId: " + pitId);
                }
                if (pitIdValue > 14) {
                    throw new IncorrectInputException("pitId should be less than 14. pitId: " + pitId);
                }

            } catch (NumberFormatException e) {
                throw new IncorrectInputException("Number format expected for inputs. pitId : " + pitId + " ,gameId : " + gameId);
            }
        }
    }

    public PlayerEnum getCurrentPlayer(int pitId) {
        if (pitId < 7) {
            return PlayerEnum.PLAYER1;
        } else {
            return PlayerEnum.PLAYER2;
        }
    }

    private void printLog(GameMessageDTO gameMessageDTO) {
        if (gameMessageDTO.getType().equals(MessageType.INFO)) {
            log.info(gameMessageDTO.getMessage());
        } else {
            log.error(gameMessageDTO.getMessage());
        }
    }
}
