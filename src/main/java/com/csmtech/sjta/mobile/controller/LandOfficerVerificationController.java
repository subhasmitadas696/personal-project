package com.csmtech.sjta.mobile.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
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

import com.csmtech.sjta.dto.LandApplicantDTO;
import com.csmtech.sjta.dto.PlotValuationDTO;
import com.csmtech.sjta.mobile.dto.LandVerificationResponseDto;
import com.csmtech.sjta.mobile.dto.PlotLandInspectionDto;
import com.csmtech.sjta.mobile.entity.LandUseEntity;
import com.csmtech.sjta.mobile.service.LandOfficerVerificationService;
import com.csmtech.sjta.service.PlotValuationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/landOfficerVerification")
@Slf4j
public class LandOfficerVerificationController {

	@Autowired
	private LandOfficerVerificationService landVerificationService;

	@Autowired
	private PlotValuationService plotValuationService;

	@Value("${file.path}")
	private String finalUploadPath;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/assignToCO")
	public ResponseEntity assignToCO(@RequestBody PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response;
		try {
			log.info("assigning the land verification to CO");
			response = landVerificationService.assignToCO(landDto);
		} catch (Exception e) {
			log.error("error while assigning evaluation of land to CO: " + e.getMessage());
			return new ResponseEntity("error while assigning evaluation of land to CO",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/assignToCONr")
	public ResponseEntity assignToCONr(@RequestBody PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response;
		try {
			log.info("assigning the land verification to CO");
			response = landVerificationService.assignToCONr(landDto);
		} catch (Exception e) {
			log.error("error while assigning evaluation of land to CO: " + e.getMessage());
			return new ResponseEntity("error while assigning evaluation of land to CO",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/coInspectionSubmit")
	public ResponseEntity coSubmit(@RequestBody PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response;
		try {
			log.info("saving CO Inspection data process starts");
			response = landVerificationService.coSubmitInspection(landDto);
		} catch (Exception e) {
			log.error("error while saving CO inspection data" + e.getMessage());
			return new ResponseEntity("error while saving CO inspection data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchDetails")
	public ResponseEntity fetchDetailsById(@RequestBody LandApplicantDTO landDto) {
		JSONObject response = new JSONObject();
		try {
			log.info("fetching data process starts");
			response = landVerificationService.fetchDetailsById(landDto);
			log.info("response for api: " + response);
		} catch (Exception e) {
			log.error("error while fetching data: " + e.getMessage());
			return new ResponseEntity("error while fetching data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(response.toString());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchCount")
	public ResponseEntity fetchCount(@RequestBody LandApplicantDTO landDto) {
		log.info("process for fetching count of different land application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.fetchPendingAndCompleteCount();
		} catch (Exception e) {

			log.error("Fetching record failed.Some error occured in fetching count  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPendingRecordsByVillage")
	public ResponseEntity fetchPendingRecordsByVillage(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of pending land application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.fetchPendingRecordsByVillage(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPendingRecords")
	public ResponseEntity fetchPendingRecords(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of pending land application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.fetchPendingRecords();
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchCompleteRecords")
	public ResponseEntity fetchCompleteRecords(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of complete land application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.fetchCompleteRecords();
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/viewCoInspectionDetails")
	public ResponseEntity viewInspectionDetails(@RequestBody String formParams) {
		log.info("process for fetching details of CO land Inspection application");
		JSONObject requestObj = new JSONObject(formParams);

		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			ObjectMapper om = new ObjectMapper();
			PlotLandInspectionDto landDto;
			if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
				landDto = om.readValue(CommonUtil.inputStreamDecoder(formParams), PlotLandInspectionDto.class);
			} else {
				landDto = om.readValue(formParams, PlotLandInspectionDto.class);
			}
			response = landVerificationService.viewInspectionDetails(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
			return new ResponseEntity(CommonUtil.inputStreamEncoder(new JSONObject(response).toString()).toString(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity(response, HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/viewTahasildarInspectionDetails")
	public ResponseEntity viewTahasildarInspectionDetails(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of Tahasildar land Inspection application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.viewTahasildarInspectionDetails(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/viewWebTahasildarInspectionDetails")
	public ResponseEntity viewTahasildarInspectionDetailsWeb(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of web Tahasildar land Inspection application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.viewTahasildarInspectionDetailsWeb(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	// new api
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/getVillageInformation")
	public ResponseEntity getVillageInformation(@RequestBody PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.getVillageInformation(landDto);
		} catch (Exception e) {
			log.error("error occured while fetching village details: " + e.getMessage());
			response.setMessage("error occured while fetching village details");
			response.setStatus(500);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/tahasildarSubmit")
	public ResponseEntity tahasilSubmit(@RequestBody PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		log.info("checking the input for tahasildar saving action of plot inspection");
		if (landDto == null || (landDto.getTahasildarInspectedBy() == null && landDto.getTahasilRemarks() == null)) {
			return new ResponseEntity("Data required is not present", HttpStatus.BAD_REQUEST);
		}
		try {
			response = landVerificationService.saveTahasildarPlotAction(landDto);
		} catch (Exception e) {
			log.error("error occured while saving details of tahasildar plot survey: " + e.getMessage());

			response.setMessage("error occured while saving details of tahasildar plot survey");
			response.setStatus(500);

			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchTahasilPendingRecordsByVillage")
	public ResponseEntity fetchTahasilPendingRecordsByVillage(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of pending land application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.fetchTahasilPendingRecordsByVillage(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchTahasildarPendingRecords")
	public ResponseEntity fetchTahasildarPendingRecords(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of tahasildar pending land application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.fetchTahasildarPendingRecords(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchTahasildarCompleteRecords")
	public ResponseEntity fetchTahasildarCompleteRecords(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of tahasildar complete land application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.fetchTahasildarCompleteRecords(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/getVillageInformationForTahasil")
	public ResponseEntity getVillageInformationForTahasil(@RequestBody PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.getVillageInformationForTahasil(landDto);
		} catch (Exception e) {
			log.error("error occured while fetching village details for tahasil: " + e.getMessage());
			response.setMessage("error occured while fetching village details for tahasil");
			response.setStatus(500);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchCountForTahasil")
	public ResponseEntity fetchCountForTahasil(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching count of different land application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.fetchTahasilPendingAndCompleteCount(landDto);
		} catch (Exception e) {

			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/viewTahasildarValuationForm")
	public ResponseEntity viewTahasildarValuationForm(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of Tahasildar land Inspection application");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.viewTahasildarValuationForm(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/viewvaluationformbyplotcode")
	public ResponseEntity viewValuationFormByPlotCode(@RequestBody PlotLandInspectionDto landDto) {
		log.info("process for fetching details of Tahasildar land Inspection application by plotCode");
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			response = landVerificationService.viewTahasildarValuationFormByplotCode(landDto);
		} catch (Exception e) {
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage(CommonConstant.ERROR_MSG);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/plotvaluation")
	public ResponseEntity plotValuation(@RequestBody PlotValuationDTO landDto) {
		LandVerificationResponseDto response;
		try {
			log.info("inside plot valuation");
			response = plotValuationService.landPlotValuation(landDto);
		} catch (Exception e) {
			log.error("error while plot valuation : " + e.getMessage());
			return new ResponseEntity("error while plot valuation", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}


	@GetMapping("/plotvaluation/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		log.info("Inside download method of plotvaluation");
		File file = new File(finalUploadPath + "/" + "plotvaluation/" + name);
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

}
