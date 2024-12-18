package com.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductCodeGenerator {

    @Id
    private String name;

    private long counter = 1L;
}
