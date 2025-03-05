package com.auth_server.service;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_server.dto.req.PasswordConfirmable;
import com.auth_server.dto.req.RegisterReq;
import com.auth_server.dto.req.ResetPasswordReq;
import com.auth_server.entity.AuthToken;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.TokenType;
import com.auth_server.exception.ResourceNotFoundException;
import com.auth_server.mapper.SystemUserMapper;
import com.auth_server.repository.SystemUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemUserService {

    private final SystemUserRepository repository;

    private final SystemUserMapper mapper;

    private final PasswordEncoder encoder;

    private final ApplicationEventPublisher eventPublisher;

    public boolean checkExistsUsername(String username) {
        return repository.existsByUsername(username);
    }

    public boolean checkEmailExistsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Transactional
    public void register(RegisterReq req) {
        SystemUser user = mapper.toEntity(req);
        user.setPassword(encoder.encode(user.getPassword()));
        eventPublisher.publishEvent(user);
        repository.saveAndFlush(user);
    }

    @Transactional
    public void resetPassword(ResetPasswordReq req) {
        Optional<SystemUser> userOpt = repository.findUserByTokenValueAndTokenType(req.getToken(),
                TokenType.FORGOT_PASSWORD);

        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Token not found");
        }

        SystemUser u = userOpt.get();

        AuthToken resetPasswordToken = u.getTokens().iterator().next();

        u.removeToken(resetPasswordToken);

        u.setPassword(encoder.encode(req.getPassword()));
        repository.save(u);

    }

    @Transactional
    public void changePassword(PasswordConfirmable data, long userId) {
        SystemUser user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        user.setPassword(encoder.encode(data.getPassword()));
    }
}
