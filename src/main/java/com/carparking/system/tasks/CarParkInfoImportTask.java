package com.carparking.system.tasks;

import com.carparking.system.model.CarPark;
import com.carparking.system.service.CarParkService;
import com.carparking.system.utils.latlong.converter.net.qxcg.svy21.LatLonCoordinate;
import com.carparking.system.utils.latlong.converter.net.qxcg.svy21.SVY21Coordinate;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class CarParkInfoImportTask {

    private final CarParkService carParkService;

    public CarParkInfoImportTask(CarParkService carParkService) {
        this.carParkService = carParkService;
    }

    public void start() {
        log.info("Started carpark event thread");
        try {
            List<CarPark> carParks = readFromCsvFile();
            carParkService.deleteAll();
            carParkService.saveAll(carParks);

        } catch (Exception exception) {
            log.error("Error occurred during the task execution.", exception);
        }
        log.info("Finished carpark event thread");
    }

    private List<CarPark> readFromCsvFile() throws IOException {
        List<CarPark> carParks = new ArrayList<>();
        try  {

            CSVReader reader = new CSVReader(new FileReader("src/main/resources/HDBCarparkInformation.csv"));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].trim().equals( "car_park_no")) { // For header
                    continue;
                }
                LatLonCoordinate latLonCoordinate = new SVY21Coordinate(Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3])).asLatLon();
                CarPark carPark = new CarPark();
                carPark.setId(UUID.randomUUID().toString());
                carPark.setCarParkNo(nextLine[0]);
                carPark.setAddress(nextLine[1]);
                carPark.setXCoord(latLonCoordinate.getLatitude());
                carPark.setYCoord(latLonCoordinate.getLongitude());
                carPark.setCarParkType(nextLine[4]);
                carPark.setTypeOfParkingSystem(nextLine[5]);
                carPark.setShortTermParking(nextLine[6]);
                carPark.setFreeParking(nextLine[7]);
                carPark.setNightParking(nextLine[8]);
                try {
                    carPark.setCarParkDecks(Integer.parseInt(nextLine[9]));
                } catch (Exception e){
                    log.error("err: "+nextLine[9]);
                }
                carPark.setGantryHeight(Double.parseDouble(nextLine[10]));
                carPark.setCarParkBasement(nextLine[11]);
                carParks.add(carPark);
            }
        } catch (Exception e) {
            log.error("Error while reading file");
            throw new RuntimeException(e);
        }
        return carParks;
    }
}
