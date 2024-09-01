package com.csmtech.sjta.controller;

import java.math.BigInteger;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.ShowWinerDTO;
import com.csmtech.sjta.dto.WinnerFinalResponesDTO;
import com.csmtech.sjta.service.BidderWinerService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/winner")
public class BidderWinerController {

	@Autowired
	private BidderWinerService service;

	JSONObject resp = new JSONObject();

	@PostMapping("/winnerDeclare/getallRecord")
	public ResponseEntity<?> getAllFromAppOfficer(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			String data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject js = new JSONObject(data);
			List<ShowWinerDTO> record1 = service.getCustomResults(js.getInt("pageNo"), js.getInt("size"));
			BigInteger getTenderCount = service.getCountOfLiveAuctions();
			if (!record1.isEmpty()) {
				resp.put("count", getTenderCount);
				resp.put(CommonConstant.RESULT, record1);
				resp.put(CommonConstant.STATUS_KEY, 200);
			}else {
				resp.put(CommonConstant.RESULT, "No Record Found");
				resp.put(CommonConstant.STATUS_KEY, 404);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/winnerDeclare/getRecordForWinner")
	public ResponseEntity<?> getRecordForWinner(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			String data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject js = new JSONObject(data);
			WinnerFinalResponesDTO getRecord = service.getWinerFinalRecordData(js.getBigInteger("intId"));
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonResult;
			try {
				jsonResult = objectMapper.writeValueAsString(getRecord);
			} catch (Exception e) {
				return null;
			}
			if (getRecord != null && !isNoRecordFound(getRecord)) {
				resp.put(CommonConstant.RESULT, new JSONObject(jsonResult));
				resp.put(CommonConstant.STATUS_KEY, 200);
			} else {
				resp.put(CommonConstant.RESULT, "No Record Found");
				resp.put(CommonConstant.STATUS_KEY, 404);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/winnerDeclare/getWinnerUplodeDocs")
	public ResponseEntity<?> getWinnerUplodeDocs(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			String data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject js = new JSONObject(data);
			Integer respones = service.updateWinnerDocument(js.getBigInteger("intId"), js.getString("docsName"),data);
			if (respones > 0) {
				resp.put(CommonConstant.RESULT, "Record Update Success.");
				resp.put(CommonConstant.STATUS_KEY, 200);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	private boolean isNoRecordFound(WinnerFinalResponesDTO dto) {
		return dto.getLiveAuctionId() == null && dto.getAuctionNumberGen() == null && dto.getAuctionName() == null
				&& dto.getRoyality() == null && dto.getWinnerName() == null && dto.getHighestBidPrice() == null
				&& dto.getGetMultiRecord().isEmpty();
	}
}
