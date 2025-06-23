package com.example.deliveryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {
    @PostMapping("/api/notifications/delivery")
    void notifyDelivery(@RequestBody DeliveryReceiptNotification notification);

    class DeliveryReceiptNotification {
        public Integer deliveryId;
        public Integer paymentId;
        public Integer userId;
        public String customerAddress;
        public String customerCity;
        public String deliveryStatus;
        public String deliveredAt;
    }
} 