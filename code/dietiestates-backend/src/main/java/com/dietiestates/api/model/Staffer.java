package com.dietiestates.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "account_id",
			foreignKey = @ForeignKey(name = "STAFFER_ACCOCUNT_ID_FK"))
	private DefaultAccount securityAccountDecorator;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "admin_id",
			foreignKey = @ForeignKey(name = "STAFFER_ADMIN_ID_FK"))
	private Admin admin;

	public Staffer(
			DefaultAccount securityAccountDecorator,
			Admin admin
			) {
		this.securityAccountDecorator = securityAccountDecorator;
		admin.addStaffer(this);
	}
	
}
