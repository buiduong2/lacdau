package com.auth_server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.auth_server.entity.AuthToken;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.TokenType;
import com.auth_server.entity.UserTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SystemUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SystemUserRepository repository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    private SystemUser createUserWith2Token() {
        SystemUser u = UserTest.createUserWith2Token();
        repository.save(u);
        entityManager.flush();
        entityManager.clear();

        SystemUser savedUser = repository.findByUsername(u.getUsername()).get();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getTokens()).hasSize(2);
        return u;
    }

    private AuthToken getResetPasswordToken(SystemUser u) {

        AuthToken resetPasswordToken = u.getTokens().stream().filter(t -> t.getType() == TokenType.EMAIL_VERIFICATION)
                .findFirst().get();

        String tokenValue = resetPasswordToken.getTokenValue();
        TokenType type = resetPasswordToken.getType();

        assertThat(resetPasswordToken).isNotNull();
        assertThat(type).isNotNull();
        assertThat(tokenValue).isNotNull();

        entityManager.clear();

        return resetPasswordToken;
    }

    @Test
    void testFindUserByTokenValueAndTokenType_mustReturnAnUserWithOnlyOneTokenMatches() {
        SystemUser u = createUserWith2Token();
        AuthToken resetToken = getResetPasswordToken(u);
        String tokenValue = resetToken.getTokenValue();
        TokenType type = resetToken.getType();

        SystemUser userToResetPassword = repository
                .findUserByTokenValueAndTokenType(tokenValue, type)
                .get();

        AuthToken token = new AuthToken();
        token.setTokenValue(tokenValue);
        token.setType(type);

        assertThat(userToResetPassword).isNotNull();
        assertThat(userToResetPassword.getTokens()).hasSize(1);
        assertThat(userToResetPassword.getTokens().iterator().next()).isEqualTo(token);

    }


    @Test
    void testFindUserByTokenValueAndTokenType_whenRemoveToken_ShouldDelete() {
        SystemUser u = createUserWith2Token();
        AuthToken resetToken = getResetPasswordToken(u);
        String tokenValue = resetToken.getTokenValue();
        TokenType type = resetToken.getType();

        entityManager.clear();

        long id = resetToken.getId();
        AuthToken detactToken = new AuthToken();
        detactToken.setTokenValue(tokenValue);
        detactToken.setType(type);
        SystemUser userToResetPassword = repository
                .findUserByTokenValueAndTokenType(tokenValue, type)
                .get();

        userToResetPassword.removeToken(detactToken);
        repository.saveAndFlush(userToResetPassword);

        entityManager.flush();
        entityManager.clear();

        assertThat(authTokenRepository.findById(id)).isEmpty();
        assertThat(repository.findById(u.getId())).isNotEmpty();
        assertThat(repository.findById(u.getId()).get().getTokens()).hasSize(1);
    }

}
