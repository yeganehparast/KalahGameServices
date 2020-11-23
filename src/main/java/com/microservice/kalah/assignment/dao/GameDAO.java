package com.microservice.kalah.assignment.dao;

import com.microservice.kalah.assignment.dto.GameDTO;

public interface GameDAO {

    GameDTO save(GameDTO gameDTO);

    GameDTO findById(String gameId);

    void deleteAll();

    long getSize();

    long getId();
}
