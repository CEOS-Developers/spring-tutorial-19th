package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.Test;
import com.ceos19.springboot.repository.TestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    @Transactional(readOnly = true)
    public List<Test> findAllTests(){
        return testRepository.findAll();
    }
}
