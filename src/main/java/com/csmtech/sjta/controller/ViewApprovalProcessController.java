package com.csmtech.sjta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.ApprovalDTO;
import com.csmtech.sjta.service.ApprovalService;

/**
 * @author guru.prasad
 */
@RestController
@RequestMapping("/api/admin")

public class ViewApprovalProcessController {
	
	@Autowired
    private ApprovalService approvalService;
	
	private static final Logger log = LoggerFactory.getLogger(ViewApprovalProcessController.class);
	
	@GetMapping("/testing")
	public String test() {
		return "testing";
	}
	
	@GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getApprovalData() {
        Map<String, Object> response = new HashMap<>();

        try {
        	log.info("get started...");
            List<ApprovalDTO> approvalData = approvalService.getApprovalData();
            log.info("get ended...");
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Data retrieved successfully");
            response.put("result", approvalData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
			log.error("Error occurred while getting details: {}", e.getMessage());

            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "An error occurred while processing the request");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
