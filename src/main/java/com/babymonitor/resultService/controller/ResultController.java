package com.babymonitor.resultService.controller;

import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.service.ResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin
public class ResultController
{
    private ResultServiceImpl resultServiceImpl;

    @Autowired
    //@Qualifier("clientService")
    public void setClientService(ResultServiceImpl resultServiceImpl){
        this.resultServiceImpl = resultServiceImpl;
    }

    @GetMapping("/{userid}")
    public List<Result> getAllResults(@PathVariable int userid)
    {
        return resultServiceImpl.findByUser(userid);
    }

    @GetMapping("/{userid}/{resultid}")
    public Result getResult(@PathVariable int userid, @PathVariable int resultid)
    {
        return resultServiceImpl.findByUserAndSession(userid, resultid);
    }

    @PostMapping("/add")
    public String addResult(@RequestBody Result result)
    {
        resultServiceImpl.addResult(result);
        return "Result added";
    }
}
