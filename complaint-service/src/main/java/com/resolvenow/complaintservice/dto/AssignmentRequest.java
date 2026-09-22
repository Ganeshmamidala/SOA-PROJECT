package com.resolvenow.complaintservice.dto;

public class AssignmentRequest {

    private Long complaintId;
    private Long userId;
    private String assignedTo;
    private String department;

    public AssignmentRequest() {
    }

    public AssignmentRequest(
            Long complaintId,
            Long userId,
            String assignedTo,
            String department) {

        this.complaintId = complaintId;
        this.userId = userId;
        this.assignedTo = assignedTo;
        this.department = department;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}