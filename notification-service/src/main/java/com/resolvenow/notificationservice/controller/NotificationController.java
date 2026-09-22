package com.resolvenow.notificationservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resolvenow.notificationservice.entity.Notification;
import com.resolvenow.notificationservice.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Notification notification) {

        return ResponseEntity.ok(
                notificationService.createNotification(notification)
        );
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {

        return ResponseEntity.ok(
                notificationService.getAllNotifications()
        );
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<Notification> getNotificationById(
            @PathVariable Long notificationId) {

        return ResponseEntity.ok(
                notificationService.getNotificationById(notificationId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByUser(userId)
        );
    }

    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<List<Notification>> getNotificationsByComplaint(
            @PathVariable Long complaintId) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByComplaint(complaintId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Notification>> getNotificationsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByStatus(status)
        );
    }

    @PutMapping("/{notificationId}/status")
    public ResponseEntity<Notification> updateStatus(
            @PathVariable Long notificationId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                notificationService.updateStatus(notificationId, status)
        );
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long notificationId) {

        notificationService.deleteNotification(notificationId);

        return ResponseEntity.ok("Notification deleted successfully");
    }
}