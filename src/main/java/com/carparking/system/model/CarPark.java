package com.carparking.system.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
public class CarPark {
    @Id
    private String id;
    private String carParkNo;
    private String address;
    private Double xCoord;
    private Double yCoord;
    private String carParkType;
    private String typeOfParkingSystem;
    private String shortTermParking;
    private String freeParking;
    private String nightParking;
    private Integer carParkDecks;
    private Double gantryHeight;
    private String carParkBasement;
}
