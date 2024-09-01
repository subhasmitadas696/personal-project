/**
 * @author prasanta.sethi
 */

package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.mobile.dto.PlotLandInspectionDto;
import com.csmtech.sjta.service.ViewAssignedDetailsService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

@RestController
@RequestMapping("/view-assigned-details")
public class ViewAssignedDetailsController {
	@Autowired
	ViewAssignedDetailsService viewAssignedDetailsService;
	JSONObject js = new JSONObject();
	String requestBodyData = null;

	@PostMapping("/assigned-details")
	public ResponseEntity<?> getAssignedDetails(@RequestBody String formParams) {
		JSONObject response = new JSONObject();
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			requestBodyData = CommonUtil.inputStreamDecoder(formParams);
			List<PlotLandInspectionDto> dtodata = viewAssignedDetailsService.getAssignedDetails();

			response.put(CommonConstant.RESULT, dtodata);
			response.put(CommonConstant.STATUS_KEY, 200);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}
}
