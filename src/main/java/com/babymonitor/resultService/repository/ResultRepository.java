package com.babymonitor.resultService.repository;

import com.babymonitor.resultService.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ResultRepository extends MongoRepository<Result, String> {
    List<Result> findByUser(UUID user);
    Result findByUserAndSession(UUID user, int session);
}
