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

    @ManyToMany
    @JoinTable(name = "notification_seen_by", joinColumns = @JoinColumn(name = "account_holder_id"), inverseJoinColumns = @JoinColumn(name = "notification_id"))
    private Set<Notification> seenNotifications = new HashSet<>();

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

    public Set<Notification> getSeenNotifications() {
        return seenNotifications;
    }

    public void setSeenNotifications(Set<Notification> seenNotifications) {
        this.seenNotifications = seenNotifications;
    }
}
