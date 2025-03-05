package com.auth_server.service;

import org.apache.logging.log4j.util.Strings;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.auth_server.dto.res.BaseEmail;
import com.auth_server.entity.AuthToken;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.User;
import com.auth_server.mapper.EmailMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final EmailMapper mapper;

    public void sendWelcomeEmail(User user, AuthToken verificationToken) {
        if (Strings.isBlank(user.getEmail())) {
            return;
        }

        BaseEmail email = mapper.toVerificationEmail(user, verificationToken);
        sendEmail(email);
    }

    @Async
    public void sendResetPasswordEmail(SystemUser user, AuthToken resetPasswordToken) {
        if (Strings.isBlank(user.getEmail())) {
            return;
        }

        BaseEmail email = mapper.toResetPassword(user, resetPasswordToken);
        sendEmail(email);
    }

    private void sendEmail(BaseEmail email) {
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
