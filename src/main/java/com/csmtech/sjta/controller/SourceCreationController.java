/**
 * 
 */
package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.AuctionPlotIdDetrailsMainDTO;
import com.csmtech.sjta.dto.SourcerCreationDto;
import com.csmtech.sjta.service.SourceCreationService;
import com.csmtech.sjta.util.AuctionPlotValidationCheck;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.MeetingScheduleValidationCheck;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abhijit.sahoo
 *
 */

@RestController
@RequestMapping("/auction")
@Slf4j
public class SourceCreationController {
	@Autowired
	private SourceCreationService sourceCreationService;

	JSONObject js = new JSONObject();

	JSONObject resp = new JSONObject();
	String data=null;

	@SuppressWarnings("unused")
	@PostMapping("/source-creation/addEdit")
	public ResponseEntity<?> create(@RequestBody SourcerCreationDto sourcerCreationDto) {
		if (AuctionPlotValidationCheck.BackendValidation(sourcerCreationDto) != null) {
			resp.put(CommonConstant.STATUS_KEY, 502);
			resp.put(CommonConstant.ERROR_MESSAGE, AuctionPlotValidationCheck.BackendValidation(sourcerCreationDto));
		} else {
			Integer status = sourceCreationService.save(sourcerCreationDto);
			if (status == 101001) {
				resp.put(CommonConstant.STATUS_KEY, 323);
				resp.put(CommonConstant.ERROR_MESSAGE, "Auction Plot Already Exist");
			} else if (status > 0) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put("successMsg", "Data Inserted / Updated Successfully.");
			}

			else {
				resp.put(CommonConstant.STATUS_KEY, 500);
				resp.put(CommonConstant.ERROR_MESSAGE, "Something went Wrong!");
			}
		}
		return ResponseEntity.ok(resp.toString());
	}

	@PostMapping("/getAuctionPlotDetails")
	public ResponseEntity<?> auctionPlotDetrails(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			JSONObject newjson = new JSONObject(CommonUtil.inputStreamDecoder(formParams));
			String plotNo = newjson.getString("plotNo");
			String auctionFlag = newjson.getString("auctionFlag");
			js.put(CommonConstant.RESULT,
					sourceCreationService.auctionPlotDetrails((CommonUtil.inputStreamDecoder(formParams))));

			if (!"".equals(auctionFlag) || !"".equals(plotNo)) {
				js.put(CommonConstant.COUNT,
						sourceCreationService.getCountAuctionRecordUseLike(CommonUtil.inputStreamDecoder(formParams)));
				js.put(CommonConstant.STATUS_KEY, 200);
			} else {
				js.put(CommonConstant.COUNT, sourceCreationService.getCountAuctionRecord());
				js.put(CommonConstant.STATUS_KEY, 200);
			}
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/getAuctionPlotIdDetails")
	public ResponseEntity<?> auctionPlotDetrailsId(@RequestBody String formParams) {
		JSONObject response = new JSONObject(formParams);
		JSONObject json = new JSONObject();
		AuctionPlotIdDetrailsMainDTO respones = sourceCreationService
				.auctionPlotIdDetrails(response.getBigInteger("intId"));
		if (respones == null) {
			json.put(CommonConstant.STATUS_KEY, 404);
			json.put(CommonConstant.RESULT, "No Record Found ..!!");
			return ResponseEntity.ok(json.toString());
		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.RESULT, new JSONObject(respones));
		return ResponseEntity.ok(json.toString());
	}

	@PostMapping("/getRemoveRecord")
	public ResponseEntity<?> getRemoveAuctionDetails(@RequestBody String formParams) {
		if (formParams != null && !formParams.isEmpty()) {
			JSONObject response = new JSONObject(formParams);
			JSONObject json = new JSONObject();
			if (response.has("intId")) {
				Integer getCountDelete = sourceCreationService
						.softDeleteAuctionDetails(response.getBigInteger("intId"));
				if (getCountDelete > 0) {
					json.put(CommonConstant.STATUS_KEY, 200);
					json.put(CommonConstant.RESULT, "Record Remove Success..!!");
					return ResponseEntity.ok(json.toString());
				}
			}
		}

		JSONObject json = new JSONObject();
		json.put(CommonConstant.STATUS_KEY, 404);
		json.put(CommonConstant.RESULT, "No Record Remove ..!!");
		return ResponseEntity.ok(json.toString());
	}

	@PostMapping("/addAuctionDocument")
	public ResponseEntity<?> addAuctionDocument(@RequestBody String formParams) {
		if (formParams != null && !formParams.isEmpty()) {
			JSONObject response = new JSONObject(formParams);
			JSONObject json = new JSONObject();
			if (response.has("plotId") && response.has("filename")) {
				Integer getCountUpdateRecord = sourceCreationService
						.updateAddAuctionDocument(response.getBigInteger("plotId"), response.getString("filename"));
				if (getCountUpdateRecord > 0) {
					json.put(CommonConstant.STATUS_KEY, 200);
					json.put(CommonConstant.RESULT, "Record Remove Success..!!");
					return ResponseEntity.ok(json.toString());
				}
			}
		}
		JSONObject json = new JSONObject();
		json.put(CommonConstant.STATUS_KEY, 404);
		json.put(CommonConstant.RESULT, "No Record Remove ..!!");
		return ResponseEntity.ok(json.toString());

	}

	@PostMapping("/getAuctionPlotDetailsSelectFlag")
	public ResponseEntity<?> auctionPlotDetrailsSelectAuctionFlag(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			js.put(CommonConstant.RESULT, sourceCreationService
					.auctionPlotDetrailsSelectAuctionFlag((CommonUtil.inputStreamDecoder(formParams))));
			js.put(CommonConstant.COUNT, sourceCreationService.countForAuctionApprovalRecord((CommonUtil.inputStreamDecoder(formParams))));
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}

	@PostMapping("/apprvalSection")
	public ResponseEntity<?> updateGrievanceStatus(@RequestBody String formParams) {
		String approvalid = null;
		Integer takeAction = null;
		JSONObject jsonobj = new JSONObject(formParams);
		Integer gid = jsonobj.getInt("intId");
		String actionRemerk = jsonobj.getString("actionRemarks");
		approvalid = jsonobj.getString("approvalAction");
		takeAction = sourceCreationService.updateDeputyOfficerAction(gid, approvalid, actionRemerk);
		String returnstatus = "";
		if (approvalid.equals("A")) {
			returnstatus = "Approved Successfully!";
		} else if (approvalid.equals("R")) {
			returnstatus = "Rejected Successfully!";
		}
		if (takeAction > 0) {
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put("message", returnstatus);
			resp.put(CommonConstant.RESULT, "Update Record Success..!!");
			return ResponseEntity.ok(resp.toString());
		} else {
			resp.put(CommonConstant.STATUS_KEY, 404);
			resp.put(CommonConstant.RESULT, "Record Not Update (Please Check )");
			return ResponseEntity.ok(resp.toString());
		}
	}

	@PostMapping("/allPlotRecordGoForauction")
	public ResponseEntity<?> allPlotRecordGoForauction(@RequestBody String formParams) {
		JSONObject js = new JSONObject();
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			String data = CommonUtil.inputStreamDecoder(formParams);
			js.put(CommonConstant.RESULT, sourceCreationService.getSelectedPlotsInfo(data));
			js.put(CommonConstant.COUNT, sourceCreationService.getSelectedPlotsInfoCount());
			js.put(CommonConstant.STATUS_KEY, 200);
		} else {
			js.put("msg", CommonConstant.ERROR);
			js.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("inside the allPlotRecordGoForauction execute and return respones");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(js.toString()).toString());
	}
	
	
	@PostMapping("/saveSourceSecondProcess")
	public ResponseEntity<?> saveSourceSecondProcess(@RequestBody String meeting_schedule) {
		log.info("Inside create method of saveSourceSecondProcess");
		JSONObject requestObj = new JSONObject(meeting_schedule);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(meeting_schedule);
				resp = sourceCreationService.saveSourceSecondProcess(data);
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

}
