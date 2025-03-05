package com.backend.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@NamedEntityGraph(name = "Customer.AdminDTO", attributeNodes = {
        @NamedAttributeNode(value = "address"),
})


@Entity
@Getter
@Setter
public class Customer extends Person {

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
