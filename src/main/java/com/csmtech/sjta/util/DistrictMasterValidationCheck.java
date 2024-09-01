package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistrictMasterValidationCheck {

	private DistrictMasterValidationCheck() {
		super();
	}

	private static final Logger logger = LoggerFactory.getLogger(DistrictMasterValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of District_masterValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selStateCode").toString())) {
			errorStatus = 1;
			errMsg = "State Code  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtDistrictCode").toString())) {
			errorStatus = 1;
			errMsg = "District Code should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isNumericKey(obj.get("txtDistrictCode").toString())) {
			errorStatus = 1;
			errMsg = "District Code should be Numeric!";
		}
		if (errorStatus == 0 && CommonValidator.validNumberCheck(obj.get("txtDistrictCode").toString())) {
			errorStatus = 1;
			errMsg = "District CodeGiven Number should be valid !";
		}
		if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtDistrictName").toString())) {
			errorStatus = 1;
			errMsg = "District Name should be a character!";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtDistrictName").toString())) {
			errorStatus = 1;
			errMsg = "District Nameshould be SpecialCharKey !";
		}
		return errMsg;
	}
}