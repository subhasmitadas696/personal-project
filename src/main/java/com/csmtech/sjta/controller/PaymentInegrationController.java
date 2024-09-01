package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.TranctionDetails;
import com.csmtech.sjta.service.PaymentInegrationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mianPayment")
@Slf4j
public class PaymentInegrationController {

	@Autowired
	private PaymentInegrationService service;

	@PostMapping("/createPayment")
	public ResponseEntity<?> createPaymentTranction(@RequestBody String formsParms) {
		JSONObject json = new JSONObject(formsParms);
		// json.getString("distId")
		TranctionDetails tr = service.createTranction(json.getInt("amount"));
		if (tr != null) {
			log.info(" :: return Success..!!");
			return ResponseEntity.ok(tr);
		}
		log.info(" :: No Traction Happen..!!");
		json.put("status", 404);
		json.put("result", "No Traction Happen.. ");
		return ResponseEntity.ok(json.toString());
	}

}
