package org.ticketdigi.model.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name="parking_details")
@Table(name = "parking_details")
public class ParkingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String slotName;
    @Column(nullable = false)
    private Boolean availability;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parkingDetails")
    @JsonIgnore
    private Booking booking;
}
