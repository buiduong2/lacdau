package com.auth_server.service;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth_server.mapper.UserPermissionMapper;
import com.auth_server.repository.UserPermissionRepository;
import com.auth_server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TestConfig {

    final UserPermissionRepository userPermissionRepository;

    final UserRepository userRepository;

    final UserPermissionMapper userPermissionMapper = Mappers.getMapper(UserPermissionMapper.class);

    @Bean
    UserPermissionService userPermissionService() {
        return new UserPermissionService(userPermissionRepository, userRepository, userPermissionMapper);
    }
}
