package com.resolvenow.notificationservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.resolvenow.notificationservice.entity.Notification;
import com.resolvenow.notificationservice.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {

        notification = new Notification();

        notification.setNotificationId(1L);
        notification.setUserId(101L);
        notification.setComplaintId(201L);
        notification.setMessage(
                "Your complaint has been assigned."
        );
        notification.setStatus("UNREAD");
    }

    @Test
    void createNotification_ShouldSaveNotification() {

        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(notification);

        Notification result =
                notificationService.createNotification(notification);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getNotificationId()
        );

        assertEquals(
                101L,
                result.getUserId()
        );

        assertEquals(
                "UNREAD",
                result.getStatus()
        );

        verify(
                notificationRepository,
                times(1)
        ).save(any(Notification.class));
    }

    @Test
    void getNotificationById_ShouldReturnNotification() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        Notification result =
                notificationService.getNotificationById(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getNotificationId()
        );

        assertEquals(
                101L,
                result.getUserId()
        );
    }

    @Test
    void getNotificationById_ShouldThrowException_WhenNotFound() {

        when(notificationRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> notificationService.getNotificationById(999L)
        );
    }

    @Test
    void getNotificationsByUser_ShouldReturnNotifications() {

        when(notificationRepository.findByUserId(101L))
                .thenReturn(List.of(notification));

        List<Notification> result =
                notificationService.getNotificationsByUser(101L);

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                101L,
                result.get(0).getUserId()
        );
    }

    @Test
    void updateStatus_ShouldUpdateStatus() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(notification);

        Notification result =
                notificationService.updateStatus(
                        1L,
                        "READ"
                );

        assertNotNull(result);

        assertEquals(
                "READ",
                result.getStatus()
        );

        verify(
                notificationRepository,
                times(1)
        ).save(any(Notification.class));
    }

    @Test
    void deleteNotification_ShouldDeleteExistingNotification() {

        when(notificationRepository.existsById(1L))
                .thenReturn(true);

        notificationService.deleteNotification(1L);

        verify(
                notificationRepository,
                times(1)
        ).deleteById(1L);
    }
}