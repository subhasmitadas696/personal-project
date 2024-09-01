package com.csmtech.sjta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.csmtech.sjta.dto.AddOfficerDTO;
import com.csmtech.sjta.dto.LandPaginationDTO;
import com.csmtech.sjta.dto.MeetingPaginationDTO;
import com.csmtech.sjta.dto.MeetingPlotsRecordDTO;
import com.csmtech.sjta.dto.PaginationInRegisterDtoResponse;
import com.csmtech.sjta.service.LandApplicantService;
import com.csmtech.sjta.service.MeetingScheduleService;
import com.csmtech.sjta.service.OfficerRegistrationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.MeetingScheduleValidationCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/meeting-management")
public class MeetingScheduleController {

	/**
	 * @author guru.prasad and @author rashmi.jena
	 */

	@Autowired
	private MeetingScheduleService meeting_scheduleService;
	@Autowired
	private OfficerRegistrationService officerRegisterService;
	@Autowired
	private LandApplicantService landApplicantService;

	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(MeetingScheduleController.class);

	JSONObject resp = new JSONObject();
	@Value("${file.path}")
	private String finalUploadPath;

	@PostMapping("/meeting-schedule/addEdit")
	public ResponseEntity<?> create(@RequestBody String meeting_schedule) {
		logger.info("Inside create method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(meeting_schedule);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(meeting_schedule);
			String validationMsg = MeetingScheduleValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put("status", 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of Meeting_scheduleController Validation Error");
			} else {
				resp = meeting_scheduleService.save(data);
				if (resp == null) {
					return null;
				}
			}
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/meeting-schedule/addFile")
	public ResponseEntity<?> createFile(@RequestBody String meeting_schedule) {
		logger.info("Inside file update method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(meeting_schedule);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(meeting_schedule);
			resp = meeting_scheduleService.updateFile(data);

		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/meeting-schedule/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = meeting_scheduleService.getById(json.getInt("intId"), json.getInt("auctionFlag"));
			resp.put("status", 200);
			resp.put("result", entity);
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/meeting-schedule/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			resp = meeting_scheduleService.deleteById(json.getInt("intId"), json.getInt("intUpdatedBy"),json.getInt("auctionFlag"));
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping("/meeting/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		logger.info("Inside download method of Meeting_scheduleController");
		File file = new File(finalUploadPath + "/" + "meeting/" + name);
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

	@GetMapping(path = "/viewDocument/{appDocId}", name = "View File")
	public ResponseEntity viewDocument(HttpServletResponse response, @PathVariable("appDocId") String fileName)
			throws FileNotFoundException {

		HttpHeaders headers = new HttpHeaders();
		headers.add("content-disposition", "inline;filename=" + fileName);
		File file = new File(finalUploadPath + "/" + "meeting/" + fileName);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		String contentType = "";

		if (null != fileName && fileName.contains(".")) {
			String fileExtension = fileName.split("\\.")[1];

			if (fileExtension.equalsIgnoreCase("pdf"))
				contentType = "application/pdf";
			if (fileExtension.equalsIgnoreCase("xls"))
				contentType = "application/vnd.ms-excel";
			if (fileExtension.equalsIgnoreCase("xlsx"))
				contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			else if (fileExtension.equalsIgnoreCase("png"))
				contentType = "image/png";
			else if (fileExtension.equalsIgnoreCase("jpeg"))
				contentType = "image/jpeg";
			else if (fileExtension.equalsIgnoreCase("jpg"))
				contentType = "image/jpg";
			else if (fileExtension.equalsIgnoreCase("csv"))
				response.setContentType("text/csv");
			else if (fileExtension.equalsIgnoreCase("zip"))
				response.setContentType("application/zip");
		}
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	@PostMapping("/getallofficer")
	public ResponseEntity<Map<String, Object>> viewAllOfficer() {
		Map<String, Object> response = new HashMap<>();

		try {
			List<AddOfficerDTO> details = officerRegisterService.getOfficerDetails();

			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, "Data retrieved successfully");
			response.put("result", details);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put(CommonConstant.MESSAGE_KEY, "An error occurred while processing the request");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/getland")
	public ResponseEntity<?> getData(@RequestBody PaginationInRegisterDtoResponse res) {
		JSONObject response = new JSONObject();
		LandPaginationDTO getdtodata = landApplicantService.getLandApplicantDetailsPage(res.getPlot_code(),
				res.getMeetingLevleId(), res.getAuctionFlag());
		if (getdtodata == null) {
			logger.info(":: getPaginationSearchData() execution Sucess No Record Found..!!");

			response.put("status", 404);
			response.put("result", "No Record Found");
			return ResponseEntity.ok(response.toString());
		} else
			logger.info(":: getPaginationSearchData() execution Success ..!!");
		response.put(CommonConstant.STATUS_KEY, 200);
		response.put(CommonConstant.MESSAGE_KEY, "Land Applicant Details Found");
		response.put("result", getdtodata);
		return ResponseEntity.ok(getdtodata);
	}

	@PostMapping("/validateApplicant")
	public ResponseEntity<?> validateApplicant(@RequestBody String meeting_schedule) {
		logger.info("Inside create method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(meeting_schedule);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(meeting_schedule);
			resp = meeting_scheduleService.validate(data);
			if (resp == null) {
				return null;
			}
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/viewall")
	public ResponseEntity<?> getPaginationSearchData(@RequestBody String formparam) throws JsonProcessingException {
		JSONObject requestObj = new JSONObject(formparam);
		try {
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formparam);
				ObjectMapper objectMapper = new ObjectMapper();
				PaginationInRegisterDtoResponse res = objectMapper.readValue(data,
						PaginationInRegisterDtoResponse.class);

				MeetingPaginationDTO getdtodata = meeting_scheduleService.viewAll(res.getPageNumber(),
						res.getPageSize(), res.getApplicationNo(), res.getKhataNo(), res.getMeetingDate(),
						res.getMeetingIid());

				if (getdtodata == null) {
					resp.put(CommonConstant.RESULT, "");
					resp.put("msg", "data not found");
					resp.put(CommonConstant.STATUS_KEY, 404);
				} else {
					resp.put("successmsg", "execution Success");
					resp.put(CommonConstant.STATUS_KEY, 200);
					resp.put(CommonConstant.RESULT, new JSONObject(new ObjectMapper().writeValueAsString(getdtodata)));
					logger.info(":: getPaginationSearchData() in view meeting schedule controller executed !!!");
				}
			}
		} catch (Exception e) {
			resp.put(CommonConstant.RESULT, "");
			resp.put("errmsg", "failed");
			resp.put(CommonConstant.STATUS_KEY, 500);
			logger.error(e.getMessage());
			logger.info("Inside Exception in getPaginationSearchData() !!" + e.getMessage());
		}
		logger.info("getPaginationSearchData returned successfully !! ");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/printPdf")
	public ResponseEntity<?> printPdf(@RequestBody String meeting_schedule) {
		logger.info("Inside create method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(meeting_schedule);

		JSONObject resp = new JSONObject();

		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(meeting_schedule);
			resp = meeting_scheduleService.printPdf(data);
			if (resp == null) {
				resp = new JSONObject();
				resp.put("msg", "error");
				resp.put("status", 404);
			}
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/sendMailSms")
	public ResponseEntity<?> sendMailSms(@RequestBody String meeting_schedule) {
		logger.info("Inside create method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(meeting_schedule);
		JSONObject resp = new JSONObject();

		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(meeting_schedule);
			resp = meeting_scheduleService.sendMail(data);
			if (resp == null) {
				resp = new JSONObject();

				resp.put("msg", "error");
				resp.put("status", 404);
			}
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/meeting-schedule/getMeetingInPlotsWise")
	public ResponseEntity<?> getMeetingInPlotsWise(@RequestBody String formParams) {
		logger.info("Inside getById method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			resp.put("status", 200);
			resp.put("result", meeting_scheduleService.getLandApplicationData(json.getBigInteger("intId"),
					json.getInt("auctionFlag"))); // ,json.getBigInteger("meetingMainId")
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("meeting-schedule/uplodeMom")
	public ResponseEntity<?> addAuctionDocument(@RequestBody String formParams)
			throws JsonMappingException, JsonProcessingException {
		JSONObject js = new JSONObject();
		Integer result = meeting_scheduleService.saveRecordForMeetingUplodeMom(formParams);
		if (result > 0) {
			js.put("status", 200);
			js.put("result", "Record Save Success..!!");
		} else {
			js.put(CommonConstant.STATUS_KEY, 404);
			js.put(CommonConstant.RESULT, "No Record Remove ..!!");
		}
		return ResponseEntity.ok(js.toString().toString());

	}

	@PostMapping("/getlandForAfterMeetings")
	public ResponseEntity<?> getlandForAfterMeetings(@RequestBody PaginationInRegisterDtoResponse res) {
		JSONObject response = new JSONObject();
		LandPaginationDTO getdtodata = landApplicantService.getlandForAfterMeetings(res.getMeetingLevleIdRe(),
				res.getMeetingIdRe(), res.getMeetingSheduleIdRe(), res.getAuctionFlagCount());
		if (getdtodata == null) {
			logger.info(":: getPaginationSearchData() execution Sucess No Record Found..!!");

			response.put("status", 404);
			response.put("result", "No Record Found");
			return ResponseEntity.ok(response.toString());
		} else
			logger.info(":: getPaginationSearchData() execution Success ..!!");
		response.put(CommonConstant.STATUS_KEY, 200);
		response.put(CommonConstant.MESSAGE_KEY, "Land Applicant Details Found");
		response.put("result", getdtodata);
		return ResponseEntity.ok(getdtodata);
	}

	@PostMapping("/getAllPlotsRecord")
	public ResponseEntity<?> getAllPlotsRecord(@RequestBody String formParams) {
		JSONObject response = new JSONObject();
		data = CommonUtil.inputStreamDecoder(formParams);
		List<MeetingPlotsRecordDTO> getdtodata = meeting_scheduleService.getAllplotsRecord(data);
		if (getdtodata == null) {
			logger.info(":: getAllPlotsRecord() execution Sucess No Record Found..!!");
			response.put("status", 404);
			response.put("result", "No Record Found");
			return ResponseEntity.ok(response.toString());
		} else
			logger.info(":: getAllPlotsRecord() execution Success ..!!");
		response.put(CommonConstant.STATUS_KEY, 200);
		response.put(CommonConstant.MESSAGE_KEY, "plot Details Found");
		response.put("result", getdtodata);
		response.put("count", meeting_scheduleService.getRowCountOfDistinctPlotMeetings());
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/meeting-schedule/history")
	public ResponseEntity<?> getHistory(@RequestBody String formParams) {
		logger.info("Inside getById method of Meeting_scheduleController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			resp.put("status", 200);
			resp.put("result", meeting_scheduleService.getHistory(json.getBigInteger("meetingId")));
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

}