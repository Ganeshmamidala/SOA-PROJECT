package com.resolvenow.notificationservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resolvenow.notificationservice.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    List<Notification> findByComplaintId(Long complaintId);

    List<Notification> findByStatus(String status);
}