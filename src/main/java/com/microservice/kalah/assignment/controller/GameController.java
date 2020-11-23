package com.microservice.kalah.assignment.controller;

import com.microservice.kalah.assignment.converter.CreateResponse;
import com.microservice.kalah.assignment.converter.MoveResponse;
import com.microservice.kalah.assignment.dto.GameDTO;
import com.microservice.kalah.assignment.logic.model.InvalidMoveException;
import com.microservice.kalah.assignment.service.GameService;
import com.microservice.kalah.assignment.service.exception.GameNotFoundException;
import com.microservice.kalah.assignment.service.exception.IncorrectInputException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Api(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Creates a new game")
    @PostMapping(path = "/games", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateResponse> createGame() {
        GameDTO game = gameService.createGame();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateResponse()
                        .toResponse(game));
    }

    @ApiOperation(value = "Moves stones of selected pitId in the selected gameId and returns the final status of the game")
    @PutMapping(value = "/games/{gameId}/pits/{pitId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MoveResponse> move(@PathVariable(name = "gameId") String gameId,
                                             @PathVariable(name = "pitId") String pitId,
                                             HttpServletResponse response) {
        try {
            return ResponseEntity.
                    ok(new MoveResponse().
                            toResponse(gameId, gameService.makeMove(gameId, pitId)));
        } catch (GameNotFoundException gnfe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, gnfe.getMessage());
        } catch (IncorrectInputException iie) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, iie.getMessage());
        } catch (InvalidMoveException iime) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, iime.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
