package com.example.deliveryservice.controller;

import com.example.deliveryservice.dto.DeliveryRequest;
import com.example.deliveryservice.dto.DeliveryResponse;
import com.example.deliveryservice.dto.UpdateDeliveryStatusRequest;
import com.example.deliveryservice.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/create")
    public ResponseEntity<DeliveryResponse> createDelivery(
            @RequestBody DeliveryRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String token = extractToken(authorizationHeader);
        DeliveryResponse response = deliveryService.createDelivery(request, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DeliveryResponse>> getPendingDeliveries(
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String token = extractToken(authorizationHeader);
        List<DeliveryResponse> responses = deliveryService.getPendingDeliveries(token);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/update-status")
    public ResponseEntity<DeliveryResponse> updateDeliveryStatus(
            @RequestBody UpdateDeliveryStatusRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String token = extractToken(authorizationHeader);
        DeliveryResponse response = deliveryService.updateDeliveryStatus(request, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-deliveries")
    public ResponseEntity<List<DeliveryResponse>> getMyDeliveries(
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String token = extractToken(authorizationHeader);
        List<DeliveryResponse> responses = deliveryService.getDeliveriesByUser(token);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/assign-driver/{deliveryId}")
    public ResponseEntity<DeliveryResponse> assignDriverToDelivery(
            @PathVariable Integer deliveryId,
            @RequestParam Integer driverId,
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String token = extractToken(authorizationHeader);
        DeliveryResponse response = deliveryService.assignDriverToDelivery(deliveryId, driverId, token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assign-driver/{deliveryId}")
    public ResponseEntity<DeliveryResponse> assignDriverToDeliveryPost(
            @PathVariable Integer deliveryId,
            @RequestParam Integer driverId,
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String token = extractToken(authorizationHeader);
        DeliveryResponse response = deliveryService.assignDriverToDelivery(deliveryId, driverId, token);
        return ResponseEntity.ok(response);
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new RuntimeException("Token inválido o ausente");
        }
    }
} 