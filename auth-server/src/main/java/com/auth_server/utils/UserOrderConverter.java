package com.auth_server.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;

import com.auth_server.entity.OAuthUser;
import com.auth_server.entity.OAuthUser_;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.SystemUser_;
import com.auth_server.entity.User;
import com.auth_server.entity.User_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

@Component
public class UserOrderConverter {
    private final Map<String, OrderSupplier<User>> suppliers = this.getSuppliers();

    private Map<String, OrderSupplier<User>> getSuppliers() {
        return Map.of(
                "username", this::sortByUsername,
                "provider", this::sortByProvider,
                "userType", this::sortUserType,
                "email", this::sortByEmail);
    }

    public List<Order> convert(List<Sort.Order> orders, Root<User> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {
        Queue<Integer> insertConvertIndex = new LinkedList<>();
        List<Sort.Order> convertAble = new ArrayList<>();
        List<Sort.Order> needConvert = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Sort.Order order = orders.get(i);
            if (suppliers.containsKey(order.getProperty())) {
                needConvert.add(order);
                insertConvertIndex.add(i);
            } else {
                convertAble.add(order);
            }
        }
        List<Order> resConvertAble = QueryUtils.toOrders(Sort.by(convertAble), root, builder);
        List<Order> resNeedConvert = needConvertToOrders(needConvert, root, query, builder);

        if (resNeedConvert.isEmpty()) {
            return resConvertAble;
        }

        return this.joinOrder(resConvertAble, resNeedConvert, insertConvertIndex);
    }


    private List<Order> needConvertToOrders(List<Sort.Order> orders, Root<User> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {
        return orders.stream()
                .map(order -> suppliers
                        .get(order.getProperty())
                        .apply(order, root, query, builder))
                .toList();
    }

    private List<Order> joinOrder(List<Order> orders, List<Order> neededConverts,
            Queue<Integer> insertPoints) {
        int totalSize = orders.size() + neededConverts.size();
        List<Order> result = new ArrayList<>(totalSize);

        Integer insertIndex = insertPoints.poll();
        int i = 0;
        int neededIndex = 0;
        while (result.size() < totalSize) {
            if (result.size() == insertIndex) {
                result.add(neededConverts.get(neededIndex));
                neededIndex++;
                insertIndex = insertPoints.poll();
            } else {
                result.add(orders.get(i));
                i++;
            }
        }

        return result;
    }

    public Order sortByEmail(Sort.Order order, Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Root<SystemUser> sysRoot = builder.treat(root, SystemUser.class);
        Root<OAuthUser> oauthRoot = builder.treat(root, OAuthUser.class);
        Expression<String> email = builder.coalesce(sysRoot.get(SystemUser_.email),
                oauthRoot.get(OAuthUser_.email));
        return buildOrder(order, email, builder);
    }

    public Order sortByUsername(Sort.Order order, Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Root<SystemUser> sysRoot = builder.treat(root, SystemUser.class);
        Root<OAuthUser> oauthRoot = builder.treat(root, OAuthUser.class);
        Expression<String> username = builder.coalesce(sysRoot.get(SystemUser_.username),
                oauthRoot.get(OAuthUser_.username));

        return buildOrder(order, username, builder);
    }

    public Order sortByProvider(Sort.Order order, Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Root<OAuthUser> oauthRoot = builder.treat(root, OAuthUser.class);
        Expression<Object> provider = builder.selectCase(root.type())
                .when(SystemUser.class, "SYS")
                .when(OAuthUser.class, oauthRoot.get(OAuthUser_.provider))
                .otherwise("");

        return buildOrder(order, provider, builder);
    }

    public Order sortUserType(Sort.Order order, Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Expression<Integer> permissionCount = builder.size(root.get(User_.permissions));
        return buildOrder(order, permissionCount, builder);
    }

    private Order buildOrder(Sort.Order order, Expression<?> exp, CriteriaBuilder builder) {
        return order.getDirection() == Direction.ASC
                ? builder.asc(exp)
                : builder.desc(exp);
    }
}
