package com.dietiestates25.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
@Table(name = "base_users")
//@DiscriminatorColumn(name = "type")
public abstract class BaseUser {

	@Id
    private String email;

    @NotNull
    private String password;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private BaseUserType baseUserType;
}
