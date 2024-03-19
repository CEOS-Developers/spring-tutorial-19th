package com.ceos19.springboot;

import com.ceos19.springboot.Test;
import com.ceos19.springboot.TestRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    /* Read All*/
    @Transactional(readOnly = true)
    public List<Test> findAllTests() {
        return testRepository.findAll();
    }
}