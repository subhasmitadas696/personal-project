package com.csmtech.sjta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.KhatianInformationDTO;
import com.csmtech.sjta.dto.PlotInformationDTO;
import com.csmtech.sjta.dto.TahasilMasterDTO;
import com.csmtech.sjta.dto.TenderAdvertisementDTO;
import com.csmtech.sjta.dto.TenderAndAdvertizeDTO;
import com.csmtech.sjta.dto.VillageMasterDTO;
import com.csmtech.sjta.entity.DistrictMaster;
import com.csmtech.sjta.entity.KhatianInformation;
import com.csmtech.sjta.entity.PlotInformation;
import com.csmtech.sjta.entity.TahasilMaster;
import com.csmtech.sjta.entity.TenderAndAdvertizeEntity;
import com.csmtech.sjta.entity.TenderType;
import com.csmtech.sjta.entity.VillageMaster;
import com.csmtech.sjta.service.TenderAndAdvertizeService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/tender")

public class TenderAndAdvertizeContoller {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private TenderAndAdvertizeService tenderservice;

	@Value("${file.path}")
	private String filePathloc;

	private static final Logger log = LoggerFactory.getLogger(TenderAndAdvertizeContoller.class);

	String data = "";
	JSONObject resp = new JSONObject();

	@PostMapping("/getalltendertype")
	public ResponseEntity<Map<String, Object>> getAllTenderType() {
		Map<String, Object> response = new HashMap<>();
		List<TenderType> list = tenderservice.getAllTenderType();
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.DATA_FOUND);
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.NO_DATA_FOUND);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getalldistrict")
	public ResponseEntity<Map<String, Object>> getAllDistrict() {
		Map<String, Object> response = new HashMap<>();
		List<DistrictMaster> list = tenderservice.getAllDistrict();
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.DATA_FOUND);
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.NO_DATA_FOUND);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getalltehsil")
	public ResponseEntity<Map<String, Object>> getAllTehsil(@RequestBody TahasilMasterDTO tehsil) {
		Map<String, Object> response = new HashMap<>();
		List<TahasilMaster> list = tenderservice.getAllTehsil(tehsil.getTxtDistrictCode());
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.DATA_FOUND);
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.NO_DATA_FOUND);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getallvillage")
	public ResponseEntity<Map<String, Object>> getAllVillage(@RequestBody VillageMasterDTO village) {
		Map<String, Object> response = new HashMap<>();
		List<VillageMaster> list = tenderservice.getAllVillage(village.getTahasilCode());
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.DATA_FOUND);
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.NO_DATA_FOUND);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getallkhatian")
	public ResponseEntity<Map<String, Object>> getAllKhatian(@RequestBody KhatianInformationDTO khatian) {
		Map<String, Object> response = new HashMap<>();
		List<KhatianInformation> list = tenderservice.getAllKhatian(khatian.getVillageCode());
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.DATA_FOUND);
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.NO_DATA_FOUND);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getallplot")
	public ResponseEntity<Map<String, Object>> getAllPlot(@RequestBody PlotInformationDTO plot) {
		Map<String, Object> response = new HashMap<>();
		List<PlotInformation> list = tenderservice.getAllPlot(plot.getKhatianCode());
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.DATA_FOUND);
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.NO_DATA_FOUND);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/savetender")
	public ResponseEntity<?> saveRecord(@RequestBody String tenderData) {
		JSONObject jsb;

		jsb = tenderservice.saveRecord(tenderData);

		jsb.put(CommonConstant.STATUS_KEY, 200);
		jsb.put(CommonConstant.MESSAGE_KEY, "Data Saved Successfully..");
		return ResponseEntity.ok(jsb.toString());
	}

	@PostMapping("/gettender")
	public ResponseEntity<?> getTender(@RequestBody String formParams) throws JsonProcessingException {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			ObjectMapper om = new ObjectMapper();
			TenderAdvertisementDTO tenderdto = om.readValue(data, TenderAdvertisementDTO.class);
			List<TenderAdvertisementDTO> list = tenderservice.viewAllTender(tenderdto.getTitle(),
					tenderdto.getStartDate());
			if (list != null && !list.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.MESSAGE_KEY, CommonConstant.DATA_FOUND);
				resp.put(CommonConstant.RESULT, list);
			} else {
				resp.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
				resp.put(CommonConstant.MESSAGE_KEY, CommonConstant.NO_DATA_FOUND);
				resp.remove(CommonConstant.RESULT);
			}

		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/viewtender")
	public ResponseEntity<Map<String, Object>> viewTender(@RequestBody TenderAdvertisementDTO tenderdto) {
		Map<String, Object> response = new HashMap<>();
		Integer countRecord = tenderservice.getCount(tenderdto.getTitle());
		List<TenderAdvertisementDTO> list = tenderservice.getAllTender(tenderdto.getPageNumber(),
				tenderdto.getPageSize(), tenderdto.getTitle());
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.DATA_FOUND);
			response.put(CommonConstant.RESULT, list);
			response.put(CommonConstant.COUNT, countRecord);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, CommonConstant.NO_DATA_FOUND);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getonetender")
	public ResponseEntity<Map<String, Object>> getDetails(@RequestBody TenderAndAdvertizeDTO tenderDto,
			HttpServletRequest request) {
		log.info(":: inside getDetails() !!");
		Map<String, Object> response = new HashMap<>();
		try {
			TenderAndAdvertizeEntity tender = tenderservice
					.findByTenderAdvertisementId(tenderDto.getTenderAdvertisementId());

			if (tender != null) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "TenderAndAdvertize_Details found");
				response.put(CommonConstant.RESULT, tender);

			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
				response.put(CommonConstant.MESSAGE_KEY, "TenderAndAdvertize_Details not found");
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error occurred while getting details: {}", e.getMessage());
			response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
			response.put(CommonConstant.MESSAGE_KEY, "Invalid tenderId");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/deletetender")
	public ResponseEntity<Map<String, Object>> deleteTender(@RequestBody TenderAdvertisementDTO tenderDto) {
		log.info("delete started");
		Map<String, Object> response = new HashMap<>();
		try {
			if (tenderDto.getTenderAdvertisementId() != null) {
				log.info("delete operation started");
				tenderservice.deleteTender(tenderDto.getCreatedBy(), tenderDto.getTenderAdvertisementId());
				log.info("deletion completed");
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "Tender deleted");
			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
				response.put(CommonConstant.MESSAGE_KEY, "Tender not deleted");
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error occurred while getting details: {}", e.getMessage());

			response.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put(CommonConstant.MESSAGE_KEY, "Server Error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
