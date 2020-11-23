package com.microservice.kalah.assignment;

import com.microservice.kalah.assignment.dao.GameDAO;
import com.microservice.kalah.assignment.domainvalue.MessageType;
import com.microservice.kalah.assignment.dto.BoardDTO;
import com.microservice.kalah.assignment.dto.GameDTO;
import com.microservice.kalah.assignment.dto.GameMessageDTO;
import com.microservice.kalah.assignment.logic.model.InvalidMoveException;
import com.microservice.kalah.assignment.logic.model.PlayerEnum;
import com.microservice.kalah.assignment.service.GameService;
import com.microservice.kalah.assignment.service.exception.GameNotFoundException;
import com.microservice.kalah.assignment.service.exception.IncorrectInputException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {

    @Autowired
    GameService gameService;

    @Autowired
    GameDAO gameDAO;

    private GameDTO game;

    @BeforeEach
    public void init() {
        game = gameService.createGame();
    }

    @DisplayName("tests switch between players with different moves. Should exceptions be thrown with invalid moves")
    @Test
    @Order(1)
    void testSwitchBetweenPlayersWithDifferentMoves() {

        String turn = game.getBoardDTO().getTurn();
        assertEquals(turn, "Player1");

        //pitId 1 belongs to Player1
        gameService.makeMove(game.getGameId(), "1");
        assertEquals(gameService.getCurrentPlayer(1), PlayerEnum.PLAYER1);
        GameDTO gameDTO = gameDAO.findById("1");

        assertThat(gameDTO.getBoardDTO().getTurn()).as("current turn should be player1").
                isEqualTo(PlayerEnum.PLAYER1.getValue());
        assertEquals(PlayerEnum.PLAYER1.getValue(), gameDTO.getBoardDTO().getTurn());

        //Choose from player2 pits --> InvalidMoveException
        assertThrows(InvalidMoveException.class, () -> {
            gameService.makeMove("1", "10");
        });


        //Next Move
        assertEquals("It is not Player2 turn", gameDTO.getMessage().getMessage());
        //pitId 3 belongs to Player1
        gameService.makeMove(game.getGameId(), "3");
        assertEquals(gameService.getCurrentPlayer(3), PlayerEnum.PLAYER1);
        gameDTO = gameDAO.findById("1");
        assertThat(gameDTO.getBoardDTO().getTurn()).as("current turn should be player2").
                isEqualTo(PlayerEnum.PLAYER2.getValue());
        assertEquals(PlayerEnum.PLAYER2.getValue(), gameDTO.getBoardDTO().getTurn());

        //Next Move
        assertEquals("It is Player2 turn.", gameDTO.getMessage().getMessage());
        //pitId 8 belongs to Player2
        gameService.makeMove(game.getGameId(), "8");
        assertEquals(gameService.getCurrentPlayer(8), PlayerEnum.PLAYER2);
        gameDTO = gameDAO.findById("1");
        assertThat(gameDTO.getBoardDTO().getTurn()).as("current turn should be player1").
                isEqualTo(PlayerEnum.PLAYER1.getValue());
        assertEquals(PlayerEnum.PLAYER1.getValue(), gameDTO.getBoardDTO().getTurn());
        assertEquals("It is Player1 turn.", gameDTO.getMessage().getMessage());
    }

    @Test
    @DisplayName("Tests throwing appropriate exception")
    @Order(2)
    public void testExceptions() {

        //negative values
        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("1", "-1");
        });
        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("-1", "1");
        });
        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("-1", "-1");
        });
        //Non numeric values
        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("a", "1");
        });
        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("1", "a");
        });
        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("a", "a");
        });
        //forbidden index e.g. 7 , 14 , 0 , 15 ...
        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("1", "7");
        });
        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("1", "14");
        });

        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("2", "15");
        });

        assertThrows(IncorrectInputException.class, () -> {
            gameService.makeMove("2", "0");
        });

        // Not created game
        assertNull(gameDAO.findById("3"));
        assertThrows(GameNotFoundException.class, () -> {
            gameService.makeMove("3", "1");
        });
    }

    @Test
    @DisplayName("test initialization of game correctly")
    @Order(3)
    public void testGameInitialized() {
        assertNotNull(game);
        assertNotNull(game.getGameId());
        assertEquals("3", game.getGameId());
        BoardDTO boardDTO = game.getBoardDTO();

        assertNotNull(boardDTO);
        assertNotNull(game.getPlayerDTO1());
        assertEquals(PlayerEnum.PLAYER1.getValue(), game.getPlayerDTO1().getUsername());
        assertNotNull(game.getPlayerDTO2());
        assertEquals(PlayerEnum.PLAYER2.getValue(), game.getPlayerDTO2().getUsername());
        assertNotNull(game.getMessage());
        assertEquals(game.getMessage(), new GameMessageDTO(MessageType.INFO, "Game created. gameId : 3"));


        assertNotNull(boardDTO.getTurn());
        assertEquals(PlayerEnum.PLAYER1.getValue(), boardDTO.getTurn());
        assertNotNull(boardDTO.getCells());
        assertEquals(Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), boardDTO.getCells());
        assertNotNull(boardDTO.getFinished());
        assertFalse(boardDTO.getFinished());
    }

    @Test
    @DisplayName("test board cells after a move and compare with memory")
    @Order(4)
    public void testBoardCellsAfterMove() {
        Map<String, String> status = gameService.makeMove(game.getGameId(), "1");
        Assertions.assertEquals(TestUtils.expectedStatus, status);
        GameDTO gameDAOById = gameDAO.findById(game.getGameId());
        assertNotNull(gameDAOById);
        BoardDTO boardDTO = gameDAOById.getBoardDTO();
        assertNotNull(boardDTO);
        Assertions.assertEquals(TestUtils.expectedStatus.get("1"), String.valueOf(boardDTO.getCells().get(0)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("2"), String.valueOf(boardDTO.getCells().get(1)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("3"), String.valueOf(boardDTO.getCells().get(2)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("4"), String.valueOf(boardDTO.getCells().get(3)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("5"), String.valueOf(boardDTO.getCells().get(4)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("6"), String.valueOf(boardDTO.getCells().get(5)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("7"), String.valueOf(boardDTO.getCells().get(6)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("8"), String.valueOf(boardDTO.getCells().get(7)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("9"), String.valueOf(boardDTO.getCells().get(8)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("10"), String.valueOf(boardDTO.getCells().get(9)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("11"), String.valueOf(boardDTO.getCells().get(10)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("12"), String.valueOf(boardDTO.getCells().get(11)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("13"), String.valueOf(boardDTO.getCells().get(12)));
        Assertions.assertEquals(TestUtils.expectedStatus.get("14"), String.valueOf(boardDTO.getCells().get(13)));
    }

}
