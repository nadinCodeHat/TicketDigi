package org.ticketdigi.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticketdigi.model.payment.Payment;
import org.ticketdigi.paymentservice.dto.PaymentDto;
import org.ticketdigi.paymentservice.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<String> saveAmount(@RequestBody PaymentDto paymentDto) {
        return paymentService.saveAmount(paymentDto);
    }

    @GetMapping("/payments/{slotName}")
    public ResponseEntity<Payment> getAmount(@PathVariable String slotName){
        return paymentService.getAmount(slotName);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllAmounts(){
        return paymentService.getAllAmounts();
    }

    @PatchMapping("/payments/{id}")
    public ResponseEntity<Payment> updateAmount(@PathVariable int id, @RequestBody PaymentDto paymentDto){
        return paymentService.updateParking(id, paymentDto);
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable int id) {
        return paymentService.deletePayment(id);
    }
}



