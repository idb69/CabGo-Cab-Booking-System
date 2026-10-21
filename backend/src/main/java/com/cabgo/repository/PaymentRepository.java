package com.cabgo.repository;
import com.cabgo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByRiderEmailIgnoreCaseOrderByCreatedAtDesc(String email);
}
