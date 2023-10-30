package com.carparking.system.service;

import com.carparking.system.model.CarParkingAvailabilityEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParkingAvailService {

    void saveAll(List<CarParkingAvailabilityEntity> models);

    List<CarParkingAvailabilityEntity> findLocationsOrderByDistance(Double latitude, Double longitude, Pageable pageable);

    List<CarParkingAvailabilityEntity> findAll();

    void deleteAll(List<CarParkingAvailabilityEntity> entities);
}
