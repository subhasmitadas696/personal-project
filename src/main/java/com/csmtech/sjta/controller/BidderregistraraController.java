package com.csmtech.sjta.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.AuctionDTO;
import com.csmtech.sjta.dto.AuctionDetails;
import com.csmtech.sjta.dto.BidderRegistratatonViewMoreDTO;
import com.csmtech.sjta.dto.FromMApplicationViewTenderWise;
import com.csmtech.sjta.dto.TenderViewOfficerDTO;
import com.csmtech.sjta.dto.VIewCitizenAuctionPlotDetailsDTO;
import com.csmtech.sjta.service.BidderregistraraService;
import com.csmtech.sjta.service.BiddingUserService;
import com.csmtech.sjta.util.BidderRegistraraValidationCheck;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/auction")
public class BidderregistraraController {

	@Autowired
	private BidderregistraraService bidderregistraraService;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(BidderregistraraController.class);
	JSONObject resp = new JSONObject();
	@Value("${file.path}")
	private String finalUploadPath;

	@Autowired
	private BiddingUserService biddingUserService;

	@PostMapping("/bidderregistraration/addEdit")
	public ResponseEntity<?> create(@RequestBody String bidderregistrara) {
		logger.info("Inside create method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(bidderregistrara);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(bidderregistrara);
			String validationMsg = BidderRegistraraValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of BidderregistraraController Validation Error");
			} else {
				resp = bidderregistraraService.save(data);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = bidderregistraraService.getById(json.getBigInteger(CommonConstant.INTID));
			Map<String, Object> getTenderAuctionDates = bidderregistraraService
					.getTenderAuctionDates(json.getBigInteger(CommonConstant.INTID));
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put("StartDate", getTenderAuctionDates.get("form_m_submit_start_date"));
			resp.put("endDate", getTenderAuctionDates.get("form_m_submit_end_date"));
			resp.put(CommonConstant.RESULT, entity);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside all method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = bidderregistraraService.getAll(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			resp = bidderregistraraService.deleteById(json.getBigInteger(CommonConstant.INTID));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping("/bidderregistraration/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		logger.info("Inside download method of BidderregistraraController");
		File file = new File(finalUploadPath + "bidderregistraration/" + name);
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
		return ResponseEntity.ok().headers(headers(name)).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(byteArrayResource);
	}

	private HttpHeaders headers(String name) {
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return header;
	}

	@PostMapping("/bidderregistraration/preview/native")
	public ResponseEntity<?> getByIdNative(@RequestBody String formParams) {
		logger.info("Inside previewnative method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			List<BidderRegistratatonViewMoreDTO> entity = bidderregistraraService
					.getByIdNative(json.getBigInteger(CommonConstant.INTID));

			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put(CommonConstant.RESULT, entity);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/all/citigrn")
	public ResponseEntity<?> getAllCitogen(@RequestBody String formParams) {
		logger.info("Inside allcitigrn method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			String inputdata = CommonUtil.inputStreamDecoder(formParams);
			JSONObject js = new JSONObject(inputdata);
			List<VIewCitizenAuctionPlotDetailsDTO> result = bidderregistraraService
					.getAuctionPlotData(js.getInt("pageNo"), js.getInt("size"),js.getBigInteger("userId"));
			BigInteger count = bidderregistraraService.getTotalApplicantCount(js.getBigInteger("userId"));
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put(CommonConstant.RESULT, result);
			resp.put(CommonConstant.COUNT, count);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/paymentUpdate")
	public ResponseEntity<?> updatePaymentRecord(@RequestBody String formParams) {
		JSONObject json = new JSONObject(formParams);
		JSONObject jsonobj = new JSONObject();
		Integer result = bidderregistraraService.tranctionCount(json.getBigInteger(CommonConstant.INTID),
				json.getString("razorpayOrderId"), json.getString("razorpaySignature"),
				json.getString("razorpayPaymentId"), json.getBigDecimal("paymentAmount"));

		if (result > 0) {
			String updateBidderFormMApplication = bidderregistraraService
					.updateBidderFormMApplication(json.getInt(CommonConstant.INTID));
			logger.info("Update Applicant");
			jsonobj.put(CommonConstant.STATUS_KEY, 200);
			jsonobj.put(CommonConstant.MESSAGE_KEY, "Success");
			jsonobj.put("uniqueNumber", updateBidderFormMApplication);
		} else {
			jsonobj.put(CommonConstant.STATUS_KEY, 404);
			jsonobj.put(CommonConstant.MESSAGE_KEY, "Not Inserted Payment In Db");
		}
		return ResponseEntity.ok(jsonobj.toString());
	}

	@PostMapping("/bidderregistraration/allTenderRecord")
	public ResponseEntity<?> getAllTenderOfficer(@RequestBody String formParams) {
		logger.info("Inside allTenderRecord method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			List<TenderViewOfficerDTO> record = bidderregistraraService
					.getTenderOfficerRecord(CommonUtil.inputStreamDecoder(formParams));
			BigInteger getTenderCount = bidderregistraraService.getTenderCount();
			if (!record.isEmpty()) {
				resp.put(CommonConstant.COUNT, getTenderCount);
				resp.put(CommonConstant.RESULT, record);
				resp.put(CommonConstant.STATUS_KEY, 200);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/allFromMapplicantIdRecord")
	public ResponseEntity<?> getAllFromAppOfficer(@RequestBody String formParams) {
		logger.info("Inside allFromMapplicantIdRecord method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			List<FromMApplicationViewTenderWise> record = bidderregistraraService
					.getBidderFormApplicationData(CommonUtil.inputStreamDecoder(formParams));
			String data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject js = new JSONObject(data);

			BigInteger getTenderCount = bidderregistraraService.getTenderCountApplicant(js.getBigInteger(CommonConstant.INTID));
			if (!record.isEmpty()) {
				resp.put(CommonConstant.COUNT, getTenderCount);
				resp.put(CommonConstant.RESULT, record);
				resp.put(CommonConstant.STATUS_KEY, 200);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/approvalApplicant")
	public ResponseEntity<?> updateApprovalProcess(@RequestBody String formParams) {
		logger.info("Inside approvalApplicant method of BidderregistraraController");
		Map<String, Object> reMap = new HashMap<>();
		Integer count = bidderregistraraService.updateBidderFormApplication(formParams);
		JSONObject js = new JSONObject(formParams);
		String messageReturn = null;
		if ("A".equalsIgnoreCase(js.getString("status"))) {
			messageReturn = "Form M Application Approved Successfully";
		} else if ("R".equalsIgnoreCase(js.getString("status"))) {
			messageReturn = "Form M Application Rejected Successfully";
		} else {
			messageReturn = "";
		}
		if (count.equals(42)) {
			reMap.put(CommonConstant.MESSAGE_KEY, "time expired");
			reMap.put(CommonConstant.STATUS_KEY, 405);
		} else if (count > 0) {
			reMap.put(CommonConstant.MESSAGE_KEY, messageReturn);
			reMap.put(CommonConstant.STATUS_KEY, 200);
		} else {
			reMap.put(CommonConstant.RESULT, "record Not Updated ..!!");
			reMap.put(CommonConstant.STATUS_KEY, 400);
		}
		return ResponseEntity.ok(reMap);
	}

	@PostMapping("/bidderregistraration/alltenderBiddingRecord")
	public ResponseEntity<?> getAllTenderAuctionData(@RequestBody String formParams) throws JsonProcessingException {
		logger.info("Inside alltenderBiddingRecord method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<AuctionDTO> respones = bidderregistraraService.getAuctions(data);
			if (!respones.isEmpty()) {
				resp.put(CommonConstant.RESULT, respones);
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.COUNT, bidderregistraraService.getAuctionsCount(data));
			} else {
				resp.put(CommonConstant.RESULT, "");
				resp.put("msg", "no Record Found");
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.COUNT, 0);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/previewAuctionDetailsLive")
	public ResponseEntity<?> getByIdAutionDtails(@RequestBody String formParams) {
		logger.info("Inside getById method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			Boolean check = bidderregistraraService.checkAuctionStatusValid(json.getBigInteger(CommonConstant.INTID));
			if (Boolean.TRUE.equals(check)) {
				List<AuctionDetails> entity = bidderregistraraService.getAuctionDetails(json.getBigInteger(CommonConstant.INTID));
				if (!entity.isEmpty()) {
					resp.put(CommonConstant.STATUS_KEY, 200);
					resp.put(CommonConstant.RESULT, entity);
				} else {
					resp.put(CommonConstant.STATUS_KEY, 404);
					resp.put(CommonConstant.RESULT, "No Record Found");
				}
			} else {
				resp.put(CommonConstant.STATUS_KEY, 432);
				resp.put(CommonConstant.RESULT, "Auction Time Expired..!");
			}

		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/checkUserApprovesOrNot")
	public ResponseEntity<?> checkUserApprovesOrNot(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			Boolean checkUserApproval = biddingUserService.checkApprovalStatus(json.getBigInteger("tenderId"),
					json.getBigInteger(CommonConstant.INTID));
			if (Boolean.TRUE.equals(checkUserApproval)) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, "Approval Success.");
			} else {
				resp.put(CommonConstant.STATUS_KEY, 432);
				resp.put(CommonConstant.RESULT, "Approval Not Done Yet ..!!");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/bidderregistraration/allEvaMApplication")
	public ResponseEntity<?> getAllFroMApplication(@RequestBody String formParams) {
		logger.info("Inside allEvaMApplication method of BidderregistraraController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = bidderregistraraService.getAllDataFromMEvalucation(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/bidderregistraration/getddhar")
	public CheckAddhar getAllFroMApplication(@RequestBody CheckAddhar data) {
		CheckAddhar cgech = new CheckAddhar();
		if (data.getAdharNo().equalsIgnoreCase("518202995855")) {
			cgech.setAdharNo("518202995855");
		} else {
			cgech.setAdharNo("123456789101");
		}
		return cgech;

	}
}

class CheckAddhar {
	private String adharNo;

	public String getAdharNo() {
		return adharNo;
	}

	public void setAdharNo(String adharNo) {
		this.adharNo = adharNo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private String key;
}