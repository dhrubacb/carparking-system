package com.carparking.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NearestParkingDto {
    private String address;
    private double latitude;
    private double longitude;
    @JsonProperty("total_lots")
    private Integer totalLots;
    @JsonProperty("available_lots")
    private Integer availableLots;

}
