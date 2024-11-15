package com.babymonitor.resultService.controller;

import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.service.ResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
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
    public String getAllResults(@PathVariable int userid)
    {
        return resultServiceImpl.findByUser(String.valueOf(userid)).toString();
    }

    @GetMapping("/{userid}/{resultid}")
    public String getResult(@PathVariable int userid)
    {
        return resultServiceImpl.FindResult(String.valueOf(userid), String.valueOf(userid)).toString();
    }

    @PostMapping("/add")
    public String addResult(@RequestBody Result result)
    {
        resultServiceImpl.addResult(result);
        return "Result added";
    }
}
