package com.babymonitor.resultService;

import com.babymonitor.resultService.controller.ResultController;
import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.model.SimType;
import com.babymonitor.resultService.service.ResultServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ResultController.class)
class ResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultServiceImpl resultService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAllResults_thenReturnJsonArray() throws Exception {
        Result result1 = new Result("Result 1", 1, 1, SimType.TRAINING);
        Result result2 = new Result("Result 2", 1, 2, SimType.TRAINING);
        List<Result> allResults = Arrays.asList(result1, result2);

        when(resultService.findByUser(1)).thenReturn(allResults);

        mockMvc.perform(get("/api/results/1")
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
        Result result = new Result("Test Result", 1, 123, SimType.TRAINING);
        result.setId("123");

        when(resultService.findByUserAndSession(1, 123)).thenReturn(result);

        mockMvc.perform(get("/api/results/1/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("Test Result")))
                .andExpect(jsonPath("$.user", is(1)))
                .andExpect(jsonPath("$.session", is(123)))
                .andExpect(jsonPath("$.simType", is("TRAINING")));
    }

    @Test
    void whenGetNonExistingResult_thenReturn404() throws Exception {
        when(resultService.findByUserAndSession(1, 999)).thenReturn(null);

        mockMvc.perform(get("/api/results/1/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void whenAddNewResult_thenReturnSuccess() throws Exception {
        Result result = new Result("New Result", 1, 1, SimType.TRAINING);

        mockMvc.perform(post("/api/results/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk())
                .andExpect(content().string("Result added"));
    }

    @Test
    void whenAddNewResultWithInvalidJson_thenReturn400() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/api/results/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetResultsForUserWithNoResults_thenReturnEmptyArray() throws Exception {
        when(resultService.findByUser(999)).thenReturn(List.of());

        mockMvc.perform(get("/api/results/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void whenAddResultWithNullFields_thenReturnSuccess() throws Exception {
        Result result = new Result(null, 1, 1, SimType.TRAINING);

        mockMvc.perform(post("/api/results/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk())
                .andExpect(content().string("Result added"));
    }
}
