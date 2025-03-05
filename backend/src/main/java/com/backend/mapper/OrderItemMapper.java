package com.backend.mapper;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.dto.req.OrderCreateDTO.OrderItemDTO;
import com.backend.entities.OrderItem;
import com.backend.entities.Product;
import com.backend.entities.enums.OrderItemStatus;
import com.backend.exception.ProductOutOfStockException;
import com.backend.exception.ResourceNotFoundException;
import com.backend.repository.ProductRepository;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class OrderItemMapper {

    @Autowired
    private ProductRepository productRepository;

    public LinkedHashSet<OrderItem> toEntities(LinkedHashSet<OrderItemDTO> dtos) {
        Map<String, Product> mapByProductCode = getMapByProductCode(dtos);

        LinkedHashSet<OrderItem> orderItems = new LinkedHashSet<>(dtos.size());
        for (OrderItemDTO dto : dtos) {
            Product product = mapByProductCode.get(dto.getProductId());
            OrderItem orderItem = this.toEntity(dto, product);
            this.setQuantityFlexibly(orderItem, dto);
            orderItems.add(orderItem);
        }
        boolean isAllOrderItemOutOfStock = orderItems.stream()
                .allMatch(o -> o.getStatus().equals(OrderItemStatus.NOT_SUPPLIED));
        if (isAllOrderItemOutOfStock) {
            throw new ProductOutOfStockException("orderItems", 0);
        }

        return orderItems;
    }

    private Map<String, Product> getMapByProductCode(LinkedHashSet<OrderItemDTO> dtos) {
        List<String> productCodes = dtos.stream().map(item -> item.getProductId()).toList();
        Map<String, Product> mapByProductCode = productRepository.findMapByProductCodeIn(productCodes);
        if (productCodes.size() != mapByProductCode.size()) {
            throw new ResourceNotFoundException("Some product not found");
        }
        return mapByProductCode;
    }

    public OrderItem toEntity(OrderItemDTO dto, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setUnitPrice(product.getOriginalPrice());
        orderItem.setSalePrice(product.getSalePrice());
        return orderItem;
    }

    public LinkedHashSet<OrderItem> updateEntities(List<OrderItem> oldItems, LinkedHashSet<OrderItemDTO> dtos) {
        LinkedHashSet<OrderItem> res = new LinkedHashSet<>();
        LinkedHashSet<OrderItemDTO> newRecordDTO = new LinkedHashSet<>();
        Map<String, OrderItem> productCodeToOrderItem = oldItems.stream()
                .collect(Collectors.toMap(i -> i.getProduct().getProductCode(), Function.identity()));

        for (OrderItemDTO dto : dtos) {
            if (productCodeToOrderItem.containsKey(dto.getProductId())) {
                String productCode = dto.getProductId();
                OrderItem entity = productCodeToOrderItem.get(productCode);
                this.updateQuantityStrict(entity, dto);
                res.add(entity);
                productCodeToOrderItem.remove(productCode);
            } else {
                newRecordDTO.add(dto);
            }
        }

        Map<String, Product> productCodeToProduct = getMapByProductCode(newRecordDTO);

        for (OrderItemDTO newDTO : newRecordDTO) {
            OrderItem orderItem = toEntity(newDTO, productCodeToProduct.get(newDTO.getProductId()));
            this.updateQuantityStrict(orderItem, newDTO);
            res.add(orderItem);
        }

        Collection<OrderItem> toRemoveEntity = productCodeToOrderItem.values();
        for (OrderItem orderItem : toRemoveEntity) {
            orderItem.remove();
            this.releaseProductQuantity(orderItem);
        }

        return res;
    }

    private void setQuantityFlexibly(OrderItem orderItem, OrderItemDTO dto) {
        Product product = orderItem.getProduct();
        orderItem.setRequestedQuantity(dto.getQuantity());
        orderItem.setSuppliedQuantity(Math.min(dto.getQuantity(), product.getQuantity()));
        if (orderItem.getRequestedQuantity() != orderItem.getSuppliedQuantity()) {
            if (orderItem.getSuppliedQuantity() == 0) {
                orderItem.setStatus(OrderItemStatus.NOT_SUPPLIED);
            } else {
                orderItem.setStatus(OrderItemStatus.PARTIALLY_SUPPLIED);
            }
        } else {
            orderItem.setStatus(OrderItemStatus.FULLY_SUPPLIED);
        }
        product.decreaseQuantity(orderItem.getSuppliedQuantity());
    }

    private void updateQuantityStrict(OrderItem entity, OrderItemDTO dto) {
        Product product = entity.getProduct();
        if (entity.getRequestedQuantity() == dto.getQuantity()) {
            return;
        }

        product.increaseQuantity(entity.getRequestedQuantity());
        if (dto.getQuantity() > product.getQuantity()) {
            throw new ProductOutOfStockException(product.getProductCode(), product.getQuantity());
        }
        product.decreaseQuantity(dto.getQuantity());

        entity.setRequestedQuantity(dto.getQuantity());
        entity.setSuppliedQuantity(dto.getQuantity());
        entity.setStatus(OrderItemStatus.FULLY_SUPPLIED);
    }

    public void releaseProductQuantity(OrderItem orderItem) {
        Product product = orderItem.getProduct();
        int quantity = orderItem.getSuppliedQuantity();
        product.increaseQuantity(quantity);
    }

    public void releaseProductQuantity(Collection<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            releaseProductQuantity(orderItem);
        }
    }

}