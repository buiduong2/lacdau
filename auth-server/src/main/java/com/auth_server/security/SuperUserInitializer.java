package com.auth_server.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.auth_server.entity.Permission;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.User;
import com.auth_server.entity.UserPermission;
import com.auth_server.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuperUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EntityManager entityManager;

    private final String SUPER_USER_USERNAME = "ROOT_USER";

    private final String SUPER_USER_DISPLAY_NAME = "SUPER ADMIN";

    @Value("${custom.admin.email}")
    private String adminEmail;

    @Value("${custom.admin.password}")
    private String adminPassword;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        SystemUser admin = getOrCreateSuperUser();

        addPermission(admin);
        userRepository.save(admin);

        Query query = entityManager
                .createQuery("UPDATE SystemUser SET isSuperuser = TRUE WHERE  username = ?1")
                .setParameter(1, SUPER_USER_USERNAME);
        query.executeUpdate();
    }

    private SystemUser getOrCreateSuperUser() {
        Optional<User> userOpt = userRepository.findByIsSuperuserIsTrue();
        SystemUser admin;
        if (userOpt.isPresent() && userOpt.get() instanceof SystemUser) {
            admin = (SystemUser) userOpt.get();
        } else if (userOpt.isEmpty()) {
            admin = new SystemUser();
        } else {
            throw new RuntimeException("ERROR HACKED");
        }

        admin.setUsername(SUPER_USER_USERNAME);
        admin.setDisplayName(SUPER_USER_DISPLAY_NAME);
        admin.setEmail(adminEmail);
        admin.setEmailVerified(true);
        admin.setUsername(SUPER_USER_USERNAME);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        return admin;
    }

    public void addPermission(User admin) {
        Collection<UserPermission> permissions = getAllPermission();
        permissions.forEach(admin::addPermission);
        admin.getPermissions().forEach(p -> {
            p.setRevokedAt(null);
        });
    }

    private Collection<UserPermission> getAllPermission() {
        return Arrays.stream(Permission.values()).map(p -> {
            UserPermission userPermission = new UserPermission();
            userPermission.setPermission(p);
            return userPermission;
        }).toList();
    }

}
