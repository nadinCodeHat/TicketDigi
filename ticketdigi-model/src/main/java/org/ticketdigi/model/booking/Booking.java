package org.ticketdigi.model.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name="booking")
@Table(name="booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String numberPlate;
    @Column(nullable = false)
    private Long contactNo;
    @Column(nullable = false)
    private String tokenNumber;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-M-yyyy hh:mm:ss")
    @Column(nullable = false)
    private Date checkin;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-M-yyyy hh:mm:ss")
    private Date checkout;
    @Column(nullable = false)
    private String paymentStatus;
    private BigDecimal amount;
    //@OneToOne(cascade = CascadeType.PERSIST)
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name ="parkingid")
    @JsonIgnore
    private ParkingDetails parkingDetails;
}
