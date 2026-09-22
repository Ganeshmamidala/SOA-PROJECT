package com.resolvenow.assignmentservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resolvenow.assignmentservice.entity.Assignment;
import com.resolvenow.assignmentservice.service.AssignmentService;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // Create a new assignment
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @RequestBody Assignment assignment) {

        return ResponseEntity.ok(
                assignmentService.createAssignment(assignment)
        );
    }

    // Get all assignments
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {

        return ResponseEntity.ok(
                assignmentService.getAllAssignments()
        );
    }

    // Get assignment by ID
    @GetMapping("/{assignmentId}")
    public ResponseEntity<Assignment> getAssignmentById(
            @PathVariable Long assignmentId) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentById(assignmentId)
        );
    }

    // Get assignments for a complaint
    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByComplaint(
            @PathVariable Long complaintId) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByComplaint(complaintId)
        );
    }

    // Get assignments for an employee
    @GetMapping("/employee/{assignedTo}")
    public ResponseEntity<List<Assignment>> getAssignmentsByEmployee(
            @PathVariable String assignedTo) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByEmployee(assignedTo)
        );
    }

    // Get assignments for a department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Assignment>> getAssignmentsByDepartment(
            @PathVariable String department) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByDepartment(department)
        );
    }

    // Delete assignment
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(
            @PathVariable Long assignmentId) {

        assignmentService.deleteAssignment(assignmentId);

        return ResponseEntity.ok(
                "Assignment deleted successfully"
        );
    }
}