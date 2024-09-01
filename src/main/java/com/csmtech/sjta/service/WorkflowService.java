package com.csmtech.sjta.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public interface WorkflowService {

	

	List<Map<String, Object>> getallApprovalAction();
	List<Map<String, Object>> getallOfficersApi();
	String setWorkflow(String setWorkflow);
	List<Map<String, Object>> getFormName();
	JSONObject fillWorkflow(String request, String dynFilterDetails);
	List<Map<String, Object>> getDocumentList();
	

}
