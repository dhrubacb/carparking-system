package com.carparking.system.service;

import com.carparking.system.dto.projection.CarParkProjection;
import com.carparking.system.model.CarPark;

import java.util.List;
import java.util.Set;

public interface CarParkService {
    void deleteAll();

    void saveAll(List<CarPark> carParks);

    Set<CarParkProjection> findByParkingNumIn(Set<String> parkingNums);
}
