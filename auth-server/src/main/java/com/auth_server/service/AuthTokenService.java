package com.auth_server.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.auth_server.entity.AuthToken;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.TokenType;
import com.auth_server.entity.User;
import com.auth_server.exception.ResourceNotFoundException;
import com.auth_server.repository.AuthTokenRepository;
import com.auth_server.repository.SystemUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthTokenService {

    private final AuthTokenRepository repository;
    private final SystemUserRepository userRepository;

    private final MailSenderService mailSenderService;

    public static final long VERIFICATION_TOKEN_EXPIRATION_MINUTES = 24 * 60;
    public static final long FORGOT_PASSWORD_TOKEN_EXPIRATION_MINUTES = 5;
    public static final String VERIFY_EMAIL_EXPIRATE = "1440L";
    public static final String RESET_PASSWORD_EMAIL_EXPIRATE = "5L";

    @Async
    @TransactionalEventListener
    @Transactional
    public void generateVeficationTokenWhenNewRegister(User user) {
        if (user.getEmail() == null) {
            return;
        }
        AuthToken verificationToken = this.generateToken(TokenType.EMAIL_VERIFICATION);
        verificationToken.setUser(user);
        repository.saveAndFlush(verificationToken);
        mailSenderService.sendWelcomeEmail(user, verificationToken);
    }

    @Transactional
    public void generateForgotPasswordToken(String email) {
        AuthToken forgotPasswordToken = this.generateToken(TokenType.FORGOT_PASSWORD);
        Optional<SystemUser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            forgotPasswordToken.setUser(userOpt.get());
            repository.saveAndFlush(forgotPasswordToken);
            mailSenderService.sendResetPasswordEmail(userOpt.get(), forgotPasswordToken);
        } else {
            throw new ResourceNotFoundException("username by email = " + email + " is not found");
        }
    }

    @Transactional
    public User verifyEmail(String token) {
        AuthToken authToken = repository.findByTokenValueAndType(token, TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new ResourceNotFoundException("Token Not Found"));

        User user = authToken.getUser();
        user.setEmailVerified(true);
        repository.delete(authToken);
        return user;

    }

    private AuthToken generateToken(TokenType type) {
        AuthToken token = new AuthToken();
        token.setType(type);
        token.setTokenValue(generateTokenValue());
        LocalDateTime now = LocalDateTime.now();
        if (type.equals(TokenType.EMAIL_VERIFICATION)) {
            token.setExpirationDate(now.plusMinutes(VERIFICATION_TOKEN_EXPIRATION_MINUTES));
        } else if (type.equals(TokenType.FORGOT_PASSWORD)) {
            token.setExpirationDate(now.plusMinutes(FORGOT_PASSWORD_TOKEN_EXPIRATION_MINUTES));
        }

        return token;
    }

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }
}
