package com.babymonitor.resultService.service;
import com.babymonitor.resultService.model.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface ResultService {
    List<Result> findByUser(HttpServletRequest request);
    Result findByUserAndSession(int session, HttpServletRequest request);
    String addResult(Result result);
}
