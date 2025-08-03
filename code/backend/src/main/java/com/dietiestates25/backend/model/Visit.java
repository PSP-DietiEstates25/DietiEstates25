package com.dietiestates25.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
// @Table(name = "visits")
public class Visit extends Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalDateTime dateTime;

    // Metodi:
    // Controlla se la visita è futura
    public boolean isUpcoming() {
        return this.dateTime != null && this.dateTime.isAfter(LocalDateTime.now());
    }

    // Controlla se la visita è avvenuta
    public boolean isCompleted() {
        return this.dateTime != null && this.dateTime.isBefore(LocalDateTime.now());
    }
}