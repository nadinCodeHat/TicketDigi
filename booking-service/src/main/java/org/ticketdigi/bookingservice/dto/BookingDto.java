package org.ticketdigi.bookingservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private String name;
    private String numberPlate;
    private Long contactNo;
    private String tokenNumber;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-M-yyyy hh:mm:ss")
    private Date checkin;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-M-yyyy hh:mm:ss")
    private Date checkout;
    private String slotName;
    private String paymentStatus;
    private BigDecimal amount;
}
