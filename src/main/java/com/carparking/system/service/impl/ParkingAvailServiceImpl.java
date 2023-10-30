package com.carparking.system.service.impl;

import com.carparking.system.model.CarParkingAvailabilityEntity;
import com.carparking.system.repository.ParkingAvailRepository;
import com.carparking.system.service.ParkingAvailService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingAvailServiceImpl implements ParkingAvailService {
    private final ParkingAvailRepository parkingAvailRepository;

    public ParkingAvailServiceImpl(ParkingAvailRepository parkingAvailRepository) {
        this.parkingAvailRepository = parkingAvailRepository;
    }

    @Override
    public void saveAll(List<CarParkingAvailabilityEntity> models) {
        parkingAvailRepository.saveAll(models);
    }

    @Override
    public List<CarParkingAvailabilityEntity> findLocationsOrderByDistance(Double latitude, Double longitude, Pageable pageable) {
        return parkingAvailRepository.findLocationsOrderByDistance(latitude, longitude, pageable).getContent();
    }

    @Override
    public List<CarParkingAvailabilityEntity> findAll() {
        return parkingAvailRepository.findAll();
    }

    @Override
    public void deleteAll(List<CarParkingAvailabilityEntity> entities) {
        parkingAvailRepository.deleteAll(entities);
    }
}
