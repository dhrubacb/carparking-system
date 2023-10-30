package com.carparking.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class CarparkDatum{
    @JsonProperty("carpark_info")
    public ArrayList<CarparkInfo> carparkInfo;
    @JsonProperty("carpark_number")
    public String carparkNumber;
    @JsonProperty("update_datetime")
    public Date updateDatetime;
}





