package com.ceos19.springboot.controller;

import com.ceos19.springboot.domain.Test;
import com.ceos19.springboot.service.TestService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {

    private final TestService testService;

    @GetMapping
    public List<Test> findAllTest() {
        return testService.findAllTest();
    }
}
