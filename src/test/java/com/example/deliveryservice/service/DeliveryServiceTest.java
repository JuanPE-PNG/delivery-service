package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.DeliveryRequest;
import com.example.deliveryservice.dto.DeliveryResponse;
import com.example.deliveryservice.dto.UpdateDeliveryStatusRequest;
import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.repository.DeliveryRepository;
import com.example.deliveryservice.client.NotificationServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @InjectMocks
    private DeliveryService deliveryService;

    private DeliveryRequest deliveryRequest;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        deliveryRequest = new DeliveryRequest();
        deliveryRequest.setPaymentId(1);
        deliveryRequest.setCustomerAddress("Calle 123");
        deliveryRequest.setCustomerCity("Bogotá");

        delivery = new Delivery();
        delivery.setDeliveryId(1);
        delivery.setPaymentId(1);
        delivery.setUserId(100);
        delivery.setCustomerAddress("Calle 123");
        delivery.setCustomerCity("Bogotá");
        delivery.setDeliveryStatus("PENDIENTE");
        delivery.setCreatedAt(LocalDateTime.now());
        delivery.setUpdatedAt(LocalDateTime.now());

        // Set the validateTokenUri property
        ReflectionTestUtils.setField(deliveryService, "validateTokenUri", "http://localhost:8080/validate");
    }

    @Test
    void convertToResponse_ShouldReturnCorrectResponse() {
        // Arrange
        String message = "Test message";

        // Act
        DeliveryResponse response = deliveryService.convertToResponse(delivery, message);

        // Assert
        assertNotNull(response);
        assertEquals(delivery.getDeliveryId(), response.getDeliveryId());
        assertEquals(delivery.getPaymentId(), response.getPaymentId());
        assertEquals(delivery.getCustomerAddress(), response.getCustomerAddress());
        assertEquals(delivery.getCustomerCity(), response.getCustomerCity());
        assertEquals(delivery.getDeliveryStatus(), response.getDeliveryStatus());
        assertEquals(message, response.getMessage());
    }

    @Test
    void convertToResponse_WithNullMessage_ShouldReturnResponseWithoutMessage() {
        // Act
        DeliveryResponse response = deliveryService.convertToResponse(delivery, null);

        // Assert
        assertNotNull(response);
        assertEquals(delivery.getDeliveryId(), response.getDeliveryId());
        assertNull(response.getMessage());
    }
} 