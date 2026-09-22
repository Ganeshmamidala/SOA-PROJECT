package com.resolvenow.complaintservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resolvenow.complaintservice.entity.Complaint;
import com.resolvenow.complaintservice.service.ComplaintService;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    // Gets the unique Docker/Eureka instance ID.
    // Uses a test-friendly default when the property is unavailable.
    @Value("${eureka.instance.instance-id:complaint-service-test}")
    private String instanceId;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // Create a new complaint
    @PostMapping
    public ResponseEntity<Complaint> createComplaint(
            @RequestBody Complaint complaint) {

        System.out.println("========================================");
        System.out.println("CREATE COMPLAINT");
        System.out.println("REQUEST HANDLED BY: " + instanceId);
        System.out.println("========================================");

        return ResponseEntity.ok(
                complaintService.createComplaint(complaint)
        );
    }

    // Get all complaints
    @GetMapping
    public ResponseEntity<List<Complaint>> getAllComplaints() {

        System.out.println("========================================");
        System.out.println("GET ALL COMPLAINTS");
        System.out.println("REQUEST HANDLED BY: " + instanceId);
        System.out.println("========================================");

        return ResponseEntity.ok(
                complaintService.getAllComplaints()
        );
    }

    // Get complaint by ID
    @GetMapping("/{complaintId}")
    public ResponseEntity<Complaint> getComplaintById(
            @PathVariable Long complaintId) {

        System.out.println("========================================");
        System.out.println("GET COMPLAINT BY ID: " + complaintId);
        System.out.println("REQUEST HANDLED BY: " + instanceId);
        System.out.println("========================================");

        return ResponseEntity.ok(
                complaintService.getComplaintById(complaintId)
        );
    }

    // Get complaints of a particular user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Complaint>> getComplaintsByUser(
            @PathVariable Long userId) {

        System.out.println("========================================");
        System.out.println("GET COMPLAINTS FOR USER: " + userId);
        System.out.println("REQUEST HANDLED BY: " + instanceId);
        System.out.println("========================================");

        return ResponseEntity.ok(
                complaintService.getComplaintsByUser(userId)
        );
    }

    // Update complaint status
    @PutMapping("/{complaintId}/status")
    public ResponseEntity<Complaint> updateStatus(
            @PathVariable Long complaintId,
            @RequestParam String status) {

        System.out.println("========================================");
        System.out.println("UPDATE COMPLAINT STATUS");
        System.out.println("COMPLAINT ID: " + complaintId);
        System.out.println("NEW STATUS: " + status);
        System.out.println("REQUEST HANDLED BY: " + instanceId);
        System.out.println("========================================");

        return ResponseEntity.ok(
                complaintService.updateStatus(complaintId, status)
        );
    }

    // Delete complaint
    @DeleteMapping("/{complaintId}")
    public ResponseEntity<String> deleteComplaint(
            @PathVariable Long complaintId) {

        System.out.println("========================================");
        System.out.println("DELETE COMPLAINT: " + complaintId);
        System.out.println("REQUEST HANDLED BY: " + instanceId);
        System.out.println("========================================");

        complaintService.deleteComplaint(complaintId);

        return ResponseEntity.ok(
                "Complaint deleted successfully"
        );
    }
}