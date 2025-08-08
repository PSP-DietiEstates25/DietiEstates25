package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class User {

	@Id
	private String email;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private String password;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications = new ArrayList<>();
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SearchUser> searches = new ArrayList<>();
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Proposal> proposals = new ArrayList<>();
	
	public void addNotification(Notification notification) {
		notifications.add(notification);
		notification.setUser(this);
	}
	
	public void addSearch(SearchUser search) {
		searches.add(search);
		search.setUser(this);
	}
	
	public void addProposal(Proposal proposal) {
		proposals.add(proposal);
		proposal.setUser(this);
	}
}
