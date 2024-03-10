package com.ceos19.springboot;

import com.ceos19.springboot.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {}