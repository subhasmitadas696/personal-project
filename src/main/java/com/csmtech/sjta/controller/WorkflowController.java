package com.csmtech.sjta.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.service.WorkflowService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.TokenCreaterAndMatcher;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WorkflowController {

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private TokenCreaterAndMatcher tokenCreater;

	@GetMapping(value = "/getallApprovalAction", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getallApprovalAction() {
		JSONObject response = new JSONObject();
		List<Map<String, Object>> getallOfficers = null;
		try {
			getallOfficers = workflowService.getallApprovalAction();
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put("msg", CommonConstant.SUCCESS);
			response.put(CommonConstant.RESULT, getallOfficers);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CommonConstant.RESPONSE_DATA,
				Base64.getEncoder().encodeToString(response.toString().getBytes()));
		jsonObject.put(CommonConstant.RESPONSE_TOKEN, TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(response.toString().getBytes())));
		return ResponseEntity.ok(jsonObject.toString());

	}

	@GetMapping(value = "/getallOfficersApi", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getallOfficersApi() {
		JSONObject response = new JSONObject();

		List<Map<String, Object>> getallOfficers = null;
		try {
			getallOfficers = workflowService.getallOfficersApi();
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put("msg", CommonConstant.SUCCESS);
			response.put(CommonConstant.RESULT, getallOfficers);

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CommonConstant.RESPONSE_DATA,
				Base64.getEncoder().encodeToString(response.toString().getBytes()));
		jsonObject.put(CommonConstant.RESPONSE_TOKEN, TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(response.toString().getBytes())));
		return ResponseEntity.ok(jsonObject.toString());
	}

	@PostMapping("/setWorkflow")
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> setWorkflow(@RequestBody String setWorkflow) {
		Integer errorFlag;
		JSONObject jsonObject = new JSONObject();
		String result = workflowService.setWorkflow(setWorkflow);
		if (result.equals(CommonConstant.SUCCESS)) {
			errorFlag = 0;
		} else {
			errorFlag = 1;
		}

		jsonObject.put(CommonConstant.RESPONSE_DATA,
				Base64.getEncoder().encodeToString(errorFlag.toString().getBytes()));
		jsonObject.put(CommonConstant.RESPONSE_TOKEN, TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(errorFlag.toString().getBytes())));
		return ResponseEntity.ok(jsonObject.toString());

	}

	@GetMapping(value = "/getFormName", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFormName() {
		JSONObject response = new JSONObject();
		List<Map<String, Object>> getallOfficers = null;
		try {
			getallOfficers = workflowService.getFormName();
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put("msg", CommonConstant.SUCCESS);
			response.put(CommonConstant.RESULT, getallOfficers);
		} catch (Exception e) {
			log.error(e.getMessage());
			response.put("msg", CommonConstant.ERROR);
			response.put(CommonConstant.STATUS_KEY, 400);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CommonConstant.RESPONSE_DATA,
				Base64.getEncoder().encodeToString(response.toString().getBytes()));
		jsonObject.put(CommonConstant.RESPONSE_TOKEN, TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(response.toString().getBytes())));
		return ResponseEntity.ok(jsonObject.toString());

	}

	@GetMapping(value = "/getDocumentList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDocumentList() {
		JSONObject response = new JSONObject();
		List<Map<String, Object>> getallDocuments = null;
		try {
			getallDocuments = workflowService.getDocumentList();
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put("msg", CommonConstant.SUCCESS);
			response.put(CommonConstant.RESULT, getallDocuments);
		} catch (Exception e) {
			log.error(e.getMessage());
			response.put("msg", CommonConstant.ERROR);
			response.put(CommonConstant.STATUS_KEY, 400);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CommonConstant.RESPONSE_DATA,
				Base64.getEncoder().encodeToString(response.toString().getBytes()));
		jsonObject.put(CommonConstant.RESPONSE_TOKEN, TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(response.toString().getBytes())));
		return ResponseEntity.ok(jsonObject.toString());

	}

	@PostMapping(value = "/fillWorkflow", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> fillWorkflow(@RequestParam("arrParam[serviceId]") String request,
			@RequestParam("arrParam[projectId]") String projectId,
			@RequestParam("arrParam[paymentType]") String paymentType,
			@RequestParam("arrParam[labelId]") String labelId, @RequestParam("arrParam[ctrlName]") String ctrlName,
			@RequestParam("arrParam[projectCategory]") String projectCategory,
			@RequestParam("arrParam[dynFilterDetails]") String dynFilterDetails) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("serviceId", request);
		jsonObject.put("projectId", projectId);
		jsonObject.put("paymentType", paymentType);
		jsonObject.put("labelId", labelId);
		jsonObject.put("ctrlName", ctrlName);
		jsonObject.put("projectCategory", projectCategory);
		jsonObject.put("vchDynFilter", dynFilterDetails);
		Map<String, Object> response = new HashMap<String, Object>();
		JSONObject getallOfficers = null;
		try {
			getallOfficers = workflowService.fillWorkflow(request, dynFilterDetails);
			response.put("errorFlag", 0);
			response.put("msg", CommonConstant.SUCCESS);
			if (getallOfficers.has(CommonConstant.RESULT)) {
				response.put(CommonConstant.RESULT, getallOfficers.getString(CommonConstant.RESULT));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

}
