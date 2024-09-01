package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryMasterValidationCheck {
	private static final Logger logger = LoggerFactory.getLogger(CountryMasterValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of Country_masterValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtCountryCode").toString())) {
			errorStatus = 1;
			errMsg = "Country Code should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey(obj.get("txtCountryCode").toString())) {
			errorStatus = 1;
			errMsg = "Country Code should be AlphaNumeric !";
		}
		if (errorStatus == 0 && CommonValidator.validNumberCheck(obj.get("txtCountryCode").toString())) {
			errorStatus = 1;
			errMsg = "Country CodeGiven Number should be valid !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtCountryName").toString())) {
			errorStatus = 1;
			errMsg = "Country Name should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtCountryName").toString())) {
			errorStatus = 1;
			errMsg = "Country Name should be a character!";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtCountryName").toString())) {
			errorStatus = 1;
			errMsg = "Country Nameshould be SpecialCharKey !";
		}
		return errMsg;
	}
}