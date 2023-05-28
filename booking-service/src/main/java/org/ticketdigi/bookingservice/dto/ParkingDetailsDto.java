package org.ticketdigi.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDetailsDto {
    private int id;
    private String slotName;
    private Boolean availability;
}
