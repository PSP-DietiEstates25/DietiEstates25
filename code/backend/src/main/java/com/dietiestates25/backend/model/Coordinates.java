package com.dietiestates25.backend.model;

import jakarta.persistence.Embeddable;

@Embeddable
record Coordinates(String latitude, String longitude) {}
