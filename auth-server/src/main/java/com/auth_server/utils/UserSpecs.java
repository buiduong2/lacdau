package com.auth_server.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.auth_server.dto.req.UserFilter;
import com.auth_server.entity.OAuthUser;
import com.auth_server.entity.OAuthUser_;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.SystemUser_;
import com.auth_server.entity.User;
import com.auth_server.entity.UserPermission;
import com.auth_server.entity.UserPermission_;
import com.auth_server.entity.User_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSpecs {

    private final UserOrderConverter orderConverter;

    public Specification<User> byFilter(UserFilter filter, Pageable pageable) {
        List<Specification<User>> specs = new ArrayList<>();
        if (filter.getUsername() != null) {
            specs.add(isContainUsernameIngoreCase(filter.getUsername()));
        }

        if (filter.getDisplayName() != null) {
            specs.add(isContainDisplayNameIgnoreCase(filter.getDisplayName()));
        }

        if (filter.getMorderates() != null) {
            specs.add(byModerateIn(filter.getMorderates()));
        }

        if (filter.getProviders() != null) {
            specs.add(byProviderIn(filter.getProviders()));
        }

        if (!pageable.getSort().isEmpty()) {
            specs.add(bySort(pageable.getSort().get().toList()));
        }

        return Specification.allOf(specs);
    }

    protected Specification<User> byModerateIn(List<String> moderate) {
        List<Specification<User>> morderateSpecs = new ArrayList<>();
        for (String isModerate : moderate) {
            if (isModerate.equals("admin")) {
                morderateSpecs.add(isModerate(true));
            } else if (isModerate.equals("user")) {
                morderateSpecs.add(isModerate(false));
            }
        }

        return Specification.anyOf(morderateSpecs);
    }

    protected Specification<User> byProviderIn(List<String> providers) {
        List<Specification<User>> providerSpecs = new ArrayList<>();
        for (String provider : providers) {
            providerSpecs.add(isProvider(provider));
        }

        return Specification.anyOf(providerSpecs);
    }

    protected Specification<User> isContainUsernameIngoreCase(String username) {
        return (root, query, builder) -> {
            Root<SystemUser> sysRoot = builder.treat(root, SystemUser.class);
            Root<OAuthUser> oauthRoot = builder.treat(root, OAuthUser.class);

            String pattern = "%" + username.toUpperCase() + "%";
            return builder.or(
                    containIngoreCase(builder, sysRoot.get(SystemUser_.username), pattern),
                    containIngoreCase(builder, oauthRoot.get(OAuthUser_.username), pattern));
        };
    }

    private Predicate containIngoreCase(CriteriaBuilder builder, Expression<String> exp,
                                        String pattern) {
        return builder.like(builder.upper(exp), pattern);
    }

    protected Specification<User> isContainDisplayNameIgnoreCase(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toUpperCase() + "%";
            return containIngoreCase(builder, root.get(User_.displayName), pattern);
        };
    }
    @SuppressWarnings("null")
    protected Specification<User> isModerate(boolean isModerate) {
        return (root, query, builder) -> {
            Subquery<Long> subQ = query.subquery(Long.class);
            Root<UserPermission> usRoot = subQ.from(UserPermission.class);
            subQ.select(usRoot.get(UserPermission_.user).get(User_.id));
            subQ.distinct(true);
            if (isModerate) {
                return root.get(User_.id).in(subQ);
            } else {
                return builder.not(root.get(User_.id).in(subQ));
            }
        };
    }

    protected Specification<User> isProvider(String provider) {
        if (provider.equals("SYS")) {
            return (root, query, builder) -> builder.equal(root.type(), SystemUser.class);
        } else if (provider.startsWith("OAUTH-")) {
            return (root, query, builder) -> {
                Root<OAuthUser> oRoot = builder.treat(root, OAuthUser.class);
                return builder.equal(oRoot.get(OAuthUser_.provider), provider.substring("OAUTH-".length()));
            };
        }
        return null;
    }

    @SuppressWarnings("null")
    protected Specification<User> bySort(List<Sort.Order> pageOrders) {
        return (root, query, builder) -> {
            if (query.getResultType() != Long.class) {
                List<Order> orders = orderConverter.convert(pageOrders, root, query, builder);
                query.orderBy(orders);
            }
            return builder.conjunction();
        };
    }

}
