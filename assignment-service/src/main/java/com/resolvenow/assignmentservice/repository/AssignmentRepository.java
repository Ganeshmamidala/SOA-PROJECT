package com.resolvenow.assignmentservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resolvenow.assignmentservice.entity.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByComplaintId(Long complaintId);

    List<Assignment> findByAssignedTo(String assignedTo);

    List<Assignment> findByDepartment(String department);
}