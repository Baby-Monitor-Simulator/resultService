package com.babymonitor.resultService.controller;

import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.service.JwtAuthConverter;
import com.babymonitor.resultService.service.ResultServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/result")
@CrossOrigin
public class ResultController
{

    private ResultServiceImpl resultServiceImpl;

    @Autowired
    //@Qualifier("clientService")
    public void setClientService(ResultServiceImpl resultServiceImpl){
        this.resultServiceImpl = resultServiceImpl;
    }

    @GetMapping("/byUser")
    public List<Result> getAllResults(HttpServletRequest request)
    {
        return resultServiceImpl.findByUser(request);
    }

    @GetMapping("/{resultid}")
    public Result getResult(@PathVariable int resultid, HttpServletRequest request)
    {
        return resultServiceImpl.findByUserAndSession(resultid, request);
    }

    @PostMapping("/add")
    public String addResult(@RequestBody Result result)
    {
        return resultServiceImpl.addResult(result);
    }
}
