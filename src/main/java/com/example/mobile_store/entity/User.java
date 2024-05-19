package com.example.mobile_store.entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        DISABLED,
        DELETED,
        PENDING_CONFIRMATION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "username", length = 64, unique = true)
    private String username;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "name", length = 128)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthDate")
    private Date birthDate;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "address", length = 256)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }
}
