package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "account_holder")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class AccountHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @OneToOne(mappedBy = "holder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Account account;

    // Getters & setters
    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
