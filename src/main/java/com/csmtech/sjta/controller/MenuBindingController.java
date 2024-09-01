package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.ModuleMenuDataDTO;
import com.csmtech.sjta.service.MenuBindingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/menumainurl")
@Slf4j

/**
 * @Auth Prasanta Kumar Sethi
 */

public class MenuBindingController {

	@Autowired
	private MenuBindingService service;
	String data = "";

	@PostMapping("/databinding")
	public ResponseEntity<List<ModuleMenuDataDTO>> getMenuData(@RequestBody String formParams)
			throws JsonMappingException, JsonProcessingException, JSONException {
		data = formParams;
		JSONObject json = new JSONObject(data);
		log.info(":: getMenuData() Execution are start !!");
		List<ModuleMenuDataDTO> respones = service.getModuleAndMenuByUserId((short) json.getInt("roleId"));
		if (respones.isEmpty()) {
			log.info(":: getMenuData()  return no record found");
			return ResponseEntity.ok(respones);
		}
		log.info(":: getMenuData()  return the result");
		return ResponseEntity.ok(respones);
	}

}
