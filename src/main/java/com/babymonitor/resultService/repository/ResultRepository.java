package com.babymonitor.resultService.repository;

import com.babymonitor.resultService.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResultRepository extends MongoRepository<Result, String> {
    List<Result> findByUser(int user);
    Result findByUserAndSession(int user, int session);
}
