package com.backend.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedEntityGraph(name = "Product.ProductInfo", attributeNodes = {
        @NamedAttributeNode(value = "detail", subgraph = "Product.detail"),
        @NamedAttributeNode(value = "mainImage")
}, subgraphs = {
        @NamedSubgraph(name = "Product.detail", attributeNodes = @NamedAttributeNode("thumbnails")),
})

@NamedEntityGraph(name = "Product.UpdateFull", attributeNodes = {
        @NamedAttributeNode(value = "detail", subgraph = "Product.detail"),
        @NamedAttributeNode(value = "relateInfo"),
        @NamedAttributeNode(value = "mainImage")
}, subgraphs = {
        @NamedSubgraph(name = "Product.detail", attributeNodes = @NamedAttributeNode("thumbnails")),
})
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "status"),
        @Index(columnList = "productCode")
})

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int originalPrice;

    private Integer salePrice;

    private int quantity;

    private int viewCount;

    @Column(length = 50, nullable = false, unique = true)
    private String productCode;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE','DRAFT','ARCHIVED')", nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Image mainImage;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ProductDetail detail;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private RelateInfo relateInfo;

    @ManyToMany
    @JoinTable(name = "product_attribute_values", //
            joinColumns = @JoinColumn(name = "products_id"), //
            inverseJoinColumns = @JoinColumn(name = "attribute_values_id"))
    private Set<AttributeValue> attributeValues;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Brand brand;

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", originalPrice=" + originalPrice + ", salePrice="
                + salePrice
                + ", quantity=" + quantity + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
                + "]";
    }

}
