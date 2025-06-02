package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Es. “portineria”, “climatizzazione”, “parcheggio”, ecc.
     */
    @Column(nullable = false, unique = true)
    private String tipo;

    // RELAZIONE INVERSA MANY-TO-MANY con RealEstate
    @ManyToMany(mappedBy = "services")
    private List<RealEstate> properties = new ArrayList<>();

    public Service() {
    }

    public Service(String tipo) {
        this.tipo = tipo;
    }

    // GETTER / SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<RealEstate> getProperties() {
        return properties;
    }

    public void setProperties(List<RealEstate> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Service))
            return false;
        Service service = (Service) o;
        return id != null && id.equals(service.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}