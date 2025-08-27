package com.dietiestates.api.model;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(unique = true)
	private String username;
	
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Column(nullable = false)
	private boolean accountLocked;
	
	@Column(nullable = false)
	private boolean enabled;
	
	//trasformare il oneToMay, anche in role
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles;
	
	/*
	 * createdDate e lastModifiedDate è in collegamento con @EntityListener, se abbiamo qui o da qualche altra parte questa annotazione, dobbiamo
	 * andare nello springBootApplication e inserire @EnableJpaAuditing altrimenti il meccanismo di auditing non funziona
	 * 
	 * da google:
	 * Data auditing refers to the ability to record and track changes to data in a database, often including information about when a record was created, last modified, and by whom
	 */
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Notification> notifications = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<SearchUser> searches = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Proposal> proposals = new ArrayList<>();
	
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
		proposal.setEstateAgent(this);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return this.roles
					.stream()
					.map(r -> new SimpleGrantedAuthority(r.getName()))
					.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

	@Override
	public String getName() {
		return email;
	}	
}

