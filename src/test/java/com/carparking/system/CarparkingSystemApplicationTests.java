package com.carparking.system;

import com.carparking.system.constants.RequestURI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CarparkingSystemApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${local.server.port}")
    private int port;

    private String nearestParkingApiUrl;

    @BeforeEach
    public void setup() {
        restTemplate
                .getRestTemplate()
                .setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        nearestParkingApiUrl = "http://localhost:" + port + RequestURI.API + RequestURI.V1 + RequestURI.PARKING + RequestURI.NEAREST;
    }

    @Test
    public void getNearestParking() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        String badRes = nearestParkingApiUrl + "?longitude=33950.897&latitude=31314.37326" + "&per_page=3000" + "&page=0";
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, restTemplate.getForEntity(badRes, Object.class).getStatusCode());

        String firstReq = nearestParkingApiUrl + "?longitude=102.326897&latitude=1.312136" + "&per_page=3000" + "&page=0";
        ResponseEntity<Object> result = restTemplate.getForEntity(firstReq, Object.class);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        var firstResponse = modelMapper.map(result.getBody(), List.class);
        int total = firstResponse.size();

        String secRes = nearestParkingApiUrl + "?longitude=102.326897&latitude=1.312136" + "&per_page=" + total + "&page=0";
        ResponseEntity<Object> secResult = restTemplate.getForEntity(secRes, Object.class);
        Assertions.assertEquals(HttpStatus.OK, secResult.getStatusCode());
        var secResponse = modelMapper.map(secResult.getBody(), List.class);
        Assertions.assertEquals(secResponse.size(), total);
    }
}
