package org.ticketdigi.bookingservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ticketdigi.bookingservice.dto.BookingDto;
import org.ticketdigi.bookingservice.dto.BookingResponse;
import org.ticketdigi.model.booking.Booking;

import java.util.List;

@Service
public interface BookingService {
    ResponseEntity<String> saveBooking(BookingDto bookingDto);

    ResponseEntity<String> cancelBooking(String tokenNumber);

    ResponseEntity<List<BookingResponse>> getAllBookings();

    ResponseEntity<BookingResponse> getBooking(int id);

    ResponseEntity<Booking> updateBooking(int id, BookingDto bookingDto);
}
