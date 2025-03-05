package com.backend.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@NamedEntityGraph(name = "Order.orderDetail", attributeNodes = {
        @NamedAttributeNode(value = "payment"),
        @NamedAttributeNode(value = "shipment"),
        @NamedAttributeNode(value = "orderAddress"),
        @NamedAttributeNode(value = "orderItems", subgraph = "Order.OrderItems"),
}, subgraphs = {
        @NamedSubgraph(name = "Order.OrderItems", attributeNodes = @NamedAttributeNode("product")),
})

@NamedEntityGraph(name = "Order.AdminDTO", attributeNodes = {
        @NamedAttributeNode("payment"),
        @NamedAttributeNode("customer"),
        @NamedAttributeNode("shipment"),
        @NamedAttributeNode("orderAddress"),
        @NamedAttributeNode(value = "orderItems", subgraph = "orderItems.product"),
}, subgraphs = {
        @NamedSubgraph(name = "orderItems.product", attributeNodes = {
                @NamedAttributeNode(value = "product")
        })
})

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderStage stage;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Shipment shipment;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderAddress orderAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    public void setOrderItems(LinkedHashSet<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(this);
        }
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        if (this.orderItems == null) {
            this.orderItems = new LinkedHashSet<>();
        } else {
            this.orderItems.add(orderItem);
            orderItem.setOrder(this);
        }
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (this.orderItems == null) {
            return;
        }

        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
