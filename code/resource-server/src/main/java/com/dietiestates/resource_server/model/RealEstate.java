package com.dietiestates.resource_server.model;

import com.dietiestates.resource_server.enums.RealEstateCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private RealEstateCategory category;

    @ElementCollection
    @CollectionTable(name = "real_estate_image", joinColumns = @JoinColumn(name = "real_estate_id", foreignKey = @ForeignKey(name = "RE_IMAGE_REALESTATE_ID_FK")))
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    @Lob
    private List<String> images = new ArrayList<>();

    @Column(nullable = false)
    private String description;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> proposals = new ArrayList<>();

    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SearchRealEstate> searchRealEstates = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false, name = "estate_agent_id", foreignKey = @ForeignKey(name = "REAL_ESTATE_ESTATE_AGENT_ID_FK"))
    private EstateAgent estateAgent;

    @OneToOne
    @JoinColumn(nullable = false, name = "cadastral_data_id", foreignKey = @ForeignKey(name = "REAL_ESTATE_CADASTRAL_DATA_ID_FK"))
    private CadastralData cadastralData;

    @OneToOne
    @JoinColumn(nullable = false, name = "detail_id", foreignKey = @ForeignKey(name = "REAL_ESTATE_DETAIL_ID_FK"))
    private Detail detail;

    @Builder(builderMethodName = "builder")
    public RealEstate(
            String category,
            String[] images,
            String description,
            EstateAgent estateAgent,
            CadastralData cadastralData,
            Detail detail) {
        this.category = RealEstateCategory.valueOf(category);
        this.images = images != null ? Arrays.asList(images) : new ArrayList<>();
        this.description = description;
        estateAgent.addRealEstate(this);
        setCadastralData(cadastralData);
        this.detail = detail;
    }

    public void addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setRealEstate(this);
    }

    public void addSearchRealEstate(SearchRealEstate searchRealEstate) {
        this.searchRealEstates.add(searchRealEstate);
        searchRealEstate.setRealEstate(this);
    }

    public void setCadastralData(CadastralData cadastralData) {
        this.cadastralData = cadastralData;
        cadastralData.setRealEstate(this);
    }

}
