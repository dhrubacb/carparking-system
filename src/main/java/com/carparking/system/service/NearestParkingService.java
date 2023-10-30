package com.carparking.system.service;


import com.carparking.system.dto.NearestParkingDto;

import java.util.List;

public interface NearestParkingService {
    List<NearestParkingDto> findNearestParking(Double latitude, Double longitude, Integer page, Integer perPage);
}
