package com.dietiestates25.backend.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
// @DiscriminatorValue("USER")
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Embedded
	private Account account;
	
	@NotNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> proposals = new ArrayList<>();
	
	@NotNull
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications = new ArrayList<>();
	
	//parte forte dell'associazione, un utente potrebbe potenzialmente eliminare e creare saved searchs
	@NotNull
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "users_savedSearches",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "savedSearch_id")
	)
	private List<SavedSearch> savedSearches = new ArrayList<>();

	//getter proxy
	public List<Proposal> getProposals(){
		return Collections.unmodifiableList(proposals);
	}
	
	public List<Notification> getNotifications(){
		return Collections.unmodifiableList(notifications);
	}
	
	public List<SavedSearch> getSavedSearches(){
		return Collections.unmodifiableList(savedSearches);
	}
	
	
}