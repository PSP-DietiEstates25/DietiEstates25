package com.dietiestates25.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "saved_searches")
public class SavedSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Possiamo salvare i parametri di ricerca
     * come JSON (colonna text) o come stringa serializzata.
     * Qui lo memorizziamo in un’unica colonna “params” di tipo TEXT.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String params;

    // RELAZIONI:
    // Un SavedSearch appartiene a un User – ManyToOne
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public SavedSearch() { }

    public SavedSearch(String params, User user) {
        this.params = params;
        this.user = user;
    }

    // getter / setter …
}

/* spiegazione su “params”:

il campo params è di tipo TEXT (o VARCHAR) e conterrà (per esempio) un JSON stringificato dei filtri selezionati dall’utente ({"contract":"SALE","minPrice":50000,"city":"Napoli"}),
oppure una stringa separata da delimitatori, ad esempio "SALE;50000;200000;3;B;Milano;10".
in fase di ricerca successiva, il server può “deserializzare” quel JSON in oggetto SearchFilterDto
e comporre nuovamente la query dinamica */