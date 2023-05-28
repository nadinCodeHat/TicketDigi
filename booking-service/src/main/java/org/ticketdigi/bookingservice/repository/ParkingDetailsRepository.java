package org.ticketdigi.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.ticketdigi.model.booking.ParkingDetails;

@Repository
@EnableJpaRepositories
public interface ParkingDetailsRepository extends JpaRepository<ParkingDetails, Integer> {
}
