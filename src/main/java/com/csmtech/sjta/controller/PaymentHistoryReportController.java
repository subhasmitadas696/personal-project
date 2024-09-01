package com.csmtech.sjta.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.service.PaymentHistoryService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.google.common.net.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/reports/paymenthistory")
@Slf4j
public class PaymentHistoryReportController {

	@Autowired
	private PaymentHistoryService payHistoryService;

	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/districtwisepaymenthistory")
	public ResponseEntity<?> paymentHistory(@RequestBody String formParams) {

		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				List<Map<String, Object>> report = payHistoryService.fetchPaymentHistory(data);

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT, report);
				log.info("paymenthistoryreport success");

			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (Exception e) {
			log.error("Error occurred in paymenthistory: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put(CommonConstant.MESSAGE_KEY, "error occured in payment history report");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/tahasilwisepaymenthistory")
	public ResponseEntity<?> fetchPaymentHistoryReportOfTahasil(@RequestBody String formParams) {

		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				List<Map<String, Object>> report = payHistoryService.fetchPaymentHistoryReportOfTahasil(data);

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT, report);
				log.info("fetchPaymentHistoryReportOfTahasil success");

			} else {
				resp.put("msg", "error");
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (Exception e) {
			log.error("Error occurred in fetchPaymentHistoryReportOfTahasil: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put("message", "error occured in fetchPaymentHistoryReportOfTahasil report");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/villagewisepaymenthistory")
	public ResponseEntity<?> fetchPaymentHistoryReportOfVillage(@RequestBody String formParams) {

		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				List<Map<String, Object>> report = payHistoryService.fetchPaymentHistoryReportOfVillage(data);

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT, report);
				log.info("fetchPaymentHistoryReportOfVillage success");

			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (Exception e) {
			log.error("Error occurred in fetchPaymentHistoryReportOfVillage: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put(CommonConstant.MESSAGE_KEY, "error occured in fetchPaymentHistoryReportOfVillage report");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/landallotmentdistrictwisepaymenthistory")
	public ResponseEntity<?> landAllotmentpaymentHistory(@RequestBody String formParams) {

		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				List<Map<String, Object>> report = payHistoryService.fetchLandAllotmentPaymentHistory(data);

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT, report);
				log.info("landAllotmentpaymentHistory success");

			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (Exception e) {
			log.error("Error occurred in landAllotmentpaymentHistory: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put(CommonConstant.MESSAGE_KEY, "error occured in land allotment payment history report");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/landallotmenttahasilwisepaymenthistory")
	public ResponseEntity<?> landAllotmentpaymentHistoryOfTahasil(@RequestBody String formParams) {

		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				List<Map<String, Object>> report = payHistoryService.fetchLandAllotmentPaymentHistoryOfTahasil(data);

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT, report);
				log.info("landAllotmentpaymentHistoryOfTahasil success");

			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (Exception e) {
			log.error("Error occurred in landAllotmentpaymentHistoryOfTahasil: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put(CommonConstant.MESSAGE_KEY, "error occured in land allotment tahasil payment history report");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/landallotmentvillagewisepaymenthistory")
	public ResponseEntity<?> landAllotmentpaymentHistoryOfVillage(@RequestBody String formParams) {

		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				List<Map<String, Object>> report = payHistoryService.fetchLandAllotmentPaymentHistoryOfVillage(data);

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT, report);
				log.info("landAllotmentpaymentHistoryOfVillage success");

			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (Exception e) {
			log.error("Error occurred in landAllotmentpaymentHistoryOfVillage: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put(CommonConstant.MESSAGE_KEY, "error occured in land allotment village payment history report");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/landallotmentkhatianwisepaymenthistory")
	public ResponseEntity<?> landAllotmentpaymentHistoryOfKhatian(@RequestBody String formParams) {

		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				List<Map<String, Object>> report = payHistoryService.fetchLandAllotmentPaymentHistoryOfKhatian(data);

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT, report);
				log.info("landAllotmentpaymentHistoryOfKhatian success");

			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (Exception e) {
			log.error("Error occurred in landAllotmentpaymentHistoryOfKhatian: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put(CommonConstant.MESSAGE_KEY, "error occured in land allotment khatian payment history report");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@PostMapping("/landallotmentplotwisepaymenthistory")
	public ResponseEntity<?> landAllotmentpaymentHistoryOfPlot(@RequestBody String formParams) {

		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				List<Map<String, Object>> report = payHistoryService.fetchLandAllotmentPaymentHistoryOfPlot(data);

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT, report);
				log.info("landAllotmentpaymentHistoryOfPlot success");

			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (Exception e) {
			log.error("Error occurred in landAllotmentpaymentHistoryOfPlot: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put(CommonConstant.MESSAGE_KEY, "error occured in land allotment plot payment history report");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

	@GetMapping(value = "/getDistPaymentExcel/{fiscalYear}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generatePaymentCollectionReport(HttpServletResponse response, @PathVariable("fiscalYear") String fiscalYear) throws IOException {
		String fileName = "PaymentCollectionReport.xlsx";
		ByteArrayInputStream paymentCollectionStream = payHistoryService.fetchPaymentHistoryExcel(fiscalYear);
		InputStreamResource file = new InputStreamResource(paymentCollectionStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/getTahasilPaymentExcel/{districtCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateTahasilPaymentCollectionReport(HttpServletResponse response,
			@PathVariable("districtCode") String districtCode) throws IOException {
		String fileName = "PaymentCollectionReport.xlsx";
		ByteArrayInputStream paymentCollectionStream = payHistoryService.fetchTahasilPaymentHistoryExcel(districtCode);
		InputStreamResource file = new InputStreamResource(paymentCollectionStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/getVillagePaymentExcel/{tahasilCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateVillagePaymentCollectionReport(HttpServletResponse response,
			@PathVariable("tahasilCode") String tahasilCode) throws IOException {
		String fileName = "PaymentCollectionReport.xlsx";
		ByteArrayInputStream paymentCollectionStream = payHistoryService.fetchVillagePaymentHistoryExcel(tahasilCode);
		InputStreamResource file = new InputStreamResource(paymentCollectionStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/getDistLandAllotmentPaymentExcel/{fiscalYear}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateDistLandAllotmentPaymentCollectionReport(HttpServletResponse response, @PathVariable("fiscalYear") String fiscalYear) throws IOException {
		String fileName = "LandAllotmentPaymentCollectionReport.xlsx";
		ByteArrayInputStream paymentCollectionStream = payHistoryService.fetchDistLandAllotmentPaymentHistoryExcel(fiscalYear);
		InputStreamResource file = new InputStreamResource(paymentCollectionStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/getTahasilLandAllotmentPaymentExcel/{districtCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateTahasilLandAllotmentPaymentCollectionReport(HttpServletResponse response,
			@PathVariable("districtCode") String districtCode) throws IOException {
		String fileName = "LandAllotmentPaymentCollectionReport.xlsx";
		ByteArrayInputStream paymentCollectionStream = payHistoryService
				.fetchTahasilLandAllotmentPaymentHistoryExcel(districtCode);
		InputStreamResource file = new InputStreamResource(paymentCollectionStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/getVillageLandAllotmentPaymentExcel/{tahasilCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateVillageLandAllotmentPaymentCollectionReport(HttpServletResponse response,
			@PathVariable("tahasilCode") String tahasilCode) throws IOException {
		String fileName = "LandAllotmentPaymentCollectionReport.xlsx";
		ByteArrayInputStream paymentCollectionStream = payHistoryService
				.fetchVillageLandAllotmentPaymentHistoryExcel(tahasilCode);
		InputStreamResource file = new InputStreamResource(paymentCollectionStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/getKhatianLandAllotmentPaymentExcel/{villageCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateKhatianLandAllotmentPaymentCollectionReport(HttpServletResponse response,
			@PathVariable("villageCode") String villageCode) throws IOException {
		String fileName = "LandAllotmentPaymentCollectionReport.xlsx";
		ByteArrayInputStream paymentCollectionStream = payHistoryService
				.fetchKhatianLandAllotmentPaymentHistoryExcel(villageCode);
		InputStreamResource file = new InputStreamResource(paymentCollectionStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/getPlotLandAllotmentPaymentExcel/{khatianCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generatePlotLandAllotmentPaymentCollectionReport(HttpServletResponse response,
			@PathVariable("khatianCode") String khatianCode) throws IOException {
		String fileName = "LandAllotmentPaymentCollectionReport.xlsx";
		ByteArrayInputStream paymentCollectionStream = payHistoryService
				.fetchPlotLandAllotmentPaymentHistoryExcel(khatianCode);
		InputStreamResource file = new InputStreamResource(paymentCollectionStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}
}
