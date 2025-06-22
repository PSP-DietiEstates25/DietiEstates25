package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admin")
public class Admin /*extends Staffer*/ {
    @OneToMany(mappedBy = "nominatedBy", cascade = CascadeType.ALL)
    private Set<EstateAgent> nominatedAgents = new HashSet<>();

    public Set<EstateAgent> getNominatedAgents() {
        return nominatedAgents;
    }

    public void setNominatedAgents(Set<EstateAgent> nominatedAgents) {
        this.nominatedAgents = nominatedAgents;
    }
}
