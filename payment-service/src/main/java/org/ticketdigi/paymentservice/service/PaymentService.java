package org.ticketdigi.paymentservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ticketdigi.model.payment.Payment;
import org.ticketdigi.paymentservice.dto.PaymentDto;

import java.util.List;

@Service
public interface PaymentService {
    ResponseEntity<String> saveAmount(PaymentDto paymentDto);

    ResponseEntity<Payment> getAmount(String slotName);

    ResponseEntity<List<Payment>> getAllAmounts();

    ResponseEntity<Payment> updateParking(int id, PaymentDto paymentDto);

    ResponseEntity<String> deletePayment(int id);
}
