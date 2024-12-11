package com.babymonitor.resultService;

import com.babymonitor.resultService.controller.ResultController;
import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.model.SimType;
import com.babymonitor.resultService.service.ResultServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResultController.class)
class ResultControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultServiceImpl resultService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Create a mock HttpServletRequest
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer mockToken");
    }

    @Test
    void whenGetAllResults_thenReturnJsonArray() throws Exception {
        Result result1 = new Result("Result 1", UUID.randomUUID(), 1, SimType.TRAINING);
        Result result2 = new Result("Result 2", UUID.randomUUID(), 2, SimType.TRAINING);
        List<Result> allResults = Arrays.asList(result1, result2);

        // Mock the service method to return results when called with any HttpServletRequest
        when(resultService.findByUser(any(HttpServletRequest.class))).thenReturn(allResults);

        mockMvc.perform(get("/result/byUser")
                        .header("Authorization", "Bearer mockToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].result", is("Result 1")))
                .andExpect(jsonPath("$[0].session", is(1)))
                .andExpect(jsonPath("$[1].result", is("Result 2")))
                .andExpect(jsonPath("$[1].session", is(2)))
                .andExpect(jsonPath("$[0].simType", is("TRAINING")));
    }

    @Test
    void whenGetSpecificResult_thenReturnJson() throws Exception {
        Result result = new Result("Test Result", UUID.randomUUID(), 123, SimType.TRAINING);
        result.setId("123");

        // Mock the service method to return a result when called with any HttpServletRequest
        when(resultService.findByUserAndSession(123, any(HttpServletRequest.class))).thenReturn(result);

        mockMvc.perform(get("/result/123")
                        .header("Authorization", "Bearer mockToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("Test Result")))
                .andExpect(jsonPath("$.session", is(123)))
                .andExpect(jsonPath("$.simType", is("TRAINING")));
    }

    @Test
    void whenAddNewResult_thenReturnSuccess() throws Exception {
        Result result = new Result("New Result", UUID.randomUUID(), 1, SimType.TRAINING);
        result.setId("generatedId");

        // Mock the service method to return a result ID
        when(resultService.addResult(any(Result.class))).thenReturn("generatedId");

        mockMvc.perform(post("/result/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk())
                .andExpect(content().string("generatedId"));
    }

    @Test
    void whenAddResultWithNullFields_thenReturnSuccess() throws Exception {
        Result result = new Result("Test Result", UUID.randomUUID(), 5, SimType.TRAINING);

        // Mock the service method to return a result ID
        when(resultService.addResult(any(Result.class))).thenReturn("generatedId");

        mockMvc.perform(post("/result/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk())
                .andExpect(content().string("generatedId"));
    }

    @Test
    void whenAddNewResultWithInvalidJson_thenReturn400() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/result/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}