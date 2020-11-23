package com.microservice.kalah.assignment.converter;

import lombok.Data;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

@Data
public class BaseResponse {
    private String id;
    private String uri;


}
