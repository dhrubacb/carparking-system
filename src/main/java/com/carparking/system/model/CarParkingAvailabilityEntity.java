package com.carparking.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "car_parking_availability")
@NoArgsConstructor
@AllArgsConstructor
public class CarParkingAvailabilityEntity {
    @Id
    private String id;
    private String carParkId;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer totalLots;
    private Integer availableLots;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private ZonedDateTime createdAt;
}
