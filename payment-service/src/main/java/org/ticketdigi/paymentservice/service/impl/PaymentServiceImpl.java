package org.ticketdigi.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ticketdigi.model.payment.Payment;
import org.ticketdigi.paymentservice.dto.PaymentDto;
import org.ticketdigi.paymentservice.repository.PaymentRepository;
import org.ticketdigi.paymentservice.service.PaymentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    @Override
    public ResponseEntity<String> saveAmount(PaymentDto paymentDto) {
        try {
            Payment payment = new Payment();
            payment.setSlotName(paymentDto.getSlotName());
            payment.setAmount(paymentDto.getAmount());
            paymentRepository.save(payment);
            return new ResponseEntity<>( "Parking amount saved successfully", HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Payment> getAmount(String slotName) {
        try {
            Payment payment = paymentRepository.findBySlotName(slotName);

            if (payment.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(payment, HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<Payment>> getAllAmounts() {
        try {
            List<Payment> paymentList = paymentRepository.findAll();

            if (paymentList.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(paymentList, HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Payment> updateParking(int id, PaymentDto paymentDto) {
        try {
            Optional<Payment> payment = paymentRepository.findById(id);
            if (payment.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Payment updatePayment = payment.get();
                updatePayment.setSlotName(paymentDto.getSlotName());
                updatePayment.setAmount(paymentDto.getAmount());

                return new ResponseEntity<>(paymentRepository.save(updatePayment), HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deletePayment(int id) {
        try {
            Payment payment = paymentRepository.findById(id).get();
            if (payment.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                paymentRepository.delete(payment);
                return new ResponseEntity<>("Parking amount deleted successfully", HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
