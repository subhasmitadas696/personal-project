package com.csmtech.sjta.util;

import org.json.JSONObject;

public class ManageNotificationValidationCheck {
	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txttitle").toString())) {
			errorStatus = 1;
			errMsg = "title should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txttitle"), 1)) {
			errorStatus = 1;
			errMsg = "title  minimum length should be 1";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txttitle"), 120)) {
			errorStatus = 1;
			errMsg = "title maxmimum length should be 120";
		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txttitle"))) {
//			errorStatus = 1;
//			errMsg = "title should be SpecialCharKey !";
//		}
		if (errorStatus == 0 && CommonValidator.validateFile((String) obj.get("fileUploadDocument"))) {
			errorStatus = 1;
			errMsg = "Invalid File Type !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtrDescription").toString())) {
			errorStatus = 1;
			errMsg = "Description should not  be empty!";
		}
		return errMsg;
	}
}