package com.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class District {

    @Id
    private Long id;
    private String name;

    @ManyToOne
    private Province province;
}
