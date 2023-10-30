package com.carparking.system.repository;

import com.carparking.system.model.CarParkingAvailabilityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingAvailRepository extends JpaRepository<CarParkingAvailabilityEntity, String> {

    @Query(value = "SELECT *, 6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:lng)) +" +
            " sin(radians(:lat)) * sin(radians(latitude))) AS distance FROM car_parking_availability ORDER BY distance",
            countQuery = "select count(id) FROM car_parking_availability",
            nativeQuery = true)
    Page<CarParkingAvailabilityEntity> findLocationsOrderByDistance(@Param("lat") Double latitude, @Param("lng") Double longitude,
                                                                    Pageable pageable);
}
