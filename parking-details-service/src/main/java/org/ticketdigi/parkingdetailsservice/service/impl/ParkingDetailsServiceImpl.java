package org.ticketdigi.parkingdetailsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ticketdigi.model.booking.ParkingDetails;
import org.ticketdigi.parkingdetailsservice.dto.ParkingDetailsDto;
import org.ticketdigi.parkingdetailsservice.repository.ParkingDetailsRepository;
import org.ticketdigi.parkingdetailsservice.service.ParkingDetailsService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingDetailsServiceImpl implements ParkingDetailsService {

    private final ParkingDetailsRepository parkingDetailsRepository;
    @Override
    public ResponseEntity<String> saveParking(ParkingDetailsDto parkingDetailsDto) {
        try {
            ParkingDetails parkingDetails = new ParkingDetails();
            parkingDetails.setSlotName(parkingDetailsDto.getSlotName());
            parkingDetails.setAvailability(true);
            parkingDetailsRepository.save(parkingDetails);
            return new ResponseEntity<>( "Parking details saved successfully", HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ParkingDetails> getParking(String slotName) {

        try {
            ParkingDetails parkingDetails = parkingDetailsRepository.findBySlotName(slotName);

            if (parkingDetails.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(parkingDetails, HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ParkingDetails> updateParking(int id, ParkingDetailsDto parkingDetailsDto) {
        try {
            Optional<ParkingDetails> parkingDetails = parkingDetailsRepository.findById(id);
            if (parkingDetails.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                ParkingDetails updateParkingDetails = parkingDetails.get();
                updateParkingDetails.setSlotName(parkingDetailsDto.getSlotName());

                return new ResponseEntity<>(parkingDetailsRepository.save(updateParkingDetails), HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteParking(int id) {
        try {
            ParkingDetails parkingDetails = parkingDetailsRepository.findById(id).get();

            if (parkingDetails.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                parkingDetailsRepository.delete(parkingDetails);
                return new ResponseEntity<>("Parking slot deleted successfully", HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<List<ParkingDetails>> getAllParkings() {
        try {
            List<ParkingDetails> parkingDetailsList = parkingDetailsRepository.findAll();

            if (parkingDetailsList.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(parkingDetailsList, HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
