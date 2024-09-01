/**
 * @author prasanta.sethi
 */
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

import com.csmtech.sjta.service.LandAreaStatisticsService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")

@Slf4j
public class LandAreaStatisticsController {
	@Autowired
	private LandAreaStatisticsService landAreaStatisticsService;

	JSONObject response = new JSONObject();

	@PostMapping("/getSummaryDetails")
	public ResponseEntity<?> getStatisticsDetails(@RequestBody String formParams) {
		log.info("getlandstatistics started....!!");
		JSONObject requestObj = new JSONObject(formParams);

		try {

			if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
				if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
						requestObj.getString(CommonConstant.REQUEST_TOKEN))) {

					response.put(CommonConstant.STATUS_KEY, 200);
					response.put(CommonConstant.RESULT, new JSONObject(
							landAreaStatisticsService.fetchStatisticsInfo(CommonUtil.inputStreamDecoder(formParams))));

				} else {
					response.put(CommonConstant.STATUS_KEY, 401);
					response.put(CommonConstant.ERROR, "Token verification failed");
				}
			} else {
				response.put(CommonConstant.STATUS_KEY, 200);
				response.put(CommonConstant.RESULT,
						new JSONObject(landAreaStatisticsService.fetchStatisticsInfo(formParams)));
			}

		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.ERROR, "An error occurred while getSummaryDetails method.");
		}

		if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
		} else {
			return ResponseEntity.ok(response.toString());
		}

	}

	@PostMapping("/plotsDoc")
	public ResponseEntity<?> plotsDoc(@RequestBody String formParams) {
		try {
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put(CommonConstant.RESULT, landAreaStatisticsService.plotsDoc(formParams));

		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.ERROR, "An error occurred while plotsDoc method.");
		}

		return ResponseEntity.ok(response.toString());

	}

	@PostMapping("/plotInfoForTableView")
	public ResponseEntity<?> plotInfoForTableView(@RequestBody String formParams) {
		try {
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put(CommonConstant.RESULT, landAreaStatisticsService.plotInfoForTableView(formParams));

		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.ERROR, "An error occurred while plotInfoForTableView method.");
			response.put(CommonConstant.RESULT, "");
		}

		return ResponseEntity.ok(response.toString());

	}
	
	@PostMapping("/landPlotStatusDetails")
	public ResponseEntity<?> landApplicationDetails(@RequestBody String formParams) {
		try {

			JSONObject data = new JSONObject(formParams);
			String type = data.getString("type");
			
			response.put(CommonConstant.STATUS_KEY, 200);
			if(type.equals("APPLICATION")) {
				response.put(CommonConstant.RESULT, landAreaStatisticsService.landApplicationDetails(formParams));
			} else if(type.equals("MEETING")) {
				response.put(CommonConstant.RESULT, landAreaStatisticsService.landMeetingDetails(formParams));
			} else {
				response.put(CommonConstant.RESULT, "");
			}

		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.ERROR, "An error occurred while landApplicationDetails method.");
			response.put(CommonConstant.RESULT, "");
		}

		return ResponseEntity.ok(response.toString());
	}
	
	@PostMapping("/landMergeSplit")
	public ResponseEntity<?> landMergeSplit(@RequestBody String formParams) {
		try {

			JSONObject data = new JSONObject(formParams);
			String type = data.getString("type");
			
			if(type.equals("merge")) {
				landAreaStatisticsService.landMerge(data);
			} else if(type.equals("split")) {
				landAreaStatisticsService.landSplit(data);
			}
			
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put(CommonConstant.MESSAGE_KEY, "Success");

		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.ERROR, "An error occurred while landApplicationDetails method.");
			response.put(CommonConstant.RESULT, "");
		}

		return ResponseEntity.ok(response.toString());
	}
	
	@PostMapping("/getMergeSplitDetails")
	public ResponseEntity<?> getMergeSplitDetails(@RequestBody String formParams) {
		try {

			JSONObject data = new JSONObject(formParams);
			String type = data.getString("type");
			
			List<Map<String, Object>> result = landAreaStatisticsService.getMergeSplitDetails(type);
			
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put(CommonConstant.MESSAGE_KEY, "Success");

			response.put(CommonConstant.RESULT, result);

		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.ERROR, "An error occurred while landApplicationDetails method.");
			response.put(CommonConstant.RESULT, "");
		}

		return ResponseEntity.ok(response.toString());
	}
	
	@PostMapping("/updateRollBack")
	public ResponseEntity<?> updateRollBack(@RequestBody String formParams) {
		try {
			JSONObject data = new JSONObject(formParams);
			Integer status = landAreaStatisticsService.updateRollBack(data);
			
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put(CommonConstant.MESSAGE_KEY, "Success");

		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.ERROR, "An error occurred while landApplicationDetails method.");
			response.put(CommonConstant.RESULT, "");
		}

		return ResponseEntity.ok(response.toString());
	}
}
