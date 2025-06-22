package com.dietiestates25.backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("STAFFER")
@Table(name = "staffer")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Staffer extends AccountHolder {
    // role determined by discriminator value
}
