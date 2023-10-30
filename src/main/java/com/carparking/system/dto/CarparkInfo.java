package com.carparking.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarparkInfo{
    @JsonProperty("total_lots")
    public int totalLots;
    @JsonProperty("lot_type")
    public String lotType;
    @JsonProperty("lots_available")
    public int lotsAvailable;
}