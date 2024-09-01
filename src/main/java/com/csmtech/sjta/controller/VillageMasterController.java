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

import com.csmtech.sjta.entity.VillageBoundary;
import com.csmtech.sjta.service.VillageMasterService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.VillageMasterValidationCheck;

@RestController

@RequestMapping("/land-bank")
public class VillageMasterController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private VillageMasterService villageMasterService;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(VillageMasterController.class);
	JSONObject resp = new JSONObject();

	@PostMapping("/village-master/addEdit")
	public ResponseEntity<?> create(@RequestBody String villageMaster) {
		logger.info("Inside create method of Village_masterController");
		JSONObject requestObj = new JSONObject(villageMaster);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(villageMaster);
			String validationMsg = VillageMasterValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of Village_masterController Validation Error");
			} else {
				resp = villageMasterService.save(data);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/village-master/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of Village_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = villageMasterService.getById(json.getString("intId"));
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put("result", entity);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/village-master/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of Village_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = villageMasterService.getAll(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/village-master/viewall")
	public ResponseEntity<?> viewAll(@RequestBody VillageBoundary vh) {
		logger.info("Inside getAll method of village_masterController");
		resp = villageMasterService.viewAll(vh.getTahasilCode());
		return ResponseEntity.ok(resp.toString());
	}

}