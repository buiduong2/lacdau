package com.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import lombok.Getter;
@NamedEntityGraph(name = "Employee.AdminDTO", attributeNodes = {
    @NamedAttributeNode(value = "address"),
})
@Entity
@Getter
public class Employee extends Person {
    
}
