package com.backend.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.backend.dto.req.FilterParam;
import com.backend.dto.req.FilterProductAdmin;
import com.backend.entities.AttributeValue;
import com.backend.entities.AttributeValue_;
import com.backend.entities.Brand_;
import com.backend.entities.Category_;
import com.backend.entities.Product;
import com.backend.entities.ProductStatus;
import com.backend.entities.Product_;
import com.backend.repository.CategoryRepository;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductSpecs {

    private final CategoryRepository categoryRepository;

    public Specification<Product> isKeyWordContainIngoreCase(String keyword) {
        return (root, query, builder) -> {
            String wildCard = "%" + keyword.toUpperCase() + "%";
            return builder.or(
                    builder.like(builder.upper(root.get(Product_.productCode)), wildCard),
                    builder.like(builder.upper(root.get(Product_.name)), wildCard));
        };
    }

    public Specification<Product> isBrandId(Long id) {
        return (root, query, builder) -> {

            return builder.equal(root.get(Product_.brand).get(Brand_.id), id);
        };
    }

    public Specification<Product> isBrandIdIn(List<Long> ids) {
        return (root, query, builder) -> {
            return root.get(Product_.brand).get(Brand_.id).in(ids);
        };
    }

    public Specification<Product> isPriceBetween(int min, int max) {
        return (root, query, builder) -> {
            return builder.between(
                    builder.function(
                            "COALESCE", Integer.class,
                            root.get(Product_.salePrice),
                            root.get(Product_.originalPrice)),
                    min, max);
        };
    }

    public Specification<Product> isPriceGreaterOrEqual(int min) {
        return (root, query, builder) -> {
            return builder.ge(
                    builder.function(
                            "COALESCE", Integer.class,
                            root.get(Product_.salePrice),
                            root.get(Product_.originalPrice)),
                    min);
        };
    }

    public Specification<Product> isPriceLessrOrEqual(int min) {
        return (root, query, builder) -> {
            return builder.le(
                    builder.function(
                            "COALESCE", Integer.class,
                            root.get(Product_.salePrice),
                            root.get(Product_.originalPrice)),
                    min);
        };
    }

    public Specification<Product> isAttributeValueContainAll(List<Long> ids) {
        return isAttributeValueIdBy(ids, true);
    }

    public Specification<Product> isAttributeValueIdBy(List<Long> ids, boolean containsAll) {
        return (root, query, builder) -> {
            if (query == null) {
                throw new RuntimeException("query IN Specification is NULL");
            }
            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<Product> subProductRoot = subQuery.from(Product.class);
            Join<Product, AttributeValue> attributeValueJoin = subProductRoot.join(Product_.attributeValues);

            subQuery.select(subProductRoot.get(Product_.id));
            subQuery.where(attributeValueJoin.get(AttributeValue_.id).in(ids));
            if (containsAll) {
                subQuery.groupBy(subProductRoot.get(Product_.id));
                subQuery.having(builder.equal(builder.count(subProductRoot.get(Product_.id)), ids.size()));
            }

            return root.get(Product_.id).in(subQuery);
        };
    }

    public Specification<Product> isAttributeValueContainAnyId(List<Long> ids) {
        return isAttributeValueIdBy(ids, false);
    }

    public Specification<Product> isCategoryId(Long parentId) {
        List<Long> ids = categoryRepository.findIdsWithIdbyParentId(parentId);
        return (root, query, builder) -> {
            return root.get(Product_.category).get(Category_.id).in(ids);
        };
    }

    public Specification<Product> isCategoryIn(List<Long> categoryIds) {
        return (root, query, builder) -> {
            return root.get(Product_.category).get(Category_.id).in(categoryIds);
        };
    }

    public Specification<Product> sortByPrice(Direction direction) {
        return (root, query, builder) -> {
            if (query == null)
                throw new RuntimeException("Query must no be null");
            if (query.getResultType() != Long.class) {
                Expression<Integer> price = builder.coalesce(root.get(Product_.salePrice),
                        root.get(Product_.originalPrice));

                Order order = direction.isAscending() ? builder.asc(price) : builder.desc(price);
                query.orderBy(order);
            }

            return builder.conjunction();
        };
    }

    public Specification<Product> bySort(List<Sort.Order> orders) {
        for (Sort.Order order : orders) {
            if (order.getProperty().equals("price")) {
                return sortByPrice(order.getDirection());
            }
        }

        return null;
    }

    public Specification<Product> isStatus(ProductStatus status) {
        return (root, query, builder) -> {
            return builder.equal(root.get(Product_.status), status);
        };
    }

    public Specification<Product> isStatusIn(List<ProductStatus> status) {
        return (root, query, builder) -> {
            return root.get(Product_.status).in(status);
        };
    }

    public Specification<Product> getSpecs(FilterParam params, List<Sort.Order> order) {
        List<Specification<Product>> specs = new ArrayList<>();

        Specification<Product> filterSpec = byFilter(params);
        specs.add(filterSpec);

        Specification<Product> sortSpec = bySort(order);
        if (sortSpec != null) {
            specs.add(sortSpec);
        }

        return Specification.allOf(filterSpec, sortSpec);
    }

    public Specification<Product> fetchRelation() {
        return (root, query, builder) -> {
            if (query != null && query.getResultType() != Long.class) {
                root.fetch(Product_.mainImage, JoinType.LEFT);
            }

            return builder.conjunction();
        };
    }

    public Specification<Product> byFilter(FilterProductAdmin params) {
        List<Specification<Product>> specs = new ArrayList<>();

        specs.add(fetchRelation());

        if (params.getStatus() != null) {
            specs.add(isStatusIn(params.getStatus()));
        }
        if (params.getAttributeValueIds() != null) {
            specs.add(isAttributeValueContainAnyId(params.getAttributeValueIds()));
        }

        if (params.getBrandIds() != null) {
            specs.add(isBrandIdIn(params.getBrandIds()));
        }

        if (params.getCategoryIds() != null) {
            specs.add(isCategoryIn(params.getCategoryIds()));
        }

        if (params.getMinMaxPrice() != null) {
            specs.add(isPriceBetween(params.getMinMaxPrice().get(0), params.getMinMaxPrice().get(1)));
        }

        if (params.getSearch() != null) {
            specs.add(isKeyWordContainIngoreCase(params.getSearch()));
        }
        return Specification.allOf(specs);
    }

    public Specification<Product> byFilter(FilterParam params) {

        List<Specification<Product>> specs = new ArrayList<>();

        specs.add(isStatus(ProductStatus.ACTIVE));
        specs.add(fetchRelation());

        if (params.getFilterIds() != null && !params.getFilterIds().isEmpty()) {
            specs.add(isAttributeValueContainAll(params.getFilterIds()));
        }
        if (params.getBrandId() != null) {
            specs.add(isBrandId(params.getBrandId()));
        }

        if (params.getMax() != null && params.getMin() != null) {
            specs.add(isPriceBetween(params.getMin(), params.getMax()));
        } else if (params.getMin() != null) {
            specs.add(isPriceLessrOrEqual(params.getMin()));
        } else if (params.getMax() != null) {
            specs.add(isPriceGreaterOrEqual(params.getMax()));
        }

        specs.add(isCategoryId(params.getCategoryId()));

        return Specification.allOf(specs);
    }
}
