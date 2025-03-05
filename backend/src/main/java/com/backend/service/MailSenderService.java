package com.backend.service;

import org.apache.logging.log4j.util.Strings;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.backend.dto.res.BaseEmailContext;
import com.backend.entities.Order;
import com.backend.mapper.EmailMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final EmailMapper mapper;

    @Async
    public void sendOrderMailPlaced(Order order) {
        if (Strings.isBlank(order.getCustomer().getEmail())) {
            return;
        }

        BaseEmailContext context = mapper.toOrderPlacedContext(order);
        sendEmail(context);
    }

    @Async
    public void sendOrderMailThankYou(Order order) {
        if (Strings.isBlank(order.getCustomer().getEmail())) {
            return;
        }

        BaseEmailContext context = mapper.toOrderThankYouContext(order);
        sendEmail(context);
    }

    private void sendEmail(BaseEmailContext email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            context.setVariable("email", email);
            String html = templateEngine.process(email.getTemplate(), context);
            helper.setFrom(email.getFrom());
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
