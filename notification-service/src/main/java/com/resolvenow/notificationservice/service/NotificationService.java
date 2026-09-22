package com.resolvenow.notificationservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.resolvenow.notificationservice.entity.Notification;
import com.resolvenow.notificationservice.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification) {

        if (notification.getStatus() == null
                || notification.getStatus().isBlank()) {

            notification.setStatus("UNREAD");
        }

        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Long notificationId) {

        return notificationRepository.findById(notificationId)
                .orElseThrow(
                        () -> new RuntimeException("Notification not found")
                );
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getNotificationsByComplaint(Long complaintId) {
        return notificationRepository.findByComplaintId(complaintId);
    }

    public List<Notification> getNotificationsByStatus(String status) {
        return notificationRepository.findByStatus(status);
    }

    public Notification updateStatus(
            Long notificationId,
            String status) {

        Notification notification =
                getNotificationById(notificationId);

        notification.setStatus(status);

        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId) {

        if (!notificationRepository.existsById(notificationId)) {
            throw new RuntimeException("Notification not found");
        }

        notificationRepository.deleteById(notificationId);
    }
}