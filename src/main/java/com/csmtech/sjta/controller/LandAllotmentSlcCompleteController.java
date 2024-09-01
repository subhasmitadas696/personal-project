package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.service.LandAllertmentSlcCompliteSrvive;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.LandAllotmentValidationnCheck;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/meetingAndLandAllotment")
public class LandAllotmentSlcCompleteController {

	@Autowired
	private LandAllertmentSlcCompliteSrvive service;

	JSONObject js = new JSONObject();
	String requestBodyData = null;

	@PostMapping("/allSlcCompletedRecord")
	public ResponseEntity<?> getSldCompliteREcord(@RequestBody String formParams) {
		log.info("inside the allSlcCompletedRecord execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT, service.getMettingCompliteSlcRecord(requestBodyData));
			js.put(CommonConstant.COUNT, service.countMeetingSchedulesByMeetingLevelId());
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allSlcCompletedRecord execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/allSlcCompletedRecordWithId")
	public ResponseEntity<?> getSldCompliteREcordWithId(@RequestBody String formParams) {
		log.info("inside the allSlcCompletedRecordWithId execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT, service.getDistinctPlotNumbers(requestBodyData));
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allSlcCompletedRecordWithId execute end");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/allSlcCompletedRecordWithAllDetails")
	public ResponseEntity<?> getSldCompliteREcordWithAllDetails(@RequestBody String formParams) {
		log.info("inside the allSlcCompletedRecordWithAllDetails execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT,
					service.executeCustomQueryGetLandAllertmentAlRequiredDetails(requestBodyData));
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allSlcCompletedRecordWithAllDetails execute end");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/allSlcCompletedSaveDetails")
	public ResponseEntity<?> getSldComplitedSaveDetails(@RequestBody String formParams) {
		log.info("inside the allSlcCompletedSaveDetails execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			String validationMsg = LandAllotmentValidationnCheck.BackendValidation(new JSONObject(requestBodyData));
			if (validationMsg != null) {
				js.put(CommonConstant.STATUS_KEY, 502);
				js.put("errMsg", validationMsg);
				log.warn("Inside create method of BidderregistraraController Validation Error");
			} else {
				js.put(CommonConstant.RESULT, service.landAllortmrntSaveRecord(requestBodyData));
				js.put(CommonConstant.STATUS_KEY, 200);
			}
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allSlcCompletedSaveDetails execute end");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/allLandAllertCompliteUsers")
	public ResponseEntity<?> allLandAllertCompliteUsers(@RequestBody String formParams) {
		log.info("inside the allLandAllertCompliteUsers execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT, service.getLandAllotmentDetails(requestBodyData));
			js.put(CommonConstant.COUNT, service.countLandAlertUser());
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allLandAllertCompliteUsers execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/removeLandAllotementUser")
	public ResponseEntity<?> removeLandAllotementUser(@RequestBody String formParams) {
		log.info("inside the removeLandAllotementUser execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			Integer count = service.updateLandAllotementFlag(requestBodyData);
			if (count > 0) {
				js.put(CommonConstant.RESULT, "Record Remove Success..!");
				js.put(CommonConstant.STATUS_KEY, 200);
			} else {
				js.put(CommonConstant.RESULT, "Record Not Remove Success..!");
				js.put(CommonConstant.STATUS_KEY, 404);
			}
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the removeLandAllotementUser execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/allSlcCompliteGoForAuction")
	public ResponseEntity<?> allSlcCompliteGoForAuction(@RequestBody String formParams) {
		log.info("inside the allSlcCompliteGoForAuction execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(requestBodyData);
			if (json.getString("selPlotNo") == null) {
				js.put(CommonConstant.STATUS_KEY, 502);
				js.put("errMsg", "Plot Code Can Not Blank");
				log.warn("Inside create method of BidderregistraraController Validation Error");
			} else {
				js.put(CommonConstant.RESULT, service.insertRecordForAuction(requestBodyData));
				js.put(CommonConstant.STATUS_KEY, 200);
			}
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allSlcCompliteGoForAuction execute end");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/allGoForAuctionPlotsRecord")
	public ResponseEntity<?> allGoForAuctionPlotsRecord(@RequestBody String formParams) {
		log.info("inside the allGoForAuctionPlotsRecord execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT, service.gteRecordGoForAuction(requestBodyData));
			js.put(CommonConstant.COUNT, service.getGoForAuctionCount());
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allGoForAuctionPlotsRecord execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/removeAllGoForAuctionUser")
	public ResponseEntity<?> removeAllGoForAuctionUser(@RequestBody String formParams) {
		log.info("inside the removeAllGoForAuctionUser execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			Integer count = service.updateGoForAucton(requestBodyData);
			if (count > 0) {
				js.put(CommonConstant.RESULT, "Record Remove Success..!");
				js.put(CommonConstant.STATUS_KEY, 200);
			} else {
				js.put(CommonConstant.RESULT, "Record Not Remove Success..!");
				js.put(CommonConstant.STATUS_KEY, 404);
			}
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the removeAllGoForAuctionUser execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/updateLandAllotementFrom16Record")
	public ResponseEntity<?> updateLandAllotementFrom16Record(@RequestBody String formParams) {
		log.info("inside the updateLandAllotementFrom16Record execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			Integer count = service.updateLandAllotementFrom16Record(requestBodyData);
			if (count > 0) {
				js.put(CommonConstant.RESULT, "Record Update Success..!");
				js.put(CommonConstant.STATUS_KEY, 200);
			} else {
				js.put(CommonConstant.RESULT, "Record Not Update .!");
				js.put(CommonConstant.STATUS_KEY, 404);
			}
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the updateLandAllotementFrom16Record execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}
	
	
	@PostMapping("/getLandAllotmentWnnerDetails")
	public ResponseEntity<?> getLandAllotmentWnnerDetails(@RequestBody String formParams) {
		log.info("inside the allGoForAuctionPlotsRecord execute start");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT, service.getLandAllotmentWnnerDetails(requestBodyData));
			js.put(CommonConstant.COUNT, service.getLandAllotmentWnnerDetailsCount(requestBodyData));
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allGoForAuctionPlotsRecord execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}
	
	
}
