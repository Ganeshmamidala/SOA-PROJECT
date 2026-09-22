package com.resolvenow.complaintservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.resolvenow.complaintservice.client.AssignmentClient;
import com.resolvenow.complaintservice.client.NotificationClient;
import com.resolvenow.complaintservice.dto.AssignmentRequest;
import com.resolvenow.complaintservice.dto.NotificationRequest;
import com.resolvenow.complaintservice.entity.Complaint;
import com.resolvenow.complaintservice.repository.ComplaintRepository;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final AssignmentClient assignmentClient;
    private final NotificationClient notificationClient;

    public ComplaintService(
            ComplaintRepository complaintRepository,
            AssignmentClient assignmentClient,
            NotificationClient notificationClient) {

        this.complaintRepository = complaintRepository;
        this.assignmentClient = assignmentClient;
        this.notificationClient = notificationClient;
    }

    // Create a new complaint
    public Complaint createComplaint(Complaint complaint) {

        complaint.setStatus("CREATED");

        // First save the complaint
        Complaint savedComplaint =
                complaintRepository.save(complaint);

        // Decide department and employee
        // based on complaint description
        String description =
                savedComplaint.getDescription().toLowerCase();

        String assignedTo;
        String department;

        if (description.contains("internet")
                || description.contains("wifi")
                || description.contains("network")) {

            assignedTo = "Ravi";
            department = "Network";

        } else if (description.contains("payment")
                || description.contains("refund")
                || description.contains("billing")) {

            assignedTo = "Priya";
            department = "Finance";

        } else if (description.contains("account")
                || description.contains("login")
                || description.contains("password")) {

            assignedTo = "Arjun";
            department = "IT Support";

        } else {

            assignedTo = "Rahul";
            department = "General Support";
        }

        // Create assignment automatically
        AssignmentRequest assignmentRequest =
                new AssignmentRequest(
                        savedComplaint.getComplaintId(),
                        savedComplaint.getUserId(),
                        assignedTo,
                        department
                );

        assignmentClient.createAssignment(assignmentRequest);

        return savedComplaint;
    }

    // Get all complaints
    public List<Complaint> getAllComplaints() {

        return complaintRepository.findAll();
    }

    // Get complaint by ID
    public Complaint getComplaintById(Long complaintId) {

        return complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new RuntimeException("Complaint not found"));
    }

    // Get complaints by user
    public List<Complaint> getComplaintsByUser(Long userId) {

        return complaintRepository.findByUserId(userId);
    }

    // Update complaint status
    public Complaint updateStatus(
            Long complaintId,
            String status) {

        Complaint complaint =
                getComplaintById(complaintId);

        complaint.setStatus(status);

        Complaint updatedComplaint =
                complaintRepository.save(complaint);

        // Create notification for complaint owner
        NotificationRequest notificationRequest =
                new NotificationRequest(
                        complaint.getUserId(),
                        complaint.getComplaintId(),
                        "Your complaint status has been updated to "
                                + status + ".",
                        "UNREAD"
                );

        notificationClient.createNotification(
                notificationRequest
        );

        return updatedComplaint;
    }

    // Delete complaint
    public void deleteComplaint(Long complaintId) {

        if (!complaintRepository.existsById(complaintId)) {

            throw new RuntimeException(
                    "Complaint not found");
        }

        complaintRepository.deleteById(complaintId);
    }
}