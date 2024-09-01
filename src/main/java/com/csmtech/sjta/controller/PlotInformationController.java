package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.PlotInformationDTO;
import com.csmtech.sjta.service.PlotInformationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.PlotInformationValidationCheck;

import lombok.extern.slf4j.Slf4j;

@RestController

@RequestMapping("/land-bank")
@Slf4j
public class PlotInformationController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private PlotInformationService plotInformationService;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(PlotInformationController.class);
	JSONObject resp = new JSONObject();

	@PostMapping("/plot-information/addEdit")
	public ResponseEntity<?> create(@RequestBody String plotInformation) {
		logger.info("Inside create method of Plot_informationController");
		JSONObject requestObj = new JSONObject(plotInformation);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(plotInformation);
			String validationMsg = PlotInformationValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of Plot_informationController Validation Error");
			} else {
				resp = plotInformationService.save(data);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/plot-information/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of Plot_informationController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = plotInformationService.getById(json.getString("intId"));
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put("result", entity);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/plot-information/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of Plot_informationController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = plotInformationService.getAll(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/plotinformation/viewall")
	public ResponseEntity viewAll(@RequestBody PlotInformationDTO dto) {
		log.info("Inside getAll method of plot_informationController");
		try {
			resp = plotInformationService.viewAll(dto.getVillageCode(), dto.getKhataNo());
		} catch (Exception e) {
			log.error("error oocured while fetching plot data: " + e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, 500);
			resp.put("message", "Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(resp.toString(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/plotinformation/plotInfoForTableView")
	public ResponseEntity plotinfoForTableView(@RequestBody PlotInformationDTO dto) {
		try {
			resp = plotInformationService.plotInfoForTableView(dto.getVillageCode());
			log.info("result for " + dto.getVillageCode() + " is: " + resp);
		} catch (Exception e) {
			log.error("error occured while fetching data for plot info:" + e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, 500);
			resp.put("message", "Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(resp.toString(), HttpStatus.OK);
	}

	@PostMapping("/plotinformation/getkissam")
	public ResponseEntity<?> getKissam(@RequestBody String formParams) {
		logger.info("Inside getKissam method of plotinformationController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			resp = plotInformationService.getKissam();

		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}

}