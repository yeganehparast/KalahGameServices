package com.microservice.kalah.assignment.converter;


import com.microservice.kalah.assignment.controller.GameController;
import lombok.Data;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Map;

@Data
public class MoveResponse extends BaseResponse {
    private Map<String, String> status;

    public MoveResponse toResponse(String id, Map<String, String> status) {
        this.setId(id);
        this.setStatus(status);
        this.setUri(WebMvcLinkBuilder.linkTo(GameController.class).slash("games").slash(this.getId()).toUriComponentsBuilder().port("8080").toUriString());
        return this;
    }


}
