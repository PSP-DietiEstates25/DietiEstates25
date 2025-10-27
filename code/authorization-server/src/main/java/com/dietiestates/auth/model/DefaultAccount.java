package com.dietiestates.auth.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DefaultAccount implements Account, Serializable {

    private static final long serialVersionUID = 1086473393170997760L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "role_name",
            foreignKey = @ForeignKey(name = "DEFAULT_ACCOUNT_ROLE_FK")
    )
    private Role role;

    @Builder(builderMethodName = "builder")
    public DefaultAccount(
            String email,
            String password,
            Role role
    ) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    @JsonIgnore
    public Long getAccountId() {
        return id;
    }

    @Override
    @JsonIgnore
    public String getAccountPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getAccountEmail() {
        return email;
    }

    @Override
    @JsonIgnore
    public Role getAccountRole() {
        return role;
    }
}