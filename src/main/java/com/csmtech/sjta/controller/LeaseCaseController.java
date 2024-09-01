package com.csmtech.sjta.controller;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.csmtech.sjta.dto.LeaseCaseDTO;
import com.csmtech.sjta.entity.LeaseCasePaymantEntity;
import com.csmtech.sjta.service.LeaseCaseService;
import com.csmtech.sjta.util.BidderRegistraraValidationCheck;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/lease-case")
@Slf4j
public class LeaseCaseController {

	@Autowired
	private LeaseCaseService leaseService;

	String data = null;
	JSONObject resp = new JSONObject();

	@PostMapping("/case-files/getleaseCaseAllRecord")
	public ResponseEntity<?> getleaseCaseAllRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LeaseCaseDTO> districtList = leaseService.getleaseCaseAllRecord(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				resp.put("count", leaseService.getcountFromgetleaseCaseAllRecord(data));
				log.info("return controller of getleaseCaseAllRecord method success..!!");
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found..!!");
				log.info("getleaseCaseAllRecord  return no record found");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/case-files/getleaseCaseAllRecordWitId")
	public ResponseEntity<?> getleaseCaseAllRecordWitId(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LeaseCaseDTO> districtList = leaseService.getleaseCaseAllRecordWithId(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				resp.put("count", leaseService.getcountFromgetleaseCaseAllRecordWithId(data));
				log.info("return controller of getleaseCaseAllRecordWitId method success..!!");
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found..!!");
				log.info("getleaseCaseAllRecordWitId  return no record found");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/case-files/getleaseCaseAllLeaseCasePlotDetailsId")
	public ResponseEntity<?> getleaseCaseAllLeaseCasePlotDetailsId(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LeaseCaseDTO> districtList = leaseService.getleaseCaseAllRecordWitjLeaseCaseStatusDetails(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				log.info("return controller of getleaseCaseAllLeaseCasePlotDetailsId method success..!!");
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found..!!");
				log.info("getleaseCaseAllLeaseCasePlotDetailsId  return no record found");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/case-files/addEdit")
	public ResponseEntity<?> saveRecordForStatus(@RequestBody String parms) {
		log.info("Inside saveRecordForStatus start..!!");
		JSONObject requestObj = new JSONObject(parms);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(parms);
			if(resp!=null) {
				resp = leaseService.saveRecordForStatus(data);
				log.info("Inside saveRecordForStatus insert success..!!");
			}else {
				resp.put(CommonConstant.STATUS_KEY, 404);
			}
		    
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	
	@PostMapping("/case-files/insertPaymant")
	public ResponseEntity<?> insertPaymant(@RequestBody String parms) {
		log.info("Inside insertPaymant start..!!");
		JSONObject requestObj = new JSONObject(parms);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(parms);
			if(resp!=null) {
				resp = leaseService.insertPaymantRecord(data);
				log.info("Inside insertPaymant insert success..!!");
			}else {
				resp.put(CommonConstant.STATUS_KEY, 404);
			}
		    
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	
	@PostMapping("/case-files/getPaymantDetailsWithId")
	public ResponseEntity<?> getPaymantDetailsWithId(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LeaseCasePaymantEntity> districtList = leaseService.getPaymantDataWithId(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				resp.put("count", leaseService.getCountLeaseCasePayment(data));
				log.info("return controller of getPaymantDetailsWithId method success..!!");
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found..!!");
				log.info("getPaymantDetailsWithId  return no record found");
				resp.put("count", leaseService.getCountLeaseCasePayment(data));
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	
	@PostMapping("/case-files/getActionStatusHistory")
	public ResponseEntity<?> getActionStatusHistory(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LeaseCaseDTO> districtList = leaseService.getLeaseCaseStatus(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				log.info("return controller of getActionStatusHistory method success..!!");
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found..!!");
				log.info("getActionStatusHistory  return no record found");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

}
