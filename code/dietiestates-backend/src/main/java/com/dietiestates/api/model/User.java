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
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "_user")
@DiscriminatorColumn(name="role", discriminatorType=DiscriminatorType.STRING)
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	protected Long id;
	
	@Column(unique = true)
	private String username;
	
	@Column(unique = true)
	protected String email;
	
	@Column(nullable = false, length = 255)
	protected String password;
	
	@Column(nullable = false)
	protected boolean accountLocked;
	
	@Column(nullable = false)
	protected boolean enabled;
	
	@ManyToMany
	protected List<Role> roles;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	protected LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	protected LocalDateTime lastModifiedDate;
	
	public User(
			Long id,
			String email,
			String password,
			boolean accountLocked,
			boolean enabled,
			List<Role> roles,
			LocalDateTime createdDate,
			LocalDateTime lastModifiedDate
			) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.accountLocked = accountLocked;
		this.enabled = enabled;
		this.roles = roles;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
	}
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Notification> notifications = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Search> searches = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Proposal> proposals = new ArrayList<>();
	
	public void addNotification(Notification notification) {
		notifications.add(notification);
		notification.setUser(this);
	}
	
	public void addSearch(Search search) {
		searches.add(search);
		search.setUser(this);
	}
	
	public void addProposal(Proposal proposal) {
		proposals.add(proposal);
		proposal.setUser(this);
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

