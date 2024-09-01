package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.entity.TahasilBoundary;
import com.csmtech.sjta.entity.Tahasil_master;
import com.csmtech.sjta.service.TahasilMasterService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.TahasilMasterValidationCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController

@RequestMapping("/land-bank")
public class TahasilMasterController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private TahasilMasterService tahasil_masterService;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(TahasilMasterController.class);
	JSONObject resp = new JSONObject();

	@PostMapping("/tahasil-master/addEdit")
	public ResponseEntity<?> create(@RequestBody String tahasil_master) {
		logger.info("Inside create method of Tahasil_masterController");
		JSONObject requestObj = new JSONObject(tahasil_master);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(tahasil_master);
			String validationMsg = TahasilMasterValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of Tahasil_masterController Validation Error");
			} else {
				resp = tahasil_masterService.save(data);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/tahasil-master/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of Tahasil_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = tahasil_masterService.getById(json.getString("intId"));
			resp.put("status", 200);
			resp.put("result", entity);
		} else {
			resp.put("msg",CommonConstant.ERROR);
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/tahasil-master/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of Tahasil_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = tahasil_masterService.getAll(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/tahasil-master/viewall")
	public ResponseEntity<?> viewAll(@RequestBody TahasilBoundary th) {
		logger.info("Inside getAll method of tahasil_masterController");
		resp = tahasil_masterService.viewAll(th.getDistrictCode());
		return ResponseEntity.ok(resp.toString());
	}

	@PostMapping("/tahasil-master/getlasttahasilcode")
	public ResponseEntity<?> getLastTahasilCode(@RequestBody String formParams) throws JsonProcessingException {
		logger.info("Inside getLastTahasilCode method of tahasil_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			ObjectMapper om = new ObjectMapper();
			Tahasil_master tahasilDto = om.readValue(data, Tahasil_master.class);
			resp = tahasil_masterService.getLastTahasilCode(tahasilDto.getSelDistrictCode());

		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}
}