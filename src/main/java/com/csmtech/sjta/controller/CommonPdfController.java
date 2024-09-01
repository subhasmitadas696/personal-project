package com.csmtech.sjta.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.service.CommonPdfService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.google.common.net.HttpHeaders;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/mainView")
@Slf4j
public class CommonPdfController {

	@Autowired
	private CommonPdfService pdfService;

	JSONObject resp = new JSONObject();

	@PostMapping("/getRecordForPdfLandApp")
	public ResponseEntity<?> getRecordForWinner(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			String data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject js = new JSONObject(data);
			LandAppResponseStructureDTO getRecord = pdfService.gtePdfRecord(js.getBigInteger("intId"));
			if (getRecord != null) {
				resp.put("result", new JSONObject(getRecord));
				resp.put(CommonConstant.STATUS_KEY, 200);
			} else {
				resp.put("result", "No Record Found");
				resp.put(CommonConstant.STATUS_KEY, 404);
			}
		} else {
			resp.put("msg", "error");
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
//	@PostMapping(value = "/generatePdfTransaction", produces = MediaType.APPLICATION_PDF_VALUE)
//	public ResponseEntity<?> generateReport(@RequestBody String formParams) throws FileNotFoundException, JRException {
//		JSONObject json = new JSONObject(formParams);
//		byte[] filename = pdfService.getReportForTransaction("pdf", json.getBigInteger(CommonConstant.INTID));
//		return ResponseEntity.ok(filename);
//	}
	
	
	@GetMapping(value = "/getProcessingFeeReceipt/{landAppId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> getProcessingFeeReceipt(HttpServletResponse response,
			@PathVariable("landAppId") BigInteger landAppId) throws IOException, JRException {
		String fileName = "ProcessingFeeReceipt.pdf";
		ByteArrayInputStream getPaymntTranction = pdfService.getReportForTransaction("pdf", landAppId);
		InputStreamResource file = new InputStreamResource(getPaymntTranction);
		log.info("getProcessingFeeReceipt get data and return the pdf format ...!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_PDF)).body(file);
	}
	
	@GetMapping(value = "/getFormMFeeReceipt/{formMId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> getFormMFeeReceipt(HttpServletResponse response,
			@PathVariable("formMId") BigInteger formMId) throws IOException, JRException {
		String fileName = "ProcessingFeeReceipt.pdf";
		ByteArrayInputStream getPaymntTranction = pdfService.getReportForFormMTransaction("pdf", formMId);
		InputStreamResource file = new InputStreamResource(getPaymntTranction);
		log.info("getProcessingFeeReceipt get data and return the pdf format ...!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_PDF)).body(file);
	}
	
	@GetMapping(value = "/getFinalFeeReceipt/{payId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> getFinalFeeReceipt(HttpServletResponse response,
			@PathVariable("payId") BigInteger payId) throws IOException, JRException {
		String fileName = "ProcessingFeeReceipt.pdf";
		ByteArrayInputStream getPaymntTranction = pdfService.getReportForFinalTransaction("pdf", payId);
		InputStreamResource file = new InputStreamResource(getPaymntTranction);
		log.info("getProcessingFeeReceipt get data and return the pdf format ...!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_PDF)).body(file);
	}
}
