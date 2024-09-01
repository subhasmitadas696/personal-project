package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.AuctionPriviewDTO;
import com.csmtech.sjta.service.CommonPdfService;
import com.csmtech.sjta.service.Tender_auctionService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.TenderAuctionValidationCheck;

@RestController
@RequestMapping("/auction")
public class TenderAuctionController {
	@Autowired
	private Tender_auctionService tenderAuctionService;

	@Autowired
	private CommonPdfService pdfService;

	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(TenderAuctionController.class);
	JSONObject resp = new JSONObject();

	@PostMapping("/tender-auction/addEdit")
	public ResponseEntity<?> create(@RequestBody String tenderAuction) {
		logger.info("Inside create method of Tender_auctionController");
		JSONObject requestObj = new JSONObject(tenderAuction);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(tenderAuction);
			String validationMsg = TenderAuctionValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of Tender_auctionController Validation Error");
			} else {
				resp = tenderAuctionService.save(data);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/tender-auction/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of Tender_auctionController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			List<AuctionPriviewDTO> entity = tenderAuctionService.getById(json.getBigInteger("intId"));

			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put("result", entity);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/tender-auction/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of Tender_auctionController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = tenderAuctionService.getPriviewTenderRecord(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/tender-auction/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of Tender_auctionController to execute ");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			resp = tenderAuctionService.deleteById(json.getBigInteger("intId"),json.getBigInteger("auctionPlotDetailsId"));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	private HttpHeaders headers(String name) {
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return header;
	}

	@PostMapping("/tender-auction/delete/publisStatus")
	public ResponseEntity<?> deletePublisStatus(@RequestBody String formParams) {
		logger.info("Inside delete method of Tender_auctionController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			Integer count = tenderAuctionService.getPublicApproval(json.getString("intId"),
					json.getBigInteger("createdBy"));
			if (count > 0) {
				resp.put("msg", "success");
				resp.put(CommonConstant.STATUS_KEY, 200);
			}else {
				resp.put("msg", "success");
				resp.put(CommonConstant.STATUS_KEY, 201);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping(value = "/generatePdfTendreAuction")
	public ResponseEntity<?> exportReportForTenderAuction(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);

		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			resp = pdfService.exportReportForTenderAuction(data);
			if (resp == null) {
				resp = new JSONObject();
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 404);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/tender-auction/delete/UnpublisStatus")
	public ResponseEntity<?> deleteUnPublisStatus(@RequestBody String formParams) {
		logger.info("Inside delete method of Tender_auctionController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			Integer count = tenderAuctionService.getUnPublicApproval(json.getBigInteger("intId"));
			if (count == 99) {
				resp.put("msg", "Auction Live Expired");
				resp.put(CommonConstant.STATUS_KEY, 406);
			} else if (count > 0) {
				resp.put("msg", "success");
				resp.put(CommonConstant.STATUS_KEY, 200);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
}