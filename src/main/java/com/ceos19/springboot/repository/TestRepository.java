package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> { }