package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateMasterValidationCheck {
	private static final Logger logger = LoggerFactory.getLogger(StateMasterValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of State_masterValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selCountryCode").toString())) {
			errorStatus = 1;
			errMsg = "Country Code  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtStateCode").toString())) {
			errorStatus = 1;
			errMsg = "State Code should not  be empty!";
		}
		/*
		 * if (errorStatus == 0
		 * &&CommonValidator.isNumericKey(obj.get("txtStateCode").toString())){
		 * errorStatus = 1; errMsg= "State Code should be Numeric!";} if (errorStatus ==
		 * 0 &&CommonValidator.validNumberCheck( obj.get("txtStateCode" ).toString())){
		 * errorStatus = 1; errMsg= "State CodeGiven Number should be valid !";}
		 */
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtStateName").toString())) {
			errorStatus = 1;
			errMsg = "State Name should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtStateName").toString())) {
			errorStatus = 1;
			errMsg = "State Name should be a character!";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtStateName").toString())) {
			errorStatus = 1;
			errMsg = "State Nameshould be SpecialCharKey !";
		}
		return errMsg;
	}
}