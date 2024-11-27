package com.babymonitor.resultService.service;
import com.babymonitor.resultService.model.Result;

import java.util.List;
import java.util.Optional;

public interface ResultService {
    List<Result> findByUser(int user);
    Result findByUserAndSession(int user, int session);
    String addResult(Result result);
}
