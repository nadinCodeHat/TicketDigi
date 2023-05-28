package org.ticketdigi.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.ticketdigi.model.payment.Payment;

@Repository
@EnableJpaRepositories
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query(value = "select * from payment where slot_name =:slotName", nativeQuery = true)
    Payment findBySlotName(String slotName);
}
