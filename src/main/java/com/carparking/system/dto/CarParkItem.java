package com.carparking.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
@Data
public class CarParkItem {
    public Date timestamp;
    @JsonProperty("carpark_data")
    public ArrayList<CarparkDatum> carparkData;
}
