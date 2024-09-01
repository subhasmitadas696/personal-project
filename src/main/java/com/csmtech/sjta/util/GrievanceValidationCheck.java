package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrievanceValidationCheck {

	private GrievanceValidationCheck() {
		super();
	}

	private static final Logger logger = LoggerFactory.getLogger(GrievanceValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of GrievanceValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrict13").toString())) {
			errorStatus = 1;
			errMsg = "District  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selTahasil").toString())) {
			errorStatus = 1;
			errMsg = "Tahasil  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selVillage15").toString())) {
			errorStatus = 1;
			errMsg = "Village  should not be empty !";
		}
		return errMsg;
	}
}