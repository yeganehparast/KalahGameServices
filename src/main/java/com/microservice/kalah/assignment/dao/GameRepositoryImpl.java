package com.microservice.kalah.assignment.dao;

import com.microservice.kalah.assignment.dto.GameDTO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class GameRepositoryImpl implements GameDAO {

    private Map<String, GameDTO> games = Maps.newConcurrentMap();
    private AtomicLong id = new AtomicLong(1);

    @Override
    public GameDTO save(GameDTO gameDTO) {
        long id = this.id.getAndIncrement();
        gameDTO.setGameId(String.valueOf(id));
        games.put(String.valueOf(id), gameDTO);
        return gameDTO;
    }

    @Override
    public GameDTO findById(String gameId) {
        return games.get(gameId);
    }

    @Override
    public void deleteAll() {
        id.set(1);
        games.clear();
    }

    @Override
    public long getSize() {
        return games.size();
    }

    @Override
    public long getId() {
        return id.get();
    }
}
