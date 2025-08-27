package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.AdCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EntityListeners(AuditingEntityListener.class)
public class Search {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AdCategory adCategory;
	
	@Column(nullable = false)
	private BigDecimal minimumPrice;
	
	@Column(nullable = false)
	private BigDecimal maximumPrice;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "detail_id",
			foreignKey = @ForeignKey(name = "DETAIL_ID_FK"))
	private Detail detail;
	

	@OneToMany(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SearchUser> users = new ArrayList<>();
	
	public void addUser(SearchUser user) {
		users.add(user);
		user.setSearch(this);
	}
}
