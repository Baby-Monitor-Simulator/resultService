package com.babymonitor.resultService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class resultController
{
    @GetMapping("/")
    public String getResult()
    {
        return "Your baby is safe";
    }
}
