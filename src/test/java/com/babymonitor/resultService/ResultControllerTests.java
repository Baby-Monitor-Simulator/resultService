package com.babymonitor.resultService;

import com.babymonitor.resultService.controller.ResultController;
import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.model.SimType;
import com.babymonitor.resultService.service.ResultServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResultController.class)
@WithMockUser(username = "testuser")
class ResultControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultServiceImpl resultService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID testUserId;

    @BeforeEach
    void setUp() {
        // Create a consistent test user ID
        testUserId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    @Test
    void whenGetAllResults_thenReturnJsonArray() throws Exception {
        Result result1 = new Result("Result 1", testUserId, 1, SimType.TRAINING);
        Result result2 = new Result("Result 2", testUserId, 2, SimType.TRAINING);
        List<Result> allResults = Arrays.asList(result1, result2);

        // Mock the service method to return results
        when(resultService.findByUser(any(HttpServletRequest.class))).thenReturn(allResults);

        mockMvc.perform(get("/result/byUser"))
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
        Result result = new Result("Test Result", testUserId, 123, SimType.TRAINING);

        // Mock the service method to return a result
        when(resultService.findByUserAndSession(eq(123), any(HttpServletRequest.class))).thenReturn(result);

        mockMvc.perform(get("/result/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("Test Result")))
                .andExpect(jsonPath("$.session", is(123)))
                .andExpect(jsonPath("$.simType", is("TRAINING")));
    }

    @Test
    void whenAddNewResult_thenReturnGeneratedId() throws Exception {
        Result result = new Result("New Result", testUserId, 1, SimType.TRAINING);

        // Mock the service method to return a generated ID
        when(resultService.addResult(any(Result.class), any(HttpServletRequest.class)))
                .thenReturn("generatedId");

        mockMvc.perform(post("/result/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk())
                .andExpect(content().string("generatedId"));
    }

    @Test
    void whenAddResultWithInvalidJson_thenReturn400() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/result/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}