package com.dietiestates25.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_holder_id", nullable = false)
    private AccountHolder holder;

}
