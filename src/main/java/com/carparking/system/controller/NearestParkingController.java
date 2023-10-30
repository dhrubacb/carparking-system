package com.carparking.system.controller;

import com.carparking.system.constants.ApplicationConstants;
import com.carparking.system.constants.RequestURI;
import com.carparking.system.service.NearestParkingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.carparking.system.constants.ApplicationConstants.*;

@RestController
@Slf4j
@Validated
@RequestMapping(RequestURI.API + RequestURI.V1 + RequestURI.PARKING)
public class NearestParkingController {

    private final NearestParkingService nearestParkingService;

    public NearestParkingController(NearestParkingService nearestParkingService) {
        this.nearestParkingService = nearestParkingService;
    }

    @GetMapping(RequestURI.NEAREST)
    public ResponseEntity<?> findNearestParking(@RequestParam("latitude") String latitude,
                                                @RequestParam("longitude") String longitude,
                                                @RequestParam(value = "page", required = false,
                                                        defaultValue = "1") Integer page,
                                                @RequestParam(value = "per_page", required = false,
                                                        defaultValue = "3") Integer perPage
    ) {
        double lat, lon;
        try {
            lat = Double.parseDouble(latitude);
            lon = Double.parseDouble(longitude);
            if (lat < MIN_LAT || lat > MAX_LAT || lon < MIN_LON || lon > MAX_LON) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ApplicationConstants.INVALID_LAT_LONG);
            }
        } catch (Exception e) {
            log.error("Parsing exception! Requested lat: {}, long: {}", latitude, longitude);
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ApplicationConstants.INVALID_LAT_LONG);
        }
        return ResponseEntity.ok(nearestParkingService.findNearestParking(lat, lon, page, perPage));
    }

}
