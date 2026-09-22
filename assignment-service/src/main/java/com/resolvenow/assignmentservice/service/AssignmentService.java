package com.resolvenow.assignmentservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.resolvenow.assignmentservice.client.NotificationClient;
import com.resolvenow.assignmentservice.dto.NotificationRequest;
import com.resolvenow.assignmentservice.entity.Assignment;
import com.resolvenow.assignmentservice.repository.AssignmentRepository;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final NotificationClient notificationClient;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            NotificationClient notificationClient) {

        this.assignmentRepository = assignmentRepository;
        this.notificationClient = notificationClient;
    }

    public Assignment createAssignment(Assignment assignment) {

        Assignment savedAssignment =
                assignmentRepository.save(assignment);

        // Send notification to the actual complaint owner
        NotificationRequest notificationRequest =
                new NotificationRequest(
                        savedAssignment.getUserId(),
                        savedAssignment.getComplaintId(),
                        "Your complaint has been assigned to "
                                + savedAssignment.getAssignedTo()
                                + " from "
                                + savedAssignment.getDepartment()
                                + " department.",
                        "UNREAD"
                );

        notificationClient.createNotification(notificationRequest);

        return savedAssignment;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment getAssignmentById(Long assignmentId) {

        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() ->
                        new RuntimeException("Assignment not found"));
    }

    public List<Assignment> getAssignmentsByComplaint(Long complaintId) {
        return assignmentRepository.findByComplaintId(complaintId);
    }

    public List<Assignment> getAssignmentsByEmployee(String assignedTo) {
        return assignmentRepository.findByAssignedTo(assignedTo);
    }

    public List<Assignment> getAssignmentsByDepartment(String department) {
        return assignmentRepository.findByDepartment(department);
    }

    public void deleteAssignment(Long assignmentId) {

        if (!assignmentRepository.existsById(assignmentId)) {
            throw new RuntimeException("Assignment not found");
        }

        assignmentRepository.deleteById(assignmentId);
    }
}