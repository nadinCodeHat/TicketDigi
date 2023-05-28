package org.ticketdigi.parkingdetailsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticketdigi.model.booking.ParkingDetails;
import org.ticketdigi.parkingdetailsservice.dto.ParkingDetailsDto;
import org.ticketdigi.parkingdetailsservice.service.ParkingDetailsService;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ParkingDetailsController {

    private final ParkingDetailsService parkingDetailsService;

    @PostMapping("/parkings")
    public ResponseEntity<String> saveParking(@RequestBody ParkingDetailsDto parkingDetailsDto) {
        return parkingDetailsService.saveParking(parkingDetailsDto);
    }

    @GetMapping("/parkings/{slotName}")
    public ResponseEntity<ParkingDetails> getParking(@PathVariable String slotName){
        return parkingDetailsService.getParking(slotName);
    }

    @GetMapping("/parkings")
    public ResponseEntity<List<ParkingDetails>> getAllParkings(){
        return parkingDetailsService.getAllParkings();
    }

    @PatchMapping("/parkings/{id}")
    public ResponseEntity<ParkingDetails> updateParking(@PathVariable int id, @RequestBody ParkingDetailsDto parkingDetailsDto){
        return parkingDetailsService.updateParking(id, parkingDetailsDto);
    }

    @DeleteMapping("/parkings/{id}")
    public ResponseEntity<String> deleteParking(@PathVariable int id) {
        return parkingDetailsService.deleteParking(id);
    }

}
