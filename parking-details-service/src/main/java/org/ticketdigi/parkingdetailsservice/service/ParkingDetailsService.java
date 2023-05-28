package org.ticketdigi.parkingdetailsservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ticketdigi.model.booking.ParkingDetails;
import org.ticketdigi.parkingdetailsservice.dto.ParkingDetailsDto;

import java.util.List;

@Service
public interface ParkingDetailsService {
    ResponseEntity<String> saveParking(ParkingDetailsDto parkingDetailsDto);

    ResponseEntity<ParkingDetails> getParking(String slotName);

    ResponseEntity<ParkingDetails> updateParking(int id, ParkingDetailsDto parkingDetailsDto);

    ResponseEntity<String> deleteParking(int id);

    ResponseEntity<List<ParkingDetails>> getAllParkings();
}
