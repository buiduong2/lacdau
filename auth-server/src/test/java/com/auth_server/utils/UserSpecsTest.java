package com.auth_server.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import com.auth_server.entity.OAUthProvider;
import com.auth_server.entity.OAuthUser;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.User;
import com.auth_server.entity.User_;
import com.auth_server.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserSpecsTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private final UserOrderConverter converter = new UserOrderConverter();

    private final UserSpecs spec = new UserSpecs(converter);

    @Test
    void testIsContainUsernameIngoreCase() {
        String keyword = "duong";
        List<User> expected = repository.findAll().stream()
                .filter(u -> {
                    if (u instanceof SystemUser sysU) {
                        return containsIgnoreCase(sysU.getUsername(), keyword);
                    } else if (u instanceof OAuthUser oauthU) {
                        return containsIgnoreCase(oauthU.getUsername(), keyword);
                    }
                    return false;
                }).toList();

        entityManager.clear();

        List<User> actual = repository.findAll(spec.isContainUsernameIngoreCase(keyword));

        actual.forEach(a -> {
            if (a instanceof SystemUser sysU) {
                System.out.println(sysU.getUsername());
            }
            if (a instanceof OAuthUser oauthU) {
                System.out.println(oauthU.getUsername());
            }
        });
        assertThat(actual).hasSize(expected.size()).hasSizeGreaterThan(0);

        assertThat(actual).isEqualTo(expected);

    }

    private boolean containsIgnoreCase(String str, String keyword) {
        return str.toUpperCase().contains(keyword.toUpperCase());
    }

    @Test
    void testIsModerate() {
        boolean isModerate = true;
        Predicate<User> isMordatePredicate = u -> isModerate != u.getPermissions().isEmpty();
        List<User> expected = repository.findAll()
                .stream()
                .filter(isMordatePredicate)
                .toList();
        entityManager.clear();
        List<User> actual = repository.findAll(spec.isModerate(isModerate));

        assertThat(actual).hasSizeGreaterThan(0).hasSize(expected.size()).isEqualTo(expected);

    }

    @Test
    void testIsProvider_SYS() {
        List<User> expected = repository.findAll().stream().filter(u -> u instanceof SystemUser).toList();

        List<User> actual = repository.findAll(spec.isProvider("SYS"));
        assertThat(actual).hasSizeGreaterThan(0).hasSize(expected.size()).isEqualTo(expected);

    }

    @Test
    void testIsProvider_OAUTH_GOOGLE() {
        List<User> expected = repository.findAll().stream()
                .filter(u -> u instanceof OAuthUser oAuthUser && oAuthUser.getProvider() == OAUthProvider.GITHUB)
                .toList();

        List<User> actual = repository.findAll(spec.isProvider("OAUTH-GITHUB"));
        assertThat(actual)
                .hasSizeGreaterThan(0)
                .hasSize(expected.size())
                .isEqualTo(expected);

    }

    @Test
    void testBySort_sortBySimple() {
        List<Sort.Order> simpleSorts = List.of(Sort.Order.asc(User_.DISPLAY_NAME), Sort.Order.desc(User_.CREATED_AT));
        List<User> expected = repository.findAll(Sort.by(simpleSorts));

        List<User> actual = repository.findAll(spec.bySort(simpleSorts));

        assertThat(actual).hasSize(expected.size()).hasSizeGreaterThan(0)
                .isEqualTo(expected);
    }

    @Test
    void testBySort_sortByConverter() {
        List<Sort.Order> convertSort = List.of(
                Sort.Order.asc("username"),
                Sort.Order.asc("provider"),
                Sort.Order.asc("userType"),
                Sort.Order.desc("email")

        );
        long size = repository.count();
        List<User> actual = repository.findAll(spec.bySort(convertSort));

        assertThat(actual).hasSizeGreaterThan(0)
                .hasSize((int) size)
                .isSortedAccordingTo(usernameComparator
                        .thenComparing(providerComparator)
                        .thenComparing(userTypeComparator)
                        .thenComparing(emailComparator.reversed()));
    }

    @Test
    void testBySort_compose() {
        List<Sort.Order> convertSort = List.of(
                Sort.Order.asc(User_.DISPLAY_NAME),
                Sort.Order.asc("username"),
                Sort.Order.asc("provider")

        );
        long size = repository.count();
        List<User> actual = repository.findAll(spec.bySort(convertSort));

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) entityManager.getEntityManager().createNativeQuery("""
                SELECT u.id
                FROM users AS u
                LEFT JOIN system_users AS sys ON u.id = sys.id
                LEFT JOIN oauth_user AS oauth ON u.id = oauth.id
                ORDER BY
                    u.display_name,
                    COALESCE(sys.username, oauth.username),
                    (IF(u.user_type = 'SYS', 'SYS', oauth.provider)) DESC
                """, Long.class).getResultList();

        assertThat(actual).hasSizeGreaterThan(0)
                .hasSize((int) size);

        assertThat(actual.stream().map(User::getId).toList()).isEqualTo(ids);

    }

    // Rất khó vì Postgres so sánh kiểu ji khó hiểu
    private final Comparator<User> usernameComparator = Comparator.nullsLast(Comparator.comparing(u -> {
        if (u instanceof SystemUser sys) {
            return sys.getUsername().toUpperCase();
        } else if (u instanceof OAuthUser oauth) {
            return oauth.getUsername().toUpperCase();
        }
        return null;
    }));

    private final Comparator<User> emailComparator = Comparator.nullsLast(Comparator.comparing(u -> {
        if (u instanceof SystemUser sys) {
            return sys.getEmail().toUpperCase();
        } else if (u instanceof OAuthUser oauth) {
            return oauth.getEmail().toUpperCase();
        }
        return null;
    }));

    private final Comparator<User> providerComparator = Comparator.nullsLast(Comparator.comparing(u -> {
        if (u instanceof SystemUser) {
            return "SYS";
        } else if (u instanceof OAuthUser oauth) {
            return oauth.getProvider().toString();
        }
        return null;
    }));

    private final Comparator<User> userTypeComparator = Comparator.nullsLast(Comparator.comparing(u -> u.getPermissions().size()));

}
