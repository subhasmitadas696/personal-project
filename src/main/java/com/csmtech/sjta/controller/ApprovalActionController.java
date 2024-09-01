package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.ApprovalActionResultDTO;
import com.csmtech.sjta.dto.ApprovalActionUpdateDTO;
import com.csmtech.sjta.dto.ApprovalDocumentDTO;
import com.csmtech.sjta.service.ApprovalActionService;
import com.csmtech.sjta.util.CommonConstant;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/approvalAction")

@Slf4j
public class ApprovalActionController {

	@Autowired
	private ApprovalActionService approvalService;

	@PostMapping("/actionsdata")
	public ResponseEntity<?> getAction(@RequestBody String formParams) {
		JSONObject json = new JSONObject(formParams);
		List<ApprovalActionResultDTO> respones = approvalService.findApprovalActionsForRoleId(json.getLong("roleId"));
		if (respones.isEmpty()) {
			log.info(":: no record found..!!");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, CommonConstant.NO_RECORD);
			jsb.put(CommonConstant.RESULT, "No Record Found");
			return ResponseEntity.ok(jsb.toString());
		}
		log.info(":: Record Are return sucessfully..!!");
		return ResponseEntity.ok(respones);
	}

	@PostMapping("/updateAprovalAction")
	public ResponseEntity<?> updateApprovalProcss(@RequestBody ApprovalActionUpdateDTO approvalDto) {
		Short result = approvalService.updateApprovalProcess(approvalDto);
		// message data
		String messagedata = approvalService.messageData(approvalDto.getNewApprovalActionId());
		JSONObject jsb = new JSONObject();
		jsb.put(CommonConstant.STATUS_KEY, CommonConstant.SUCCESS_CODE);
		jsb.put(CommonConstant.RESULT, result);
		jsb.put("message", messagedata);
		return ResponseEntity.ok(jsb.toString());
	}

	@PostMapping("/actionDocumentCheckListData")
	public ResponseEntity<?> getDocument(@RequestBody String formParams) {
		JSONObject json = new JSONObject(formParams);
		List<ApprovalDocumentDTO> respones = approvalService.findApprovaldocumentForRoleId(json.getLong("roleId"));
		if (respones.isEmpty()) {
			log.info(":: no record found..!!");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, CommonConstant.NO_RECORD);
			jsb.put(CommonConstant.RESULT, "No Record Found");
			return ResponseEntity.ok(jsb.toString());
		}
		log.info(":: Record Are return sucessfully..!!");
		return ResponseEntity.ok(respones);
	}
}
