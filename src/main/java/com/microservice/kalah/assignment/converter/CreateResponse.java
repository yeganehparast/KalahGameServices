package com.microservice.kalah.assignment.converter;

import com.microservice.kalah.assignment.controller.GameController;
import com.microservice.kalah.assignment.dto.GameDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode
public class CreateResponse extends BaseResponse {

    public CreateResponse toResponse(GameDTO gameDTO) {
        this.setId(gameDTO.getGameId());
        Map<String, String> params = new HashMap<>();
        //params.put("{port}","8080");
        this.setUri(WebMvcLinkBuilder.linkTo(GameController.class).slash("games").slash(this.getId()).toUriComponentsBuilder().port("8080").toUriString());
        return this;
    }
}
