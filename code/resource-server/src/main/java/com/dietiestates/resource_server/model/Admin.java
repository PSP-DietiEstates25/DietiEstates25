package com.dietiestates.resource_server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Admin extends Staffer {
	
	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Staffer> staffers = new ArrayList<>();
	
	@Builder(builderMethodName = "builder")
	public Admin(String email, Admin admin) {
		super(email, admin);
	}
	
	public void addStaffer(Staffer staffer) {
		staffers.add(staffer);
		staffer.setAdmin(this);
	}

    public List<Admin> getCreatedAdmins(){
        var admins = new ArrayList<Admin>();
        staffers.forEach(staffer -> {
            if(staffer instanceof Admin admin)
                admins.add(admin);
        });

        return admins;
    }

    public List<EstateAgent> getCreatedEstateAgents(){
        var estateAgents = new ArrayList<EstateAgent>();
        staffers.forEach(staffer -> {
            if(staffer instanceof EstateAgent estateAgent)
                estateAgents.add(estateAgent);
        });

        return estateAgents;
    }
}
