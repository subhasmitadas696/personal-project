package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.csmtech.sjta.service.LandAllotedPaymentUserService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/landPaymentUser")
@Slf4j
public class LandAllotedPaymentUserController {

	@Autowired
	private LandAllotedPaymentUserService service;

	JSONObject js = new JSONObject();

	String requestBodyData = null;

	@PostMapping("/landAllotePaymentDetails")
	public ResponseEntity<?> getlandAllotePaymentDetails(@RequestBody String formParams) {
		log.info("inside the landAllotePaymentDetails execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT, service.getLandAllotementDetails(requestBodyData));
			js.put(CommonConstant.MULTI_RESULT, service.getPayments(requestBodyData));
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the landAllotePaymentDetails execute end");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/paymentUpdate")
	public ResponseEntity<?> updatePaymentRecord(@RequestBody String formParams) {
		log.info("inside the updatePaymentRecord execute start");
		JSONObject json = new JSONObject(formParams);
		JSONObject jsonobj = new JSONObject();
		Integer result = service.tranctionCount(json.getString("razorpayOrderId"), json.getString("razorpaySignature"),
				json.getString("razorpayPaymentId"), json.getBigDecimal("paymentAmount"), json.getBigInteger("userId"),
				json.getBigInteger("landAllotementId"));
		if (result > 0) {
			jsonobj.put(CommonConstant.STATUS_KEY, 200);
			jsonobj.put("message", "Success");
		} else {
			jsonobj.put(CommonConstant.STATUS_KEY, 404);
			jsonobj.put("message", "Not Inserted Payment In Db");
		}
		log.info("inside the updatePaymentRecord execute end");
		return ResponseEntity.ok(jsonobj.toString());
	}

	@PostMapping("/getCompliteUsersOfficerViewUserList")
	public ResponseEntity<?> allLandAllertCompliteUsers(@RequestBody String formParams) {
		log.info("inside the allLandAllertCompliteUsers execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT, service.getLandAllotementDetailsOffcer(requestBodyData));
			js.put(CommonConstant.COUNT, service.countLandAllortUser());
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allLandAllertCompliteUsers execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/uplodeDocForRegisterForm")
	public ResponseEntity<?> getWinnerUplodeDocs(@RequestBody String formParams) {
		log.info("inside the getWinnerUplodeDocs execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			Integer respones = service.updateWinnerDocument(requestBodyData);
			if (respones > 0) {
				js.put(CommonConstant.RESULT, "Record Update Success.");
				js.put(CommonConstant.STATUS_KEY, 200);
			}
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the updatePaymentRecord execute end");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

}
