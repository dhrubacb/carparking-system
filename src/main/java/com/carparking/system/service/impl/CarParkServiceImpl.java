package com.carparking.system.service.impl;

import com.carparking.system.dto.projection.CarParkProjection;
import com.carparking.system.model.CarPark;
import com.carparking.system.repository.CarParkRepository;
import com.carparking.system.service.CarParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CarParkServiceImpl implements CarParkService {
    private final CarParkRepository carParkRepository;

    @Autowired
    public CarParkServiceImpl(CarParkRepository carParkRepository) {
        this.carParkRepository = carParkRepository;
    }

    @Override
    public void deleteAll() {
        carParkRepository.deleteAllInBatch();
    }

    @Override
    public void saveAll(List<CarPark> carParks) {
        carParkRepository.saveAllAndFlush(carParks);
    }

    @Override
    public Set<CarParkProjection> findByParkingNumIn(Set<String> parkingNums) {
        return carParkRepository.findByCarParkNoIn(parkingNums);
    }

}
