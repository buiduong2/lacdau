package com.auth_server.validation;

public interface IUniqueFields {
    default String getEmail() {
        return null;
    }

    default String getUsername() {
        return null;
    };
}
