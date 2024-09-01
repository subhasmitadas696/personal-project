package com.csmtech.sjta.controller;

/**
 * @author prasanta.sethi
 */

import java.util.List;
import java.util.Objects;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.QueryDetailsDTO;
import com.csmtech.sjta.dto.ViewQueryPaginationDTO;
import com.csmtech.sjta.service.QueryService;
import com.csmtech.sjta.util.CommonCaptchaGenerate;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController

@Slf4j
public class QueryController {
	private final QueryService queryService;

	JSONObject response = new JSONObject();
	String data = "";

	public QueryController(QueryService queryService) {
		this.queryService = queryService;
	}

	@PostMapping("/raisequery")
	public ResponseEntity<String> insertQuery(@RequestBody String formParams) throws JsonProcessingException {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			ObjectMapper om = new ObjectMapper();
			QueryDetailsDTO queryDetails = om.readValue(data, QueryDetailsDTO.class);
			boolean validateCaptcha;
			Integer captchaResult = CommonCaptchaGenerate.get(queryDetails.getCaptchaId());

			if (captchaResult != null && Objects.equals(queryDetails.getAnswer(), captchaResult)) {
				CommonCaptchaGenerate.remove(queryDetails.getCaptchaId());
				validateCaptcha = true;
			} else {
				validateCaptcha = false;
			}

			if (!validateCaptcha) {
				response.put(CommonConstant.STATUS_KEY, 223);
				response.put("message", "Invalid captcha");
				return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
			}
			queryService.insertQuery(queryDetails.getQueryId(), queryDetails.getName(), queryDetails.getMobileno(),
					queryDetails.getQuery());
			log.info("insertQuery.. Executed Successfully");
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put("message", "Query Inserted Successfully !");
		} else {
			response.put("msg", "error");
			response.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}

	@PostMapping("/viewquery")
	public ResponseEntity<ViewQueryPaginationDTO> getQueryDetailsBySearch(@RequestBody search res) {
		if (res.getSearchKeyword() != null && !res.getSearchKeyword().isEmpty()) {
			Integer countint = queryService.getTotalQueryCount(res.getSearchKeyword());

			List<QueryDetailsDTO> queryDetailsList = queryService.getQueryDetailsById(res.getSearchKeyword(),
					res.getPageNumber(), res.getPageSize());
			log.info("queryDetailsList i.e queries found successfully !! ");
			return ResponseEntity.ok(new ViewQueryPaginationDTO(queryDetailsList, countint));

		} else {
			Integer countint = queryService.getTotalQueryCountAll();

			List<QueryDetailsDTO> queryDetailsList = queryService.getQueryDetailsSearch(res.getPageNumber(),
					res.getPageSize());
			return ResponseEntity.ok(new ViewQueryPaginationDTO(queryDetailsList, countint));

		}

	}

}
