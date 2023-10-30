package com.carparking.system;

import com.carparking.system.tasks.CarParkScheduler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarparkingSystemApplication {


    public static void main(String[] args) {
        SpringApplication.run(CarparkingSystemApplication.class, args);
    }

}
