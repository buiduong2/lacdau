package com.backend.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@NamedEntityGraph(name = "Province.DTO", attributeNodes = {
        @NamedAttributeNode(value = "districts"),
})

@Entity
@Getter
@Setter
public class Province {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    private List<District> districts;
}