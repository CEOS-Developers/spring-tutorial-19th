package com.ceos19.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.springboot.domain.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
}
