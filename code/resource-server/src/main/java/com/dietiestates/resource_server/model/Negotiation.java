package com.dietiestates.resource_server.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Negotiation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id",
            foreignKey = @ForeignKey(name = "NEGOTIATION_USER_ID_FK"))
    private User user;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "estate_agent_id",
            foreignKey = @ForeignKey(name = "NEGOTIATION_ESTATE_AGENT_ID_FK"))
    private EstateAgent estateAgent;

    @ManyToOne
    @JoinColumn(
            name = "real_estate_id",
            foreignKey = @ForeignKey(name = "NEGOTIATION_REAL_ESTATE_ID_FK"))
    private RealEstate realEstate;

    @OneToMany(mappedBy = "negotiation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> proposals = new ArrayList<>();

    @Builder(builderMethodName = "builder")
    public Negotiation(
            User user,
            EstateAgent estateAgent,
            RealEstate realEstate
    ){
        user.addNegotiation(this);
        estateAgent.addNegotiation(this);
        realEstate.addNegotiation(this);
    }

    public void addProposal(Proposal proposal){
        proposals.add(proposal);
        proposal.setNegotiation(this);
    }

    public List<Offer> getOffers(){
        var offers = new ArrayList<Offer>();
        proposals.forEach(proposal -> {
            if(proposal instanceof Offer offer)
                offers.add(offer);
        });

        return offers;
    }

    public List<Visit> getVisits(){
        var visits = new ArrayList<Visit>();
        proposals.forEach(proposal -> {
            if(proposal instanceof Visit visit)
                visits.add(visit);
        });

        return visits;
    }
}
