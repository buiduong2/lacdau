package com.backend.processor;

import static com.backend.entities.enums.OrderStageAction.UPDATE_ADDRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.backend.dto.req.OrderProcessAddressUpdateDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.enums.OrderStage;
import com.backend.exception.OrderActionNotAllowedInStageException;
import com.backend.processor.cancel.FailureOrderProcessor;
import com.backend.processor.progress.ConfirmOrderProcessor;
import com.backend.processor.progress.DeliveredOrderProcessor;
import com.backend.processor.progress.PackingOrderProcessor;
import com.backend.processor.progress.PaymentOrderProcessor;
import com.backend.processor.progress.PendingOrderProcessor;
import com.backend.processor.progress.ShippingOrderProcessor;
import com.backend.processor.progress.SuccessfulOrderProcessor;
import com.backend.repository.EmployeeRepository;
import com.backend.repository.OrderRepository;
import com.backend.service.OrderAdminService;
import com.backend.service.OrderProcessService;

@SpringJUnitConfig
@ContextConfiguration(classes = OrderProcessService.class)
@Import(TestConfig.class)
public class OrderProcessServiceTest {

    @Autowired
    OrderProcessService service;

    @MockBean
    private OrderAdminService orderAdminService;

    @MockBean
    private OrderRepository repository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @SpyBean
    private PendingOrderProcessor pending;
    @SpyBean
    private ConfirmOrderProcessor confirm;
    @SpyBean
    private PaymentOrderProcessor payment;
    @SpyBean
    private PackingOrderProcessor packing;
    @SpyBean
    private ShippingOrderProcessor shipping;
    @SpyBean
    private DeliveredOrderProcessor delivered;
    @SpyBean
    private SuccessfulOrderProcessor successful;

    @SpyBean
    private FailureOrderProcessor failure;

    @Test
    @DisplayName("Inject in to a list Amount and order should valid")
    @SuppressWarnings("unchecked")
    public void progressOrderProcessors_injectShouldWork() throws Exception {

        Field processField = OrderProcessService.class.getDeclaredField("progressOrderProcessors");
        processField.setAccessible(true);

        List<AbstractOrderProcessor> progressOrderProcessors = (List<AbstractOrderProcessor>) processField
                .get(service);

        assertThat(progressOrderProcessors)
                .isNotNull()
                .isNotEmpty()
                .hasSize(7);

        List<?> processorClasses = progressOrderProcessors.stream()
                .map(AbstractOrderProcessor::getClass).toList();

        assertThat(processorClasses).isEqualTo(
                List.of(
                        PendingOrderProcessor.class,
                        ConfirmOrderProcessor.class,
                        PaymentOrderProcessor.class,
                        PackingOrderProcessor.class,
                        ShippingOrderProcessor.class,
                        DeliveredOrderProcessor.class,
                        SuccessfulOrderProcessor.class));

    }

    @Test
    @DisplayName("Inject in to a list Amount and order should valid")
    @SuppressWarnings("unchecked")
    public void cancelOrderProcessors_injectShouldWork() throws Exception {
        Field cancelField = OrderProcessService.class.getDeclaredField("cancelOrderProcessors");
        cancelField.setAccessible(true);

        List<AbstractOrderProcessor> cancelOrderProcessors = (List<AbstractOrderProcessor>) cancelField
                .get(service);

        assertThat(cancelOrderProcessors)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        List<?> processorClasses = cancelOrderProcessors.stream()
                .map(AbstractOrderProcessor::getClass).toList();

        assertThat(processorClasses).isEqualTo(List.of(FailureOrderProcessor.class));

    }

    @Test
    @DisplayName("When OrderStage not valid should thrown: OrderActionNotAllowedInStageException")
    void testHandleAction_whenInValidShouldThrown() {
        OrderProcessAddressUpdateDTO dto = new OrderProcessAddressUpdateDTO();
        dto.setAction(UPDATE_ADDRESS);
        dto.setDescription("123");
        Order order = new Order();
        order.setStage(OrderStage.PENDING);
        Employee employee = new Employee();
        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(employeeRepository.getReferenceById(0L)).thenReturn(employee);

        assertThrows(OrderActionNotAllowedInStageException.class, () -> {
            service.handleAction(1L, dto, 0);
        });

        verify(pending).supports(order.getStage());
        verify(pending).handleAction(order, dto, employee);

    }

    @Test
    @DisplayName("When OrderStage VAlid should perform Action")
    void testHandleAction_whenValidShouldPerform() {
        OrderProcessAddressUpdateDTO dto = new OrderProcessAddressUpdateDTO();
        OrderProcessAddressUpdateDTO.OrderAddressUpdateDTO payload = new OrderProcessAddressUpdateDTO.OrderAddressUpdateDTO();
        dto.setAction(UPDATE_ADDRESS);
        dto.setDescription("123");

        dto.setPayload(payload);

        Order order = new Order();
        order.setStage(OrderStage.CONFIRMATION);
        Employee employee = new Employee();

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(employeeRepository.getReferenceById(0L)).thenReturn(employee);
        service.handleAction(1L, dto, 0L);

        verify(orderAdminService).updateOrerAddress(order, employee, payload);
    }

}
