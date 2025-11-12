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
@ToString
@NoArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NotificationCategory> notificationCategories = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Search> searches = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Negotiation> negotiations = new ArrayList<>();
	
	@Builder(builderMethodName = "builder")
	public User(String email) {
		this.email = email;
	}
	
	public void addNotificationCategory(NotificationCategory notificationCategory) {
		notificationCategories.add(notificationCategory);
        notificationCategory.setUser(this);
	}
	
	public void addSearch(Search search) {
		searches.add(search);
		search.setUser(this);
	}
	
	public void addNegotiation(Negotiation negotiation) {
		negotiations.add(negotiation);
		negotiation.setUser(this);
	}	
}

