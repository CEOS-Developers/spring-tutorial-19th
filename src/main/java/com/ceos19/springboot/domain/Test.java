package com.ceos19.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Test {

    @Id
    private Long id;
    private String name;
}
