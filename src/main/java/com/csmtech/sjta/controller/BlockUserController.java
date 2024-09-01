/**
 * @author prasanta.sethi
 */
package com.csmtech.sjta.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.BlockUserDTO;
import com.csmtech.sjta.repository.BlockUserRepository;
import com.csmtech.sjta.service.BlockUserService;

import lombok.extern.slf4j.Slf4j;

@RestController

@Slf4j
public class BlockUserController {

	@Autowired
	private BlockUserRepository blockUserRepository;

	@Autowired
	private BlockUserService blockUserService;

	private static final String STATUS_KEY = "status";
	private static final String MESSAGE_KEY = "message";
	private static final String ERROR = "error";
	private static final String BLOCK_STATUS = "userBlockStatus";

	@PostMapping("/block-user")
	public ResponseEntity<String> blockUser(@RequestBody BlockUserDTO blockUserDTO) {
		JSONObject response = new JSONObject();

		try {
			// Check if the user with the given userId exists
			Long existingUserId = blockUserRepository.checkCitizenExists(blockUserDTO.getUserId());
			if (existingUserId == null) {
				response.put(STATUS_KEY, ERROR);
				response.put(MESSAGE_KEY, "User not found with provided userId");
				return ResponseEntity.badRequest().body(response.toString());
			}

			Boolean status = blockUserDTO.getUserBlockStatus();

			if (Boolean.TRUE.equals(status)) {
				blockUserRepository.updateCitizenBlockStatusAndRemarks(blockUserDTO.getUserId(),
						blockUserDTO.getUserBlockStatus(), blockUserDTO.getBlockRemarks());

				response.put(STATUS_KEY, "fail");
				response.put(BLOCK_STATUS, status);
				response.put(MESSAGE_KEY, "Citizen blocked");
			} else {
				blockUserRepository.updateCitizenBlockStatusAndRemarks(blockUserDTO.getUserId(),
						blockUserDTO.getUserBlockStatus(), blockUserDTO.getBlockRemarks());
				response.put(STATUS_KEY, "success");
				response.put(BLOCK_STATUS, status);
				response.put(MESSAGE_KEY, "Citizen unblocked");
			}
		} catch (Exception e) {
			response.put(STATUS_KEY, ERROR);
			response.put(MESSAGE_KEY, "Error blocking/unblocking citizen: " + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.toString());
		}
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("/block-officer")
	public ResponseEntity<String> blockOfficer(@RequestBody BlockUserDTO blockUserDTO) {
		JSONObject response = new JSONObject();

		try {
			// Check if the user with the given userId exists
			Long existingUserId = blockUserRepository.checkUserExists(blockUserDTO.getUserId());
			if (existingUserId == null) {
				response.put(STATUS_KEY, ERROR);
				response.put(MESSAGE_KEY, "User not found with provided userId");
				return ResponseEntity.badRequest().body(response.toString());
			}

			Boolean status = blockUserDTO.getUserBlockStatus();

			if (Boolean.TRUE.equals(status)) {
				blockUserRepository.updateUserBlockStatusAndRemarks(blockUserDTO.getUserId(),
						blockUserDTO.getUserBlockStatus(), blockUserDTO.getBlockRemarks());

				response.put(STATUS_KEY, "fail");
				response.put(BLOCK_STATUS, status);
				response.put(MESSAGE_KEY, "User blocked successfully");
			} else {
				blockUserRepository.updateUserBlockStatusAndRemarks(blockUserDTO.getUserId(),
						blockUserDTO.getUserBlockStatus(), blockUserDTO.getBlockRemarks());
				response.put(STATUS_KEY, "success");
				response.put(BLOCK_STATUS, status);
				response.put(MESSAGE_KEY, "User unblocked successfully");
			}
		} catch (Exception e) {
			response.put(STATUS_KEY, ERROR);
			response.put(MESSAGE_KEY, "Error blocking/unblocking user: " + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.toString());
		}
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("/block-status")
	public Map<String, Boolean> getUserBlockStatus(@RequestBody BlockUserDTO request) {
		Long userId = request.getUserId();
		Boolean userBlockStatus = blockUserRepository.checkBlockStatus(userId);
		Map<String, Boolean> response = new HashMap<>();
		response.put(BLOCK_STATUS, userBlockStatus);
		return response;
	}
}
