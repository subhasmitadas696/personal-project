/**
 * 
 */
package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.TrackStatusDto;
import com.csmtech.sjta.service.TrackStatusService;
import com.csmtech.sjta.util.CommonCaptchaGenerate;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abhijit.sahoo
 *
 */

@Slf4j
@RestController

@RequestMapping("/trackstatus")
public class TrackStatusController {

	@Autowired
	private TrackStatusService trackStatusService;
	JSONObject resp = new JSONObject();
	String data = "";

	@PostMapping("/showstatus")
	public ResponseEntity<?> showStatus(@RequestBody String formParam) {
		JSONObject requestObj = new JSONObject(formParam);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParam);

			JSONObject formdata = new JSONObject(data);
			String captchaId = formdata.getString("captchaId");
			Integer answer = formdata.getInt("answer");

			boolean validateCaptcha;
			Integer captchaResult = CommonCaptchaGenerate.get(captchaId);

			if (captchaResult != null && Objects.equals(answer, captchaResult)) {
				CommonCaptchaGenerate.remove(captchaId);
				validateCaptcha = true;
			} else {
				validateCaptcha = false;
			}

			if (!validateCaptcha) {
				resp.put(CommonConstant.STATUS_KEY, 223);
				resp.put(CommonConstant.MESSAGE_KEY, "Invalid captcha");
				return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
			}
			TrackStatusDto respDto = new TrackStatusDto();
			if (formdata.getInt("applicationType") == 1) {
				respDto = trackStatusService.fetchGrievanceDetails(formdata);
			} else if (formdata.getInt("applicationType") == 2) {
				respDto = trackStatusService.fetchLandApplicationDetails(formdata);
			}

			if (respDto.getApplicationNo() != null) {
				resp.put("msg", "success");
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, new JSONObject(respDto));
			} else {
				resp.put("msg", "error");
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put("result", "");
			}

		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
			resp.put("result", "");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

}
