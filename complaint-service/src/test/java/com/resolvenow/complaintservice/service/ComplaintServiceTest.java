package com.resolvenow.complaintservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.resolvenow.complaintservice.client.AssignmentClient;
import com.resolvenow.complaintservice.client.NotificationClient;
import com.resolvenow.complaintservice.entity.Complaint;
import com.resolvenow.complaintservice.repository.ComplaintRepository;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private AssignmentClient assignmentClient;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private ComplaintService complaintService;

    private Complaint complaint;

    @BeforeEach
    void setUp() {
        complaint = new Complaint();

        complaint.setComplaintId(1L);
        complaint.setUserId(101L);
        complaint.setDescription("My WiFi internet is not working");
        complaint.setStatus("CREATED");
    }

    @Test
    void createComplaint_ShouldSaveComplaintAndCreateAssignment() {

        when(complaintRepository.save(any(Complaint.class)))
                .thenReturn(complaint);

        Complaint result =
                complaintService.createComplaint(complaint);

        assertNotNull(result);
        assertEquals("CREATED", result.getStatus());

        verify(complaintRepository, times(1))
                .save(any(Complaint.class));

        verify(assignmentClient, times(1))
                .createAssignment(any());
    }

    @Test
    void getComplaintById_ShouldReturnComplaint() {

        when(complaintRepository.findById(1L))
                .thenReturn(Optional.of(complaint));

        Complaint result =
                complaintService.getComplaintById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getComplaintId());
        assertEquals(101L, result.getUserId());
    }

    @Test
    void getComplaintById_ShouldThrowException_WhenNotFound() {

        when(complaintRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> complaintService.getComplaintById(999L)
        );
    }

    @Test
    void updateStatus_ShouldUpdateComplaintAndSendNotification() {

        when(complaintRepository.findById(1L))
                .thenReturn(Optional.of(complaint));

        when(complaintRepository.save(any(Complaint.class)))
                .thenReturn(complaint);

        Complaint result =
                complaintService.updateStatus(1L, "RESOLVED");

        assertEquals("RESOLVED", result.getStatus());

        verify(complaintRepository, times(1))
                .save(any(Complaint.class));

        verify(notificationClient, times(1))
                .createNotification(any());
    }

    @Test
    void deleteComplaint_ShouldDeleteExistingComplaint() {

        when(complaintRepository.existsById(1L))
                .thenReturn(true);

        complaintService.deleteComplaint(1L);

        verify(complaintRepository, times(1))
                .deleteById(1L);
    }

    @Test
    void deleteComplaint_ShouldThrowException_WhenNotFound() {

        when(complaintRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> complaintService.deleteComplaint(999L)
        );

        verify(complaintRepository, never())
                .deleteById(999L);
    }
}