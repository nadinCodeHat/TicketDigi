package org.ticketdigi.parkingdetailsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.ticketdigi.model.booking.ParkingDetails;

@Repository
@EnableJpaRepositories
public interface ParkingDetailsRepository extends JpaRepository<ParkingDetails, Integer> {

  @Query(value = "select * from parking_details where slot_name =:slotName", nativeQuery = true)
  ParkingDetails findBySlotName(String slotName);
}
