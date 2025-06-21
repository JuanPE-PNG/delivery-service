package com.example.deliveryservice.repository;

import com.example.deliveryservice.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    
    List<Delivery> findByDeliveryStatus(String status);
    
    List<Delivery> findByUserId(Integer userId);
    
    List<Delivery> findByAssignedDriverId(Integer driverId);
} 