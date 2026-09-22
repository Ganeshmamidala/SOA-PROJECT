package com.resolvenow.complaintservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.resolvenow.complaintservice.dto.NotificationRequest;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/notifications")
    Object createNotification(
            @RequestBody NotificationRequest request);
}