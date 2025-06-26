package com.dietiestates25.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "notification_categories")
public class NotificationCategory {

    @Id
    @Enumerated(EnumType.STRING)
    private NotificationCategoryType name;

    @NotNull
    private boolean isActive;

    @OneToMany(mappedBy = "notificationCategory", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

}
