package org.ticketdigi.parkingdetailsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication

@EntityScan(basePackages = {"org.ticketdigi.model.booking" })
@EnableEurekaClient
public class ParkingDetailsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParkingDetailsServiceApplication.class, args);
    }
}
