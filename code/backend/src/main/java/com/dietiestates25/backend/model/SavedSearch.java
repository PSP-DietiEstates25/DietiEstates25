package com.dietiestates25.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "saved_searches")
public class SavedSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToMany(mappedBy = "savedSearches")
    private List<User> users = new ArrayList<>();
    
    @NotNull
    @ManyToMany(mappedBy = "savedSearches")
    private List<Ad> ads = new ArrayList<>();
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
     * Possiamo salvare i parametri di ricerca
     * come JSON (colonna text) o come stringa serializzata.
     * Qui lo memorizziamo in un’unica colonna “params” di tipo TEXT.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String params;
}


/*
 * spiegazione su “params”:
 * 
 * il campo params è di tipo TEXT (o VARCHAR) e conterrà (per esempio) un JSON
 * stringificato dei filtri selezionati dall’utente
 * ({"contract":"SALE","minPrice":50000,"city":"Napoli"}),
 * oppure una stringa separata da delimitatori, ad esempio
 * "SALE;50000;200000;3;B;Milano;10".
 * in fase di ricerca successiva, il server può “deserializzare” quel JSON in
 * oggetto SearchFilterDto
 * e comporre nuovamente la query dinamica
 */