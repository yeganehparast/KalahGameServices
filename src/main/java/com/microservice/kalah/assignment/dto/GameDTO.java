package com.microservice.kalah.assignment.dto;

import com.microservice.kalah.assignment.logic.model.PlayerEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDTO {
    String gameId;

    PlayerDTO playerDTO1;

    PlayerDTO playerDTO2;

    BoardDTO boardDTO;

    GameMessageDTO message;

    public PlayerDTO getPlayer(String palyer) {
        if (playerDTO1.getUsername().equals(palyer)) {
            return playerDTO1;
        }
        if (playerDTO2.getUsername().equals(palyer)) {
            return playerDTO2;
        }
        return null;
    }

    public PlayerDTO getOpponent(PlayerDTO playerDTO) {
        if (playerDTO.equals(playerDTO1)) {
            return playerDTO2;
        }
        return playerDTO1;
    }

    public PlayerEnum getPlayerEnum(PlayerDTO playerDTO) {
        if (playerDTO.equals(playerDTO1)) {
            return PlayerEnum.PLAYER1;
        }
        return PlayerEnum.PLAYER2;
    }
}
