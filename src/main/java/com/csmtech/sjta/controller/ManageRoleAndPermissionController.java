package com.csmtech.sjta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.ModuleMenuDTO;
import com.csmtech.sjta.dto.RoleDTO;
import com.csmtech.sjta.dto.SetPermissionRespones;
import com.csmtech.sjta.entity.Department;
import com.csmtech.sjta.entity.RoleEntity;
import com.csmtech.sjta.service.ManageRoleAndPermissionService;
import com.csmtech.sjta.util.CommonConstant;

@RestController
@RequestMapping("/mainpermission")

public class ManageRoleAndPermissionController {

	private static final Logger log = LoggerFactory.getLogger(ManageRoleAndPermissionController.class);

	@Autowired
	private ManageRoleAndPermissionService service;

	@PostMapping("/getalldept")
	public ResponseEntity<Map<String, Object>> getAllDept() {
		Map<String, Object> response = new HashMap<>();
		List<Department> list = service.getAllDept();
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, "DATA FOUND");
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, "NO DATA FOUND");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getallrole")
	public ResponseEntity<Map<String, Object>> getAllRoleByDepartment(@RequestBody RoleDTO role) {
		Map<String, Object> response = new HashMap<>();
		List<RoleEntity> list = service.getAllRoleByDepartment(role.getDepartmentId());
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, "DATA FOUND");
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, "NO DATA FOUND");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getrole")
	public ResponseEntity<List<RoleDTO>> getRoles() {
		return ResponseEntity.ok(service.getRoles());
	}

	@GetMapping("/getmodulewisemenu")
	public ResponseEntity<List<ModuleMenuDTO>> getModulesWithMenus(@RequestParam("rollId") Integer rollId) {
		return ResponseEntity.ok(service.getModulesWithMenus(rollId));
	}

	@PostMapping("/saveBatchrecord")
	public ResponseEntity<?> saveRollModuleMenuDetails(@RequestBody List<SetPermissionRespones> dataToInsertOrUpdate) {
		try {
			log.info("save method started");
			Integer respones = service.batchInsertOrUpdateSetPermissionTestR(dataToInsertOrUpdate);
			log.info("save method ended");
			if (respones > 0) {
				JSONObject response = new JSONObject();
				response.put(CommonConstant.STATUS_KEY, 200);
				response.put(CommonConstant.RESULT, respones);
				return ResponseEntity.ok(response.toString());
			} else {
				return ResponseEntity.ok("Not Save.");

			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving data.");
		}
	}

	@PostMapping("/updateBatchrecord")
	public ResponseEntity<?> batchUpdateSetPermissionTestR(
			@RequestBody List<SetPermissionRespones> dataToInsertOrUpdate) {
		try {
			Integer respones = service.batchUpdateSetPermissionTestR(dataToInsertOrUpdate);
			if (respones > 0) {
				JSONObject response = new JSONObject();
				response.put(CommonConstant.STATUS_KEY, 200);
				response.put(CommonConstant.RESULT, respones);
				return ResponseEntity.ok(response.toString());
			}
			return ResponseEntity.ok("Not Update.");
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving data.");
		}
	}
}
