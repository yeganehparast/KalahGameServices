package com.microservice.kalah.assignment;

import com.microservice.kalah.assignment.converter.CreateResponse;
import com.microservice.kalah.assignment.converter.MoveResponse;
import com.microservice.kalah.assignment.dao.GameDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameControllerTest {

    @Autowired
    private GameDAO gameDAO;
    @Autowired
    private WebApplicationContext wac;

    private static String lastGameId;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Order(1)
    @Test
    @DisplayName("Check WebApplicationContext to be initialized correctly and gameController is instantiated")
    public void checkContext() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("gameController"));
    }

    @Order(2)
    @Test
    @DisplayName("Tests createGame endpoint to creates a game")
    public void testPostCreateGame() throws Exception {
        gameDAO.deleteAll();
        MvcResult mvcResult = mockMvc.perform(post("/games").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        objectMapper = new ObjectMapper();
        String content = mvcResult.getResponse().getContentAsString();
        CreateResponse createResponse = objectMapper.readValue(content, CreateResponse.class);
        CreateResponse expected = objectMapper.readValue("{\"id\":\"1\",\"uri\":\"http://localhost:8080/games/1\"}", CreateResponse.class);

        assertEquals(expected.getId(), createResponse.getId());
        assertEquals(expected.getUri(), createResponse.getUri());
        lastGameId = createResponse.getId();
    }

    @Order(3)
    @Test
    @DisplayName("Tests move endpoint to make move for a pitId")
    public void testPutMove() throws Exception {

        MvcResult mvcResult = mockMvc.perform(put("/games/{gameId}/pits/{pitId}", lastGameId, "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        objectMapper = new ObjectMapper();
        String content = mvcResult.getResponse().getContentAsString();
        MoveResponse response = objectMapper.readValue(content, MoveResponse.class);
        MoveResponse expected = new MoveResponse().toResponse(lastGameId, TestUtils.expectedStatus);
        assertEquals(expected.getId(), response.getId());
        assertEquals(expected.getUri(), response.getUri());
        assertEquals(expected.getStatus(), response.getStatus());
    }

}
