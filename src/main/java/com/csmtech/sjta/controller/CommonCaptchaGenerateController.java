package com.csmtech.sjta.controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.util.CommonCaptchaGenerate;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/commonCaptchaGenerator")
@Slf4j
public class CommonCaptchaGenerateController {

	@PostMapping("/generate")
	public ResponseEntity<String> generateCaptcha() throws NoSuchAlgorithmException {
		log.info("Execute  generateCaptcha() Method ..!!");
		SecureRandom rand = SecureRandom.getInstanceStrong();
		JSONObject response = new JSONObject();
		try {
			Integer number1 = rand.nextInt(9) + 1;
			Integer number2 = rand.nextInt(9) + 1;
			String operator = getRandomOperator();
			Integer result = performOperation(number1, number2, operator);
			String captchaText = number1 + " " + operator + " " + number2 + " = ?";

			String captchaId = UUID.randomUUID().toString();
			CommonCaptchaGenerate.put(captchaId, result);
			response.put("id", captchaId);
			response.put("text", captchaText);
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			log.info("successfully generate captcha");

		} catch (Exception e) {
			log.error("error occured while generating captcha:", e.getMessage());
			response.put(CommonConstant.STATUS_KEY, 500);
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	private String getRandomOperator() throws NoSuchAlgorithmException {
		String[] operators = { "+", "*" };
		SecureRandom rand = SecureRandom.getInstanceStrong();

		int randomIndex = rand.nextInt(operators.length);
		return operators[randomIndex];
	}

	private int performOperation(int number1, int number2, String operator) {
		switch (operator) {
		case "+":
			return number1 + number2;
		case "*":
			return number1 * number2;

		default:
			throw new IllegalArgumentException("Invalid operator: " + operator);
		}
	}

}
