package org.ticketdigi.bookingservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticketdigi.bookingservice.dto.BookingDto;
import org.ticketdigi.bookingservice.dto.BookingResponse;
import org.ticketdigi.bookingservice.service.BookingService;
import org.ticketdigi.model.booking.Booking;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    @PostMapping("/bookings")
    @CircuitBreaker(name = "service-instance", fallbackMethod = "fallbackMethodSaveBooking")
    @TimeLimiter(name = "service-instance")
    @Retry(name = "service-instance")
    public CompletableFuture<ResponseEntity<String>> saveBooking(@RequestBody BookingDto bookingDto) {
        return CompletableFuture.supplyAsync(()-> bookingService.saveBooking(bookingDto));
    }

    public CompletableFuture<ResponseEntity<String>> fallbackMethodSaveBooking(BookingDto bookingDto, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(()-> new ResponseEntity<>( "Parking details service is unavailable, please try again later", HttpStatus.SERVICE_UNAVAILABLE));
    }

    @PatchMapping("/bookings/{id}")
    @CircuitBreaker(name = "service-instance", fallbackMethod = "fallbackMethodUpdateBooking")
    @TimeLimiter(name = "service-instance")
    @Retry(name = "service-instance")
    public CompletableFuture<ResponseEntity<Booking>> updateBooking(@PathVariable int id, @RequestBody BookingDto bookingDto) {
        return CompletableFuture.supplyAsync(()->bookingService.updateBooking(id, bookingDto));
    }

    public CompletableFuture<ResponseEntity<Booking>> fallbackMethodUpdateBooking(int id, BookingDto bookingDto, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(()-> new ResponseEntity<>( HttpStatus.SERVICE_UNAVAILABLE));
    }

    @DeleteMapping("/bookings/{tokenNumber}")
    public ResponseEntity<String> cancelBooking(@PathVariable String tokenNumber) {
        return bookingService.cancelBooking(tokenNumber);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable int id) {
        return bookingService.getBooking(id);
    }

}
