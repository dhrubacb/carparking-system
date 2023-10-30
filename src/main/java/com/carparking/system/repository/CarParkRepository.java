package com.carparking.system.repository;

import com.carparking.system.dto.projection.CarParkProjection;
import com.carparking.system.model.CarPark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CarParkRepository extends JpaRepository<CarPark, Long> {
    Set<CarParkProjection> findByCarParkNoIn(Set<String> parkingNums);
}
