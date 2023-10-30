package com.carparking.system.service.impl;

import com.carparking.system.dto.NearestParkingDto;
import com.carparking.system.model.CarParkingAvailabilityEntity;
import com.carparking.system.service.NearestParkingService;
import com.carparking.system.service.ParkingAvailService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NearestParkingServiceImpl implements NearestParkingService {
    private final ParkingAvailService parkingAvailService;
    private final ModelMapper modelMapper;

    public NearestParkingServiceImpl(ParkingAvailService parkingAvailService, ModelMapper modelMapper) {
        this.parkingAvailService = parkingAvailService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<NearestParkingDto> findNearestParking(Double latitude, Double longitude, Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<CarParkingAvailabilityEntity> availabilityEntities = parkingAvailService.findLocationsOrderByDistance(latitude,
                longitude, pageable);
        return availabilityEntities.stream().map(entity -> modelMapper.map(entity, NearestParkingDto.class))
                .collect(Collectors.toList());
    }
}
