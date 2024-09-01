package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.service.FaqService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/faq")

@Slf4j
public class FaqController {
	JSONObject response = new JSONObject();

	@Autowired
	private FaqService faqService;

	@PostMapping("/questionsandanswers")
	public ResponseEntity<?> getAllQuestionsAndAnswers(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN)))

		{
			log.info("inside getAllQuestionsAndAnswers in controller !!");
			log.info(":: inside FaqController !!");
			response.put("status", 200);
			response.put("result", faqService.getAllQuestionsAndAnswers());
			log.info(":: faq executed successfully !!");
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}
}