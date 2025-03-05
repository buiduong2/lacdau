package com.auth_server.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.auth_server.repository.AuthTokenRepository;
import com.auth_server.repository.SystemUserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTest {

    @Autowired
    private SystemUserRepository repository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    public static SystemUser createUserWith2Token() {
        String username = "123";

        SystemUser u = new SystemUser();
        u.setUsername(username);
        u.setEmail(username);
        u.setPassword(username);
        AuthToken t1 = new AuthToken();
        t1.setTokenValue("123");
        t1.setType(TokenType.EMAIL_VERIFICATION);
        AuthToken t2 = new AuthToken();
        t2.setTokenValue("123");
        t2.setType(TokenType.FORGOT_PASSWORD);

        u.addToken(t2);
        u.addToken(t1);

        return u;
    }

    @Test
    void testRemoveToken_whenRemoveTokenDetach_shouldDelete() {
        SystemUser u = createUserWith2Token();

        repository.saveAndFlush(u);
        entityManager.clear();

        SystemUser u2 = repository.findById(u.getId()).get();

        assertThat(u2).isNotNull();
        assertThat(u2.getTokens()).hasSize(2);

        List<AuthToken> tokens = u2.getTokens().stream().toList();

        AuthToken t = new AuthToken();
        t.setTokenValue(tokens.get(0).getTokenValue());
        t.setType(tokens.get(0).getType());

        u2.removeToken(t);
        repository.saveAndFlush(u2);

        entityManager.flush();
        entityManager.clear();

        assertThat(authTokenRepository.findById(tokens.get(0).getId())).isEmpty();

    }
}
