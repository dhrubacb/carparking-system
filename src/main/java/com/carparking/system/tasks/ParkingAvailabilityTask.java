package com.carparking.system.tasks;

import com.carparking.system.dto.CarParkItem;
import com.carparking.system.dto.CarParkRoot;
import com.carparking.system.dto.CarparkDatum;
import com.carparking.system.dto.CarparkInfo;
import com.carparking.system.dto.projection.CarParkProjection;
import com.carparking.system.model.CarParkingAvailabilityEntity;
import com.carparking.system.service.CarParkService;
import com.carparking.system.service.ParkingAvailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Log4j2
public class ParkingAvailabilityTask {
    @Value("${carpark.request.retry}")
    private Integer requestRetryCount;
//    @Value("${carpark.availability.url}")
    private String carparkAvailabilityUrl= "https://api.data.gov.sg/v1/transport/carpark-availability";
    private final CarParkService carParkService;
    private final ParkingAvailService parkingAvailService;

    public ParkingAvailabilityTask(CarParkService carParkService, ParkingAvailService parkingAvailService) {
        this.carParkService = carParkService;
        this.parkingAvailService = parkingAvailService;
    }

    public void start() {
        log.info("Started carpark event thread");
        boolean updated = false;
        List<CarParkingAvailabilityEntity> entities = parkingAvailService.findAll();
        try {
            updated = updateAvailability();
        } catch (Exception exception) {
            log.error("Error occurred during the task execution.", exception);
        }
        if (updated) {
            parkingAvailService.deleteAll(entities);
        }
        log.info("Finished carpark event thread");
    }

    @SneakyThrows
    private boolean updateAvailability() {
        CloseableHttpResponse response = invokeCarParkAvailUrl();

        if (Objects.isNull(response) || response.getCode() != 200) {
            log.error("Error while trying to retrieve Car Park Availability API");
            return false;
        }
        String resp = EntityUtils.toString(response.getEntity());
        CarParkRoot carParkRootObj = new ObjectMapper().readValue(resp, CarParkRoot.class);
        CarParkItem carParkItem = carParkRootObj.getItems().get(0);
        if (carParkItem == null) {
            return false;
        }
        Map<String, Pair<Integer, Integer>> accumResp = new HashMap<>();
        for (CarparkDatum carparkDatum : carParkItem.getCarparkData()) {
            int total = 0, avail = 0;
            for (CarparkInfo carparkInfo : carparkDatum.getCarparkInfo()) {
                total += carparkInfo.getTotalLots();
                avail += carparkInfo.getLotsAvailable();
            }
            Pair<Integer, Integer> pair = accumResp.getOrDefault(carparkDatum.getCarparkNumber(), Pair.of(0, 0));
            accumResp.put(carparkDatum.getCarparkNumber(), Pair.of(pair.getLeft() + total, pair.getRight() + avail));
        }
        log.info("{}", accumResp.keySet());

        Map<String, CarParkProjection> carParkProjections = carParkService.findByParkingNumIn(accumResp.keySet())
                .stream().collect(Collectors.toMap(CarParkProjection::getCarParkNo, Function.identity()));
        log.info("{}", carParkProjections.keySet());
        List<CarParkingAvailabilityEntity> entities = accumResp.entrySet().stream().filter(ent -> ent.getValue().getRight() > 0)
                .filter(ent -> carParkProjections.containsKey(ent.getKey()))
                .map(entry -> {
                    CarParkProjection parkingInfo = carParkProjections.get(entry.getKey());
                    Pair<Integer, Integer> value = entry.getValue();
                    return createParkingAvailEntity(parkingInfo, value);
                }).collect(Collectors.toList());
        parkingAvailService.saveAll(entities);
        return true;
}

    private CarParkingAvailabilityEntity createParkingAvailEntity(CarParkProjection parkingInfo, Pair<Integer, Integer> value) {
        CarParkingAvailabilityEntity carAvailability = new CarParkingAvailabilityEntity();
        carAvailability.setId(UUID.randomUUID().toString());
        carAvailability.setCarParkId(parkingInfo.getId());
        carAvailability.setAddress(parkingInfo.getAddress());
        carAvailability.setLatitude(Double.parseDouble(parkingInfo.getxCoord()));
        carAvailability.setLongitude(Double.parseDouble(parkingInfo.getyCoord()));
        carAvailability.setTotalLots(value.getLeft());
        carAvailability.setAvailableLots(value.getRight());
        return carAvailability;
    }


    @SneakyThrows
    private CloseableHttpResponse invokeCarParkAvailUrl() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(carparkAvailabilityUrl);

        while (requestRetryCount > 0) {
            try {
                return client.execute(httpGet);
            } catch (Exception exception) {
                log.error("Unable to retrieve car park availability data", exception);
                requestRetryCount--;
            }
        }
        log.error("Max requestRetryCount limit exceeded. Sensor not available.");

        client.close();
        return null;
    }
}
