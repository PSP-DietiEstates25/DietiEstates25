package com.dietiestates.api.model;

import java.awt.image.BufferedImage;

import com.dietiestates.api.enums.AdCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
public class Ad {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private AdCategory category;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@Lob
	private byte[] photo;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private String description;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "real_estate_id",
			foreignKey = @ForeignKey(name = "REAL_ESTATE_ID_FK"))
	private RealEstate realEstate;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "estate_agent_email",
			foreignKey = @ForeignKey(name = "ESTATE_AGENT_EMAIL_FK"))
	private EstateAgent estateAgent;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "detail_id",
			foreignKey = @ForeignKey(name = "DETAIL_ID_FK"))
	private Detail detail;
}
