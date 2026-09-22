package com.resolvenow.assignmentservice.service;

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

import com.resolvenow.assignmentservice.client.NotificationClient;
import com.resolvenow.assignmentservice.entity.Assignment;
import com.resolvenow.assignmentservice.repository.AssignmentRepository;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private AssignmentService assignmentService;

    private Assignment assignment;

    @BeforeEach
    void setUp() {
        assignment = new Assignment();

        assignment.setAssignmentId(1L);
        assignment.setComplaintId(101L);
        assignment.setUserId(201L);
        assignment.setAssignedTo("Ravi");
        assignment.setDepartment("Network");
    }

    @Test
    void createAssignment_ShouldSaveAssignmentAndSendNotification() {

        when(assignmentRepository.save(any(Assignment.class)))
                .thenReturn(assignment);

        Assignment result =
                assignmentService.createAssignment(assignment);

        assertNotNull(result);
        assertEquals(1L, result.getAssignmentId());
        assertEquals("Ravi", result.getAssignedTo());
        assertEquals("Network", result.getDepartment());

        verify(assignmentRepository, times(1))
                .save(any(Assignment.class));

        verify(notificationClient, times(1))
                .createNotification(any());
    }

    @Test
    void getAssignmentById_ShouldReturnAssignment() {

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.of(assignment));

        Assignment result =
                assignmentService.getAssignmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getAssignmentId());
        assertEquals(101L, result.getComplaintId());
        assertEquals("Ravi", result.getAssignedTo());
    }

    @Test
    void getAssignmentById_ShouldThrowException_WhenNotFound() {

        when(assignmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> assignmentService.getAssignmentById(999L)
        );
    }

    @Test
    void deleteAssignment_ShouldDeleteExistingAssignment() {

        when(assignmentRepository.existsById(1L))
                .thenReturn(true);

        assignmentService.deleteAssignment(1L);

        verify(assignmentRepository, times(1))
                .deleteById(1L);
    }

    @Test
    void deleteAssignment_ShouldThrowException_WhenNotFound() {

        when(assignmentRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> assignmentService.deleteAssignment(999L)
        );

        verify(assignmentRepository, never())
                .deleteById(999L);
    }

    @Test
    void getAssignmentsByComplaint_ShouldReturnAssignments() {

        when(assignmentRepository.findByComplaintId(101L))
                .thenReturn(java.util.List.of(assignment));

        var result =
                assignmentService.getAssignmentsByComplaint(101L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).getComplaintId());
    }
}