package com.csmtech.sjta.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
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
import com.csmtech.sjta.dto.LesaeCaseReportDTO;
import com.csmtech.sjta.service.LesaeCaseReportService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/leaseCaseReport")
@RestController
@Slf4j
public class LesaeCaseReportController {

	@Autowired
	private LesaeCaseReportService leaseCaseService;
	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/lease-information/getDistrictWiseRecord")
	public ResponseEntity<?> getDistrictRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LesaeCaseReportDTO> districtList = leaseCaseService.getDistrictLeaseReport(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				resp.put(CommonConstant.COUNT, leaseCaseService.getDistrictWiseLeaseCount());
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("getDistrictWiseRecord method return success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping(value = "/lease-information/leaseCaseDistrictExcel", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateDistrictReport() throws IOException {
		String fileName = "DistrictWiseLeaseCaseRecord.xlsx";
		ByteArrayInputStream districtStream = leaseCaseService.exportgetDistrictLeaseReport();
		InputStreamResource file = new InputStreamResource(districtStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@PostMapping("/lease-information/getTahasilWiseRecord")
	public ResponseEntity<?> getTahasilRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LesaeCaseReportDTO> tahasilList = leaseCaseService.getTahasilLeaseReport(data);
			if (!tahasilList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, tahasilList);
				resp.put(CommonConstant.COUNT, leaseCaseService.getTahasilWiseLeaseCount(data));
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("getTahasilRecord method return success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping(value = "/lease-information/leaseCaseTahailExcel/{districtCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateDistrictReport(HttpServletResponse response,
			@PathVariable("districtCode") String districtCode) throws IOException {
		String fileName = "TahasilWiseLeaseCaseRecord.xlsx";
		ByteArrayInputStream districtStream = leaseCaseService.exportgetTahasilLeaseReport(districtCode);
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("generateDistrictReport method return success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@PostMapping("/lease-information/getVillageWiseRecord")
	public ResponseEntity<?> getVillageWiseRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LesaeCaseReportDTO> VillageList = leaseCaseService.getVillageLeaseReport(data);
			if (!VillageList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, VillageList);
				resp.put(CommonConstant.COUNT, leaseCaseService.getVillageWiseLeaseCount(data));
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("getVillageWiseRecord method return success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping(value = "/lease-information/leaseCaseVillageExcel/{tahasilCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generatevillageReport(HttpServletResponse response,
			@PathVariable("tahasilCode") String tahasilCode) throws IOException {
		String fileName = "VillageWiseLeaseCaseRecord.xlsx";
		ByteArrayInputStream districtStream = leaseCaseService.exportgetVillageReport(tahasilCode);
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("generatevillageReport method return success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@PostMapping("/lease-information/getKhataWiseRecord")
	public ResponseEntity<?> getKhataWiseRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LesaeCaseReportDTO> VillageList = leaseCaseService.getKhataLeaseReport(data);
			if (!VillageList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, VillageList);
				resp.put(CommonConstant.COUNT, leaseCaseService.getKhataWiseLeaseCount(data));
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("getKhataWiseRecord method return success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/lease-information/getPlotWiseRecord")
	public ResponseEntity<?> getPlotWiseRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LesaeCaseReportDTO> VillageList = leaseCaseService.getPlotLeaseReport(data);
			if (!VillageList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, VillageList);
				resp.put(CommonConstant.COUNT, leaseCaseService.getPlotWiseLeaseCount(data));
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("getPlotWiseRecord method return success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping(value = "/lease-information/generateKhataReport/{villageCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateKhataReport(HttpServletResponse response,
			@PathVariable("villageCode") String villageCode) throws IOException {
		String fileName = "KhataWiseLeaseCaseRecord.xlsx";
		ByteArrayInputStream districtStream = leaseCaseService.exportgetKhataReport(villageCode);
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("generateKhataReport method return success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/lease-information/leaseCasePlotExcel/{khataCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generatePlotReport(HttpServletResponse response,
			@PathVariable("khataCode") String khataCode) throws IOException {
		String fileName = "PlotWiseLeaseCaseRecord.xlsx";
		ByteArrayInputStream districtStream = leaseCaseService.exportgetPlotReport(khataCode);
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("generatePlotReport method return success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

}
