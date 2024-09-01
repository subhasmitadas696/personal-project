package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.DistrictDTO;
import com.csmtech.sjta.dto.KhatianPlotDTO;
import com.csmtech.sjta.dto.TahasilDTO;
import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.dto.VillageInfoDTO;
import com.csmtech.sjta.service.TahasilTeamUseService;
import com.csmtech.sjta.util.CommonCaptchaGenerate;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.VillageRecordRespones;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mainRecord")

@Slf4j
public class TahasilTeamUseController {

	@Autowired
	private TahasilTeamUseService service;

	String data = "";

	@Value("${sjta.bcryptpassword.secretKey}")
	private String SECRET_KEY;

	@GetMapping("/distDropDownData")
	public ResponseEntity<?> getDistricts() {
		List<DistrictDTO> respones = service.getDistricts();
		if (respones.isEmpty()) {
			log.info(":: No record return in distDropDownData method");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 404);
			jsb.put(CommonConstant.RESULT, "No record..!!");
			return ResponseEntity.ok(jsb.toString());
		} else
			log.info("Execute and return the result");
		return ResponseEntity.ok(respones);
	}

	@PostMapping("/getThasilRecord")
	public ResponseEntity<?> getRecord(@RequestBody String formsParms) {
		JSONObject json = new JSONObject(formsParms);
		List<TahasilDTO> respones = service.getTahasilData(json.getString("tahasilCode"));
		if (respones.isEmpty()) {
			log.info(":: No record in getThasilRecord");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 404);
			jsb.put(CommonConstant.RESULT, "No record found..!!");
			return ResponseEntity.ok(jsb.toString());
		}
		log.info("Sucess..Execute and return the result");
		JSONObject jsb = new JSONObject();
		jsb.put(CommonConstant.STATUS_KEY, 200);
		jsb.put(CommonConstant.RESULT, respones);
		return ResponseEntity.ok(jsb.toString());
	}

	@PostMapping("/tahasilLogin")
	public ResponseEntity<?> checkLoginSpark(@RequestBody TahasilTeamUseRequestDto dto) {
		String password = dto.getPassword();
		String passpasswordPass;
		// ----------------Captcha Validate Start------------------//

		Boolean validateCaptcha;
		Integer captchaResult = CommonCaptchaGenerate.get(dto.getCaptchaId());
		if (captchaResult != null && dto.getAnswer().equals(captchaResult)) {
			CommonCaptchaGenerate.remove(dto.getCaptchaId());
			validateCaptcha = true;
		} else {
			validateCaptcha = false;
		}

		if (!validateCaptcha) {
			JSONObject response = new JSONObject();
			response.put(CommonConstant.STATUS_KEY, 223);
			response.put(CommonConstant.RESULT, "Invalid captcha");
			return ResponseEntity.ok(response.toString());
		}
		List<TahasilTeamUseRequestDto> respones = service.checkLogin(dto.getTahasilCode());
		if (respones.isEmpty()) {
			log.info("Fail Execute");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 404);
			jsb.put(CommonConstant.RESULT, "Fail ");
			return ResponseEntity.ok(jsb.toString());
		}

		if (!respones.get(0).getTahasilCode().equalsIgnoreCase(dto.getTahasilCode())
				&& !respones.get(0).getTahasilCode().equalsIgnoreCase(dto.getTahasilcodeselct())) {
			log.info("Fail Execute");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 404);
			jsb.put(CommonConstant.RESULT, "Fail..");
			return ResponseEntity.ok(jsb.toString());
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		boolean isMatch = encoder.matches(SECRET_KEY + password, respones.get(0).getPassword());
		if (!isMatch) {
			log.info("Fail..Execute");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 404);
			jsb.put(CommonConstant.RESULT, "Fail..");
			return ResponseEntity.ok(jsb.toString());
		}

		if (respones != null && !respones.isEmpty()) {
			log.info("Sucess..Execute");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 200);
			jsb.put(CommonConstant.RESULT, "Sucess..!");
			return ResponseEntity.ok(jsb.toString());
		}
		log.info("Fail..Execute");
		JSONObject jsb = new JSONObject();
		jsb.put(CommonConstant.STATUS_KEY, 404);
		jsb.put(CommonConstant.RESULT, "Fail..");
		return ResponseEntity.ok(jsb.toString());
	}

	@PostMapping("/getVillageRecord")
	public ResponseEntity<?> getRecordKatha(@RequestBody String formsParms) {
		JSONObject json = new JSONObject(formsParms);
		List<VillageInfoDTO> respones = service.getKathaRecord(json.getString("tahasilCode"),
				json.getString("villageName"));
		if (respones.isEmpty()) {
			log.info(":: No record in getVillageRecord");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 404);
			jsb.put(CommonConstant.RESULT, "No data..!!");
			return ResponseEntity.ok(jsb.toString());
		}
		log.info("Sucess in getVillageRecord");
		VillageRecordRespones response = new VillageRecordRespones(200, respones);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/getVillagekathaRecordMore")
	public ResponseEntity<?> getRecordKathaMore(@RequestBody String formsParms) {
		JSONObject json = new JSONObject(formsParms);
		List<KhatianPlotDTO> respones = service.getKathaRecordMore(json.getString("villageCode"));

		if (respones.isEmpty()) {
			log.info(":: No record in getVillagekathaRecordMore");
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 404);
			jsb.put(CommonConstant.RESULT, "No data found..!!");
			return ResponseEntity.ok(jsb.toString());
		}
		log.info("Sucess in getVillagekathaRecordMore");
		JSONObject jsb = new JSONObject();
		jsb.put(CommonConstant.STATUS_KEY, 200);
		jsb.put(CommonConstant.RESULT, respones);

		return ResponseEntity.ok(jsb.toString());

	}

	@PostMapping("/plotVerification")
	public ResponseEntity<?> verify(@RequestBody String plotVerfication) {
		log.info(plotVerfication);
		JSONObject resp = new JSONObject();

		resp = service.save(plotVerfication);
		return ResponseEntity.ok(resp.toString());
	}

}
