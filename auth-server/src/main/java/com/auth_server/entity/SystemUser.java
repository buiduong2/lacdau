package com.auth_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("SYS")
@Table(name = "system_users")
public class SystemUser extends User {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Override
    public String toString() {
        return super.toString() + "SystemUser [username=" + username + ", password=" + password + ", email=" + email + "]";
    }

}
