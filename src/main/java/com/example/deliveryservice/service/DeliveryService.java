package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.DeliveryRequest;
import com.example.deliveryservice.dto.DeliveryResponse;
import com.example.deliveryservice.dto.UpdateDeliveryStatusRequest;
import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.repository.DeliveryRepository;
import com.example.deliveryservice.utils.JwtUtil;
import com.example.deliveryservice.client.NotificationServiceClient;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    @Value("${microservice.auth-service.endpoints.endpoint.uri}")
    private String validateTokenUri;

    public DeliveryResponse createDelivery(DeliveryRequest request, String token) {
        Claims claims = JwtUtil.extractClaimsWithoutValidation(token);
        Integer userId = claims.get("id", Integer.class);

        Delivery delivery = new Delivery();
        delivery.setPaymentId(request.getPaymentId());
        delivery.setUserId(userId);
        delivery.setCustomerAddress(request.getCustomerAddress());
        delivery.setCustomerCity(request.getCustomerCity());
        delivery.setDeliveryStatus("PENDIENTE");

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return convertToResponse(savedDelivery, "Entrega creada exitosamente");
    }

    public List<DeliveryResponse> getPendingDeliveries(String token) {
        validateToken(token);

        List<Delivery> pendingDeliveries = deliveryRepository.findByDeliveryStatus("PENDIENTE");
        
        return pendingDeliveries.stream()
                .map(delivery -> convertToResponse(delivery, null))
                .collect(Collectors.toList());
    }

    public DeliveryResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request, String token) {
        validateToken(token);

        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        delivery.setDeliveryStatus(request.getNewStatus());
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        // Notificar a notification-service si la entrega fue completada
        if ("ENTREGADA".equalsIgnoreCase(updatedDelivery.getDeliveryStatus())) {
            NotificationServiceClient.DeliveryReceiptNotification notification = new NotificationServiceClient.DeliveryReceiptNotification();
            notification.deliveryId = updatedDelivery.getDeliveryId();
            notification.paymentId = updatedDelivery.getPaymentId();
            notification.userId = updatedDelivery.getUserId();
            notification.customerAddress = updatedDelivery.getCustomerAddress();
            notification.customerCity = updatedDelivery.getCustomerCity();
            notification.deliveryStatus = updatedDelivery.getDeliveryStatus();
            notification.deliveredAt = updatedDelivery.getUpdatedAt() != null ? updatedDelivery.getUpdatedAt().toString() : null;
            try {
                notificationServiceClient.notifyDelivery(notification);
            } catch (Exception e) {
                System.out.println("No se pudo notificar a notification-service: " + e.getMessage());
            }
        }

        return convertToResponse(updatedDelivery, "Estado de entrega actualizado exitosamente");
    }

    public List<DeliveryResponse> getDeliveriesByUser(String token) {
        Claims claims = JwtUtil.extractClaimsWithoutValidation(token);
        Integer userId = claims.get("id", Integer.class);

        List<Delivery> userDeliveries = deliveryRepository.findByUserId(userId);
        
        return userDeliveries.stream()
                .map(delivery -> convertToResponse(delivery, null))
                .collect(Collectors.toList());
    }

    public DeliveryResponse assignDriverToDelivery(Integer deliveryId, Integer driverId, String token) {
        validateToken(token);

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        delivery.setAssignedDriverId(driverId);
        delivery.setDeliveryStatus("ASIGNADA");
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        return convertToResponse(updatedDelivery, "Repartidor asignado exitosamente");
    }

    private void validateToken(String token) {
        try {
            Claims claims = JwtUtil.extractClaimsWithoutValidation(token);
            if (claims == null) {
                throw new RuntimeException("Token inválido");
            }
            
            try {
                String url = validateTokenUri + "?token=" + token;
                String response = restTemplate.getForObject(url, String.class);
                if (response == null || !response.contains("valid")) {
                    System.out.println("Warning: Token validation service not available, continuing with basic validation");
                }
            } catch (Exception e) {
                System.out.println("Warning: Could not validate token with auth service: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error validando token: " + e.getMessage());
        }
    }

    private DeliveryResponse convertToResponse(Delivery delivery, String message) {
        DeliveryResponse response = new DeliveryResponse();
        response.setDeliveryId(delivery.getDeliveryId());
        response.setPaymentId(delivery.getPaymentId());
        response.setUserId(delivery.getUserId());
        response.setCustomerAddress(delivery.getCustomerAddress());
        response.setCustomerCity(delivery.getCustomerCity());
        response.setDeliveryStatus(delivery.getDeliveryStatus());
        response.setCreatedAt(delivery.getCreatedAt());
        response.setUpdatedAt(delivery.getUpdatedAt());
        response.setAssignedDriverId(delivery.getAssignedDriverId());
        response.setMessage(message);
        return response;
    }
} 