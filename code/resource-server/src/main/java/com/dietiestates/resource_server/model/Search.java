package com.dietiestates.resource_server.model;

import com.dietiestates.resource_server.enums.RealEstateCategory;
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
@EntityListeners(AuditingEntityListener.class)
public class Search {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RealEstateCategory category;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id",
			foreignKey = @ForeignKey(name = "SEARCH_USER_ID_FK"))
	private User user;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "detail_id",
			foreignKey = @ForeignKey(name = "SEARCH_DETAIL_ID_FK"))
	private Detail detail;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "cadastral_filter_id",
			foreignKey = @ForeignKey(name = "SEARCH_CADASTRAL_FILTER_ID_FK"))
	private CadastralFilter cadastralFilter;
	
	@Builder(builderMethodName = "builder")
	public Search(
			String category,
			User user,
			CadastralFilter cadastralFilter,
			Detail detail
			) {
		this.category = RealEstateCategory.valueOf(category);
		this.user = user;
        this.detail = detail;
        this.cadastralFilter = cadastralFilter;
		user.addSearch(this);
	}
}
