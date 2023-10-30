package com.carparking.system.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CarParkRoot {
    public ArrayList<CarParkItem> items;
}