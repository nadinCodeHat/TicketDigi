package org.ticketdigi.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.ticketdigi.model.booking.Booking;

@Repository
@EnableJpaRepositories
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value = "select * from booking where token_number =:tokenNumber", nativeQuery = true)
    Booking findBookingByTokeNumber(String tokenNumber);
}
