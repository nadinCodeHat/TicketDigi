package org.ticketdigi.bookingservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.ticketdigi.bookingservice.dto.AmountDto;
import org.ticketdigi.bookingservice.dto.BookingDto;
import org.ticketdigi.bookingservice.dto.BookingResponse;
import org.ticketdigi.bookingservice.dto.ParkingDetailsDto;
import org.ticketdigi.bookingservice.event.BookingPlacedEvent;
import org.ticketdigi.bookingservice.repository.BookingRepository;
import org.ticketdigi.bookingservice.repository.ParkingDetailsRepository;
import org.ticketdigi.bookingservice.service.BookingService;
import org.ticketdigi.model.booking.Booking;
import org.ticketdigi.model.booking.ParkingDetails;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ParkingDetailsRepository parkingDetailsRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, BookingPlacedEvent> kafkaTemplate;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }
    @Override
    public ResponseEntity<String> saveBooking( BookingDto bookingDto) {

            // Call parking details service and check for slot availability
            ResponseEntity<ParkingDetailsDto> result = webClientBuilder.build().get()
                    .uri("http://parking-details-service/api/v1/parkings/"+bookingDto.getSlotName())
                    .retrieve()
                    .toEntity(ParkingDetailsDto.class)
                    .block();

            ParkingDetailsDto parkingDetailsDto = result.getBody();


            // if parking slot is available -> save booking
            if(parkingDetailsDto.getAvailability().equals(true)) {
                Booking booking = new Booking();
                booking.setName(bookingDto.getName());
                booking.setTokenNumber(generateToken());
                Date date = new Date();
                booking.setCheckin(date);
                booking.setCheckout(bookingDto.getCheckout());
                booking.setContactNo(bookingDto.getContactNo());
                booking.setNumberPlate(bookingDto.getNumberPlate());
                booking.setPaymentStatus("Not Paid");
                booking.setAmount(bookingDto.getAmount());

                //Model Mapper
                ParkingDetails parkingDetails = new ParkingDetails();
                parkingDetails.setId(parkingDetailsDto.getId());
                parkingDetails.setSlotName(parkingDetailsDto.getSlotName());
                parkingDetails.setAvailability(parkingDetailsDto.getAvailability());

                booking.setParkingDetails(parkingDetails);

                bookingRepository.save(booking);

                // Change Availability of parking slot
                ParkingDetails updateParkingDetails = parkingDetailsRepository.findById(parkingDetailsDto.getId()).get();

                if (updateParkingDetails.equals(null)) {
                    return new ResponseEntity<>("Invalid parking id", HttpStatus.NOT_FOUND);
                } else {
                    updateParkingDetails.setAvailability(false);
                    parkingDetailsRepository.save(updateParkingDetails);

                    // Kafka
                    kafkaTemplate.send("notificationTopic", new BookingPlacedEvent(parkingDetails.getId()));
                }

                return new ResponseEntity<>( "Parking slot Booked successfully", HttpStatus.CREATED);
            }else {
                return new ResponseEntity<>("Parking slot is not available, Please try again later", HttpStatus.OK);
            }
    }

    @Override
    public ResponseEntity<String> cancelBooking(String tokenNumber) {
        try {
            Booking booking = bookingRepository.findBookingByTokeNumber(tokenNumber);
            //System.out.println(booking.toString());
            if (booking.equals(null)) {
                return new ResponseEntity<>("No booking found under this token number", HttpStatus.NOT_FOUND);
            } else {
                bookingRepository.delete(booking);
             return new ResponseEntity<>("Booking canceled successfully", HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        try{
            List<BookingResponse> bookingResponses = new ArrayList<>();
            List<Booking> booking  = bookingRepository.findAll();

            BookingResponse bookingResponse = new BookingResponse();
            for(Booking booking1: booking){
                bookingResponse.setName(booking1.getName());
                bookingResponse.setNumberPlate(booking1.getNumberPlate());
                bookingResponse.setContactNo(booking1.getContactNo());
                bookingResponse.setTokenNumber(booking1.getTokenNumber());
                bookingResponse.setCheckin(booking1.getCheckin());
                bookingResponse.setCheckout(booking1.getCheckout());
                bookingResponse.setSlotName(booking1.getParkingDetails().getSlotName());
                bookingResponse.setPaymentStatus(booking1.getPaymentStatus());
                bookingResponse.setAmount(booking1.getAmount());

                bookingResponses.add(bookingResponse);
            }
            return new ResponseEntity<>(bookingResponses, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<BookingResponse> getBooking(int id) {
        try{
            Booking booking = bookingRepository.findById(id).get();

            if (booking.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                BookingResponse bookingResponse = new BookingResponse();
                bookingResponse.setName(booking.getName());
                bookingResponse.setNumberPlate(booking.getNumberPlate());
                bookingResponse.setContactNo(booking.getContactNo());
                bookingResponse.setTokenNumber(booking.getTokenNumber());
                bookingResponse.setCheckin(booking.getCheckin());
                bookingResponse.setCheckout(booking.getCheckout());
                bookingResponse.setSlotName(booking.getParkingDetails().getSlotName());
                bookingResponse.setPaymentStatus(booking.getPaymentStatus());
                bookingResponse.setAmount(booking.getAmount());
                return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Booking> updateBooking(int id, BookingDto bookingDto) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.equals(null)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            // if payment status is "Paid" and checkout time is updated
            // Call payment service and get the payment amount

            if(bookingDto.getPaymentStatus().equals("Paid")){
                ResponseEntity<AmountDto> result = webClientBuilder.build().get()
                        .uri("http://payment-service/api/v1/payments/"+bookingDto.getSlotName())
                        .retrieve()
                        .toEntity(AmountDto.class)
                        .block();

                AmountDto amountDto = result.getBody();

                Booking updateBooking = booking.get();
                updateBooking.setName(bookingDto.getName());
                updateBooking.setNumberPlate(bookingDto.getNumberPlate());
                updateBooking.setTokenNumber(bookingDto.getTokenNumber());
                updateBooking.setCheckin(bookingDto.getCheckin());
                Date date = new Date();
                updateBooking.setCheckout(date);
                updateBooking.setContactNo(bookingDto.getContactNo());
                updateBooking.setPaymentStatus(bookingDto.getPaymentStatus());
                updateBooking.setAmount(amountDto.getAmount());
                return new ResponseEntity<>(bookingRepository.save(updateBooking), HttpStatus.OK);
            }
            // if payment status is "Not Paid" then remove amount
            else {
                Booking updateBooking = booking.get();
                updateBooking.setName(bookingDto.getName());
                updateBooking.setNumberPlate(bookingDto.getNumberPlate());
                updateBooking.setTokenNumber(bookingDto.getTokenNumber());
                updateBooking.setCheckin(bookingDto.getCheckin());
                updateBooking.setCheckout(bookingDto.getCheckout());
                updateBooking.setContactNo(bookingDto.getContactNo());
                updateBooking.setPaymentStatus(bookingDto.getPaymentStatus());
                updateBooking.setAmount(null);
                return new ResponseEntity<>(bookingRepository.save(updateBooking), HttpStatus.OK);
            }
        }
    }
}
