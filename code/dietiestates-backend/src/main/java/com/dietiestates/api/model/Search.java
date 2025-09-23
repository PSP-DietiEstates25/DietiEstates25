package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.AdCategory;

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
import jakarta.persistence.OneToOne;
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
@EntityListeners(AuditingEntityListener.class)
public class Search {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AdCategory category;
	
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
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "detail_id",
			foreignKey = @ForeignKey(name = "SEARCH_DETAIL_ID_FK"))
	private Detail detail;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_email",
			foreignKey = @ForeignKey(name = "SEARCH_USER_EMAIL_FK"))
	private User user;
	
	public void addDetails(Detail detail) {
		this.detail = detail;
		detail.setSearch(this);
	}
}
