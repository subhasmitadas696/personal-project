package com.csmtech.sjta.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.LandAllotmentConfigurationDTO;
import com.csmtech.sjta.entity.LandAllotmentConfigurationEntity;
import com.csmtech.sjta.service.LandAllotmentConfigurationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.LandAllotmentValidationCheck;

@RestController
@RequestMapping("/landAllotment")
public class LandAllotmentConfigurationController {

	@Autowired
	private LandAllotmentConfigurationService service;

	JSONObject js = new JSONObject();

	@PostMapping("/allotmentConfiguration")
	public ResponseEntity<?> create(@RequestBody LandAllotmentConfigurationEntity dto) {
		if (LandAllotmentValidationCheck.BackendValidation(dto) != null) {
			js.put(CommonConstant.STATUS_KEY, 502);
			js.put("errMsg", LandAllotmentValidationCheck.BackendValidation(dto));
		} else {
			Map<String, Object> status = service.landAlertmentSaveRecord(dto);
			if (status.get("landId") != null) {
				js.put(CommonConstant.STATUS_KEY, 200);
				js.put("landId", status.get("landId"));
				js.put("successMsg", "Data Inserted / Updated Successfully.");
			} else {
				js.put(CommonConstant.STATUS_KEY, 404);
				js.put("errMsg", "Record Not Insert");
			}

		}
		return ResponseEntity.ok(js.toString());
	}

	@PostMapping("/allRecord")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			js.put(CommonConstant.RESULT, service.getAllRecord(CommonUtil.inputStreamDecoder(formParams)));
			js.put("count", service.getCount());
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/allRecordById")
	public ResponseEntity<?> getRecordyId(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			String data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject jss = new JSONObject(data);
			Object result = service.getFindById(jss.getBigInteger("landId"));
			js.put(CommonConstant.RESULT, result);
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/removeRecordById")
	public ResponseEntity<?> remoreRecordById(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			String data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject jss = new JSONObject(data);
			Integer result = service.updatDeleteFlageId(jss.getBigInteger("landId"));
			if (result > 0) {
				js.put(CommonConstant.RESULT, "Delete Success");
				js.put(CommonConstant.STATUS_KEY, 200);
			} else {
				js.put(CommonConstant.RESULT, "Error Occure");
				js.put(CommonConstant.STATUS_KEY, 404);
			}
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/allRecordByIdMore")
	public ResponseEntity<?> auctionPlotDetrailsId(@RequestBody String formParams) {
		JSONObject response = new JSONObject(formParams);
		JSONObject json = new JSONObject();
		List<LandAllotmentConfigurationDTO> respones = service.getFindByIdMore(response.getBigInteger("landId"));
		if (respones == null) {
			json.put(CommonConstant.STATUS_KEY, 404);
			json.put(CommonConstant.RESULT, "No Record Found ..!!");
			return ResponseEntity.ok(json.toString());
		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.RESULT, respones);
		return ResponseEntity.ok(json.toString());
	}

}
