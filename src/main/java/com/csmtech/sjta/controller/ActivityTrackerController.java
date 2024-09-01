
package com.csmtech.sjta.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.service.ActivityTrackerService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

@RestController
@RequestMapping("/activityTracker")
public class ActivityTrackerController {

	@Autowired
	private ActivityTrackerService activityTrackerService;

	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody String formParams) {
		Map<String, Object> response = new HashMap<>();
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			activityTrackerService.save(CommonUtil.inputStreamDecoder(formParams));

			response.put(CommonConstant.STATUS_KEY, 200);
			response.put(CommonConstant.MESSAGE_KEY, "Query Inserted Successfully !");
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.UNAUTHORIZED.value());
			response.put(CommonConstant.MESSAGE_KEY, "Token verification failed");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}
}
