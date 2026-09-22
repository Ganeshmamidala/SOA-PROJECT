package com.resolvenow.complaintservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.resolvenow.complaintservice.dto.AssignmentRequest;

@FeignClient(name = "assignment-service")
public interface AssignmentClient {

    @PostMapping("/assignments")
    void createAssignment(@RequestBody AssignmentRequest request);
}