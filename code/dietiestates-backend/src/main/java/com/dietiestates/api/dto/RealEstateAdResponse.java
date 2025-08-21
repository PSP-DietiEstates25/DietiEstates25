package com.dietiestates.api.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RealEstateAdResponse {
        private Long id;
        private String category;
        private String description;
        private BigDecimal price;
        private Float size;
        private String address;
        private Integer rooms;
        private Integer floor;
        private String energyClass;
        private Double latitude;
        private Double longitude;
        private String postedByEmail;
        private Long detailId;
}
