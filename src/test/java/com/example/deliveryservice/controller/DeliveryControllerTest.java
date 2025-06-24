package com.example.deliveryservice.controller;

import com.example.deliveryservice.dto.DeliveryRequest;
import com.example.deliveryservice.dto.DeliveryResponse;
import com.example.deliveryservice.dto.UpdateDeliveryStatusRequest;
import com.example.deliveryservice.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {

    @Mock
    private DeliveryService deliveryService;

    @InjectMocks
    private DeliveryController deliveryController;

    private DeliveryRequest deliveryRequest;
    private DeliveryResponse deliveryResponse;
    private UpdateDeliveryStatusRequest updateRequest;

    @BeforeEach
    void setUp() {
        deliveryRequest = new DeliveryRequest();
        deliveryRequest.setPaymentId(1);
        deliveryRequest.setCustomerAddress("Calle 123");
        deliveryRequest.setCustomerCity("Bogotá");

        deliveryResponse = new DeliveryResponse();
        deliveryResponse.setDeliveryId(1);
        deliveryResponse.setPaymentId(1);
        deliveryResponse.setCustomerAddress("Calle 123");
        deliveryResponse.setCustomerCity("Bogotá");
        deliveryResponse.setDeliveryStatus("PENDIENTE");

        updateRequest = new UpdateDeliveryStatusRequest();
        updateRequest.setDeliveryId(1);
        updateRequest.setNewStatus("ENTREGADA");
    }

    @Test
    void createDelivery_WithValidRequest_ShouldReturnOk() {
        // Arrange
        String authHeader = "Bearer valid.token";
        when(deliveryService.createDelivery(any(DeliveryRequest.class), anyString()))
                .thenReturn(deliveryResponse);

        // Act
        ResponseEntity<DeliveryResponse> response = deliveryController.createDelivery(deliveryRequest, authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(deliveryResponse.getDeliveryId(), response.getBody().getDeliveryId());
        verify(deliveryService).createDelivery(deliveryRequest, "valid.token");
    }

    @Test
    void getPendingDeliveries_ShouldReturnOk() {
        // Arrange
        String authHeader = "Bearer valid.token";
        List<DeliveryResponse> pendingDeliveries = Arrays.asList(deliveryResponse);
        when(deliveryService.getPendingDeliveries(anyString())).thenReturn(pendingDeliveries);

        // Act
        ResponseEntity<List<DeliveryResponse>> response = deliveryController.getPendingDeliveries(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(deliveryService).getPendingDeliveries("valid.token");
    }

    @Test
    void updateDeliveryStatus_WithValidRequest_ShouldReturnOk() {
        // Arrange
        String authHeader = "Bearer valid.token";
        when(deliveryService.updateDeliveryStatus(any(UpdateDeliveryStatusRequest.class), anyString()))
                .thenReturn(deliveryResponse);

        // Act
        ResponseEntity<DeliveryResponse> response = deliveryController.updateDeliveryStatus(updateRequest, authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(deliveryService).updateDeliveryStatus(updateRequest, "valid.token");
    }

    @Test
    void getMyDeliveries_ShouldReturnOk() {
        // Arrange
        String authHeader = "Bearer valid.token";
        List<DeliveryResponse> myDeliveries = Arrays.asList(deliveryResponse);
        when(deliveryService.getDeliveriesByUser(anyString())).thenReturn(myDeliveries);

        // Act
        ResponseEntity<List<DeliveryResponse>> response = deliveryController.getMyDeliveries(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(deliveryService).getDeliveriesByUser("valid.token");
    }

    @Test
    void assignDriverToDelivery_ShouldReturnOk() {
        // Arrange
        String authHeader = "Bearer valid.token";
        Integer deliveryId = 1;
        Integer driverId = 5;
        when(deliveryService.assignDriverToDelivery(anyInt(), anyInt(), anyString()))
                .thenReturn(deliveryResponse);

        // Act
        ResponseEntity<DeliveryResponse> response = deliveryController.assignDriverToDelivery(deliveryId, driverId, authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(deliveryService).assignDriverToDelivery(deliveryId, driverId, "valid.token");
    }
} 