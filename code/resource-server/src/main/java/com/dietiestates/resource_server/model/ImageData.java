package com.dietiestates.resource_server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name="data", length = 1000)
    private byte[] imageData;
}
