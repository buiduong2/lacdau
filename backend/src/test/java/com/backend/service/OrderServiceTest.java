package com.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.backend.dto.req.OrderCreateDTO;
import com.backend.dto.req.OrderCreateDTO.AddressDTO;
import com.backend.dto.req.OrderCreateDTO.OrderItemDTO;
import com.backend.dto.res.OrderDTO;
import com.backend.entities.Customer;
import com.backend.entities.Order;
import com.backend.entities.OrderItem;
import com.backend.entities.Payment;
import com.backend.entities.Product;
import com.backend.entities.enums.OrderItemStatus;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;
import com.backend.entities.enums.PaymentMethod;
import com.backend.entities.enums.PaymentStatus;
import com.backend.exception.ProductOutOfStockException;
import com.backend.repository.CustomerRepository;
import com.backend.repository.OrderRepository;
// import com.backend.repository.TestCustomerRepository;
import com.backend.repository.TestProductRepository;
import com.backend.utils.Payments;
import com.backend.utils.ServiceTest;

@ServiceTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TestProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private OrderCreateDTO prepareCreateDTO(List<Product> products, List<Integer> quanities) {
        OrderCreateDTO dto = new OrderCreateDTO();

        LinkedHashSet<OrderCreateDTO.OrderItemDTO> orderItems = new LinkedHashSet<>();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Integer quantity = quanities.get(i);

            OrderCreateDTO.OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setProductId(product.getProductCode());
            itemDto.setQuantity(quantity);
            orderItems.add(itemDto);
        }
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setFullName("setFullName");
        addressDTO.setEmail("setEmail");
        addressDTO.setPhone("setPhone");
        addressDTO.setDistrictId(1L);
        addressDTO.setDetail("setDetail");
        addressDTO.setMessage("setMessage");

        dto.setOrderItems(orderItems);
        dto.setPaymentMethod(PaymentMethod.CARD.toString());
        dto.setAddress(addressDTO);

        return dto;
    }

    private Customer prepareCustomer() {
        Customer customer = new Customer();
        customerRepository.saveAndFlush(customer);
        return customer;
    }

    @Test
    @DisplayName("When order must be created all Relate Table")
    void testCreate() {
        List<Product> products = productRepository.findFirst2ByQuantityGreaterThan(5);
        List<Integer> quanities = List.of(4, 5);
        Customer customer = prepareCustomer();
        assertThat(products).hasSize(2);
        assertThat(customer.getId()).isNotNull();

        entityManager.clear();

        OrderCreateDTO dto = prepareCreateDTO(products, quanities);

        OrderDTO orderDTO = orderService.create(dto, customer);

        entityManager.flush();
        entityManager.clear();

        assertThat(orderDTO.getId()).isNotNull();
        assertThat(orderRepository.existsById(orderDTO.getId()));

        Order order = orderRepository.findById(orderDTO.getId()).get();

        assertThat(order.getType()).isEqualTo(OrderType.ONLINE);
        assertThat(order.getStage()).isEqualTo(OrderStage.PENDING);
        assertThat(order.getCustomer()).isNotNull();

        Payment payment = order.getPayment();

        assertThat(payment).isNotNull();
        assertThat(payment.getMethod()).isEqualTo(PaymentMethod.CARD);
        assertThat(Payments.getPaymentStatus(payment)).isEqualTo(PaymentStatus.PROCESSING);
        assertThat(payment.getShippingPrice()).isNull();
        assertThat(payment.getSubTotalPrice()).isGreaterThan(0L);
        assertThat(payment.getTotalPrice()).isNull();

        assertThat(order.getShipment()).isNull();
        assertThat(order.getOrderAddress()).isNotNull();

        Set<OrderItem> orderItems = order.getOrderItems();
        assertThat(orderItems).hasSize(2)
                .allMatch(o -> o.getProduct() != null);

    }

    @Test
    @DisplayName("When OrderItem quantity Valid . Must have Quantity Fully")
    void testCreate_ValidQuantity() {
        List<Product> products = productRepository.findFirst2ByQuantityGreaterThan(5);
        List<Integer> quanities = List.of(4, 4);
        Customer customer = prepareCustomer();
        assertThat(products).hasSize(2);
        assertThat(customer.getId()).isNotNull();

        entityManager.clear();

        OrderCreateDTO dto = prepareCreateDTO(products, quanities);

        OrderDTO orderDTO = orderService.create(dto, customer);

        entityManager.flush();
        entityManager.clear();
        Order order = orderRepository.findById(orderDTO.getId()).get();

        Set<OrderItem> orderItems = order.getOrderItems();
        assertThat(orderItems).hasSize(2)
                .allMatch(o -> o.getSuppliedQuantity() == 4)
                .allMatch(o -> o.getRequestedQuantity() == 4)
                .allMatch(o -> o.getStatus().equals(OrderItemStatus.FULLY_SUPPLIED));
    }

    @Test
    @DisplayName("When OrderItem quantity invalid . Must have Partial Item")
    void testCreate_invalidQuantity() {
        List<Product> products = productRepository.findFirst2ByQuantityLessThanAndQuantityGreaterThan(5, 0);
        List<Integer> quanities = List.of(5, 5);
        Customer customer = prepareCustomer();
        assertThat(products).hasSize(2);
        assertThat(customer.getId()).isNotNull();

        entityManager.clear();

        OrderCreateDTO dto = prepareCreateDTO(products, quanities);

        OrderDTO orderDTO = orderService.create(dto, customer);

        entityManager.flush();
        entityManager.clear();
        Order order = orderRepository.findById(orderDTO.getId()).get();

        Set<OrderItem> orderItems = order.getOrderItems();
        assertThat(orderItems).hasSize(2)
                .allMatch(o -> o.getSuppliedQuantity() < 5)
                .allMatch(o -> o.getRequestedQuantity() == 5)
                .anyMatch(o -> o.getStatus().equals(OrderItemStatus.PARTIALLY_SUPPLIED));
    }

    @Test
    @DisplayName("When OrderItem quantity invalid . Must have NOT_SUPPLIED Item")
    void testCreate_FullyInvalidQuantity() {
        List<Product> products = productRepository.findFirst2ByQuantity(0);
        List<Product> products2 = productRepository.findFirst2ByQuantityGreaterThan(1);
        products.addAll(products2);
        List<Integer> quanities = List.of(5, 5, 5, 5);
        Customer customer = prepareCustomer();
        assertThat(products).hasSize(4);
        assertThat(customer.getId()).isNotNull();

        entityManager.clear();

        OrderCreateDTO dto = prepareCreateDTO(products, quanities);

        OrderDTO orderDTO = orderService.create(dto, customer);

        entityManager.flush();
        entityManager.clear();
        Order order = orderRepository.findById(orderDTO.getId()).get();

        Set<OrderItem> orderItems = order.getOrderItems();
        assertThat(orderItems).hasSize(4)
                .anyMatch(o -> o.getSuppliedQuantity() == 0)
                .allMatch(o -> o.getRequestedQuantity() == 5)
                .anyMatch(o -> o.getStatus().equals(OrderItemStatus.NOT_SUPPLIED))
                .anyMatch(o -> !o.getStatus().equals(OrderItemStatus.NOT_SUPPLIED));
    }

    @Test
    @DisplayName("When all order items is not supllied must be thrown")
    void testCreate_FullyInvalidQuantityAll() {
        List<Product> products = productRepository.findFirst2ByQuantity(0);
        List<Integer> quanities = List.of(5, 5);
        Customer customer = prepareCustomer();
        assertThat(products).hasSize(2);
        assertThat(customer.getId()).isNotNull();

        entityManager.clear();

        OrderCreateDTO dto = prepareCreateDTO(products, quanities);
        assertThrows(ProductOutOfStockException.class, () -> {
            orderService.create(dto, customer);
        });
    }

    @Test
    @DisplayName("When find ById Must be owner by customer And Eager LOAD")
    void testFindById() {
        // If not eager load will be not mapped
        List<Product> products = productRepository.findFirst2ByQuantityGreaterThan(0);
        List<Integer> quanities = List.of(5, 5);
        Customer customer = prepareCustomer();
        assertThat(products).hasSize(2);
        assertThat(customer.getId()).isNotNull();

        entityManager.clear();
        OrderCreateDTO dto = prepareCreateDTO(products, quanities);
        OrderDTO detailDTO = orderService.create(dto, customer);

        entityManager.clear();
        OrderDTO result = orderService.findById(detailDTO.getId(), customer);
        entityManager.clear();

        assertThat(result).isNotNull();

    }
}
