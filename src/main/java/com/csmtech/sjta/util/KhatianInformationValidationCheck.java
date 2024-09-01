package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KhatianInformationValidationCheck {

	private static final Logger logger = LoggerFactory.getLogger(KhatianInformationValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of Khatian_informationValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selVillageCode").toString())) {
			errorStatus = 1;
			errMsg = "Village Code  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtKhatianCode").toString())) {
			errorStatus = 1;
			errMsg = "Khatian Code should not  be empty!";
		}

		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtKhataNo").toString())) {
			errorStatus = 1;
			errMsg = "Khata No should not  be empty!";
		}

		return errMsg;
	}
}