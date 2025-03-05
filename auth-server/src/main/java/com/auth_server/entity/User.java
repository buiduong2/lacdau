package com.auth_server.entity;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@NamedEntityGraph(name = "auth", attributeNodes = {
        @NamedAttributeNode("permissions")
})
@Entity
@Getter
@Setter
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "USER_TYPE")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String displayName;

    private String avatarUrl;

    private boolean emailVerified;

    @Column(updatable = false, insertable = false)
    private Boolean isSuperuser;

    public String getEmail() {
        return null;
    }

    public void setEmail(String email) {

    }

    @OneToMany(mappedBy = "user", cascade = { PERSIST, MERGE })
    private Set<UserPermission> permissions;

    @OneToMany(mappedBy = "user", cascade = { PERSIST, REMOVE, MERGE }, orphanRemoval = true)
    private Set<AuthToken> tokens;

    public void addPermission(UserPermission userPermission) {
        if (this.permissions == null) {
            this.permissions = new HashSet<>();
        }
        this.permissions.add(userPermission);
        userPermission.setUser(this);
    }

    public void addToken(AuthToken token) {
        if (tokens == null) {
            tokens = new HashSet<>();
        }

        tokens.add(token);
        token.setUser(this);
    }

    public void removeToken(AuthToken token) {
        if (tokens == null) {
            return;
        }
        boolean success = tokens.remove(token);
        if (success) {
            token.setUser(null);
        }
    }

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        User entity = (User) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", displayName=" + displayName + ", avatarUrl=" + avatarUrl + ", emailVerified="
                + emailVerified + ", isSuperuser=" + isSuperuser + ", updatedAt=" + updatedAt + ", createdAt="
                + createdAt + "]";
    }

}
