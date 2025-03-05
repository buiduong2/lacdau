package com.auth_server.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(indexes = @Index(columnList = "tokenValue"), uniqueConstraints = @UniqueConstraint(columnNames = { "tokenValue",
        "type" }))
@Entity
public class AuthToken {

    @Id
    @GeneratedValue
    private Long id;

    private String tokenValue;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private LocalDateTime expirationDate;

    @ManyToOne
    private User user;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tokenValue == null) ? 0 : tokenValue.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AuthToken other = (AuthToken) obj;
        if (tokenValue == null) {
            if (other.tokenValue != null)
                return false;
        } else if (!tokenValue.equals(other.tokenValue))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
