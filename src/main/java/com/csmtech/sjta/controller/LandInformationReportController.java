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

import com.csmtech.sjta.dto.LandInformationReportDTO;
import com.csmtech.sjta.service.LandInformationReportService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.google.common.net.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/reports")
@Slf4j
public class LandInformationReportController {

	@Autowired
	private LandInformationReportService reportService;

	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/land-information/getDistrictRecord")
	public ResponseEntity<?> getDistrictRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandInformationReportDTO> districtList = reportService.getDistrictReportData(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				resp.put(CommonConstant.COUNT, reportService.getCountForDistrictReportData());
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("LandInformationReportController of  getDistrictRecord get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/land-information/getTahasilRecord")
	public ResponseEntity<?> getTahasilRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandInformationReportDTO> tahasilList = reportService.getTahasilReportData(data);
			List<LandInformationReportDTO> reverseRecord = reportService.ReverseRecordForDistrict(data);
			if (!tahasilList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, tahasilList);
				resp.put(CommonConstant.COUNT, reportService.getCountForTahasilReportData(data));
				resp.put(CommonConstant.REVERSE_RECORD, reverseRecord);
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found Tahasil");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("LandInformationReportController of  getTahasilRecord get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/land-information/getVillageRecord")
	public ResponseEntity<?> getVillageRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandInformationReportDTO> villageList = reportService.getVillageReportData(data);
			List<LandInformationReportDTO> reverseRecord = reportService.ReverseRecordForTahasil(data);
			if (!villageList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, villageList);
				resp.put(CommonConstant.COUNT, reportService.getCountForVillageReportData(data));
				resp.put(CommonConstant.REVERSE_RECORD, reverseRecord);
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, CommonConstant.NO_RECORD_VILLAGE_FOUND);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("LandInformationReportController of  getVillageRecord get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/land-information/getKataRecord")
	public ResponseEntity<?> getKataRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandInformationReportDTO> khataList = reportService.getKhataReportData(data);
			List<LandInformationReportDTO> reverseRecord = reportService.ReverseRecordForVillage(data);
			if (!khataList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, khataList);
				resp.put(CommonConstant.COUNT, reportService.getCountForKahtaReportData(data));
				resp.put(CommonConstant.REVERSE_RECORD, reverseRecord);
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, CommonConstant.NO_RECORD_VILLAGE_FOUND);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("LandInformationReportController of  getKataRecord get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/land-information/getPlotRecord")
	public ResponseEntity<?> getPlotRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandInformationReportDTO> plotList = reportService.getPlotReportData(data);
			List<LandInformationReportDTO> reverseRecord = reportService.ReverseRecordForKhata(data);
			if (!plotList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, plotList);
				resp.put(CommonConstant.COUNT, reportService.getCountForPlotReportData(data));
				resp.put(CommonConstant.REVERSE_RECORD, reverseRecord);
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, CommonConstant.NO_RECORD_VILLAGE_FOUND);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("LandInformationReportController of  getPlotRecord get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping(value = "/land-information/generateDistrictExcel", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateDistrictReport() throws IOException {
		String fileName = "DistrictRecord.xlsx";
		ByteArrayInputStream districtStream = reportService.exportReportToDistrict();
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("LandInformationReportController of  generateDistrictReport get exucation success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/land-information/generateTahasilWithDistrictIdReport/{districtCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateTahasilWithDistrictIdReport(HttpServletResponse response,
			@PathVariable("districtCode") String districtCode) throws IOException {
		String fileName = "TahasilRecord.xlsx";
		ByteArrayInputStream districtStream = reportService.exportReportToTahasil(districtCode);
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("LandInformationReportController of  generateTahasilWithDistrictIdReport get exucation success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/land-information/generateVillageWithTahasilIdReport/{tahasilCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateVillageWithTahasilIdReport(HttpServletResponse response,
			@PathVariable("tahasilCode") String tahasilCode) throws IOException {
		String fileName = "VillageRecord.xlsx";
		ByteArrayInputStream districtStream = reportService.exportReportToVillage(tahasilCode);
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("LandInformationReportController of  generateVillageWithTahasilIdReport get exucation success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/land-information/generateKhataWithVillageIdReport/{villageCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateKhataWithVillageIdReport(HttpServletResponse response,
			@PathVariable("villageCode") String villageCode) throws IOException {
		String fileName = "KhataRecord.xlsx";
		ByteArrayInputStream districtStream = reportService.exportReportToKhata(villageCode);
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("LandInformationReportController of  generateKhataWithVillageIdReport get exucation success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

	@GetMapping(value = "/land-information/generatePlotWithKhataIdReport/{khataCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generatePlotWithKhataIdReport(HttpServletResponse response,
			@PathVariable("khataCode") String khataCode) throws IOException {
		String fileName = "PlotRecord.xlsx";
		ByteArrayInputStream districtStream = reportService.exportReportToPlot(khataCode);
		InputStreamResource file = new InputStreamResource(districtStream);
		log.info("LandInformationReportController of  generatePlotWithKhataIdReport get exucation success..!!");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

}
