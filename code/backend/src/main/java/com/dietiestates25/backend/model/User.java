package com.dietiestates25.backend.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.DiscriminatorValue;
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
@DiscriminatorValue("USER")
//@Table(name = "users")
public class User extends BaseUser {

	@NotNull
	private BaseUserType baseUserType = BaseUserType.USER;

	@NotNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

	@NotNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();
	
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

	
	
	
	// Metodi:
	
	//id price state user ad estateagent
	public Offer makeOffer(Ad ad, EstateAgent ag) {
		//return new Offer(20L, new BigDecimal(40), OfferState.PENDING, this, ad, ag);
		return new Offer();
	}
	
	public Visit bookVisit(){ return new Visit(); }

	// Aggiunge una notifica all'utente
	public void addNotification(Notification notification) {
		if (notification != null) {
			notification.setUser(this);
			this.notifications.add(notification);
		}
	}

	/*
	// Ritorna il numero di notifiche non lette
	public long getUnreadNotificationsCount() {
    	return this.notifications.stream().filter(n -> !n.isRead()).count();
	}
	*/

	// Aggiunge una ricerca salvata all'utente
	public void addSavedSearch(SavedSearch savedSearch) {
		if (savedSearch != null) {
			savedSearch.getUsers().add(this);
			this.savedSearches.add(savedSearch);
		}
	}
}