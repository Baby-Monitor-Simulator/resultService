package com.babymonitor.resultService.service;

import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultServiceImpl implements ResultService {
    @Autowired
    private ResultRepository resultRepo;

    public List<Result> findByUser(String user){
        return resultRepo.findByUser(user);
    };

    public Optional<Result> FindResult(String user, String id){
        return resultRepo.findById(id);
    };

    public void addResult(Result result){
        resultRepo.save(new Result(result.getResult(), result.getUser(), result.getSession()));
    };
}
