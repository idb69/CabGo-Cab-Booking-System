package com.cabgo.repository;
import com.cabgo.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RideRepository extends JpaRepository<Ride,Long> {
    List<Ride> findByRiderEmailIgnoreCaseOrderByCreatedAtDesc(String email);
}
