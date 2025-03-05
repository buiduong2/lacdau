package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Value;

import com.backend.dto.res.EmailOrderPlacedContext;
import com.backend.dto.res.EmailOrderThankYouContext;
import com.backend.entities.Order;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EmailMapper {

    @Value("${custom.mail.client.domain}")
    private String CLIENT_DOMAIN;

    private static final String ORDER_THANK_YOUT_TEMPLATE = "email/order-thank-you";
    private static final String ORDER_PLACED_TEMPLATE = "email/order-placed";
    private static final String ORDER_THANK_YOU_SUBJECT = "Lắc đầu - Đơn hàng %s của bạn đã được giao thành công";
    private static final String ORDER_PLACED_SUBJECT = "Lắc đầu - Đơn hàng %s của bạn đang được chuẩn bị giao";

    private static final String FROM = "buiducduong";

    public EmailOrderPlacedContext toOrderPlacedContext(Order order) {
        EmailOrderPlacedContext context = new EmailOrderPlacedContext();
        context.setFrom(FROM);
        context.setTo(order.getCustomer().getEmail());
        context.setSubject(String.format(ORDER_PLACED_SUBJECT, " #" + order.getId()));
        context.setTemplate(ORDER_PLACED_TEMPLATE);

        context.setCustomerName(order.getCustomer().getLastName() + " " + order.getCustomer().getLastName());
        context.setOrderId(order.getId());
        context.setOrderDetailUrl(getOrderDetailUrl(order));
        context.setOrderCreatedAt(order.getCreatedAt());
        return context;
    }

    public EmailOrderThankYouContext toOrderThankYouContext(Order order) {
        EmailOrderThankYouContext context = new EmailOrderThankYouContext();
        context.setFrom(FROM);
        context.setTo(order.getCustomer().getEmail());
        context.setSubject(String.format(ORDER_THANK_YOU_SUBJECT, " #" + order.getId()));
        context.setTemplate(ORDER_THANK_YOUT_TEMPLATE);

        context.setClientHomePage(CLIENT_DOMAIN);
        context.setCustomerName(order.getCustomer().getLastName() + " " + order.getCustomer().getFirstName());
        context.setOrderId(order.getId());
        return context;
    }

    private String getOrderDetailUrl(Order order) {
        return CLIENT_DOMAIN + "/account/order/" + order.getId();
    }
}
