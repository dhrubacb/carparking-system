package com.carparking.system.tasks;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class CarParkScheduler {

    @Value("${carpark.scheduler.initialDelay}")
    private Integer schedulerInitialDelay;

    @Value("${carpark.scheduler.period}")
    private Integer schedulerPeriod;

    private final CarParkInfoImportTask carParkInfoImportTask;
    private final ParkingAvailabilityTask parkingAvailabilityTask;


    public CarParkScheduler(ParkingAvailabilityTask parkingAvailabilityTask, CarParkInfoImportTask carParkInfoImportTask) {
        this.parkingAvailabilityTask = parkingAvailabilityTask;
        this.carParkInfoImportTask = carParkInfoImportTask;
    }


//    @Scheduled(initialDelay = 5, fixedDelay = 3600, timeUnit = TimeUnit.SECONDS)
    public void start() {
        try {
            carParkInfoImportTask.start();
            parkingAvailabilityTask.start();
        } catch (Exception exception) {
            log.error("Failed while scheduling the task", exception);
        }
        log.info("Carpark event scheduler has been started");
    }
}
