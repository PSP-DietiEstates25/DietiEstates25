package com.dietiestates.resource_server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Staffer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String email;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "admin_id",
			foreignKey = @ForeignKey(name = "STAFFER_ADMIN_ID_FK"))
	private Admin admin;

	public Staffer(String email, Admin admin) {
		this.email = email;
		admin.addStaffer(this);
	}
	
}
