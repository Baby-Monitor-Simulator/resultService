package com.babymonitor.resultService.service;
import com.babymonitor.resultService.model.Result;

import java.util.List;
import java.util.Optional;

public interface ResultService {
    List<Result> findByUser(String user);
    Result FindResult(String user, String id);
    void addResult(Result result);
}
