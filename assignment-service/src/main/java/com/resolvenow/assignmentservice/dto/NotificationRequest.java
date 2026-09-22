package com.resolvenow.assignmentservice.dto;

public class NotificationRequest {

    private Long userId;
    private Long complaintId;
    private String message;
    private String status;

    public NotificationRequest() {
    }

    public NotificationRequest(Long userId, Long complaintId,
                               String message, String status) {
        this.userId = userId;
        this.complaintId = complaintId;
        this.message = message;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}