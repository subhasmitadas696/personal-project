package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScannedDocValidationCheck {
	private static final Logger logger = LoggerFactory.getLogger(ScannedDocValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of Scanned_docValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("fileType").toString())) {
			errorStatus = 1;
			errMsg = "File Type  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrict12").toString())) {
			errorStatus = 1;
			errMsg = "District  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selTehsil13").toString())) {
			errorStatus = 1;
			errMsg = "Tehsil  should not be empty !";
		}
//		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selMouza14").toString())) {
//			errorStatus = 1;
//			errMsg = "Mouza  should not be empty !";
//		}
//		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selKhataNo15").toString())) {
//			errorStatus = 1;
//			errMsg = "Khata No  should not be empty !";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("fileNo").toString())) {
			errorStatus = 1;
			errMsg = "File No should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("fileUploadScannedPDF16").toString())) {
			errorStatus = 1;
			errMsg = "Upload Scanned PDF file should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.validateFile(obj.get("fileUploadScannedPDF16").toString())) {
			errorStatus = 1;
			errMsg = "Invalid File Type !";
		}
//		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("fileUploadCSV17").toString())) {
//			errorStatus = 1;
//			errMsg = "Upload CSV file should not be empty !";
//		}
//		if (errorStatus == 0 && CommonValidator.validateFile(obj.get("fileUploadCSV17").toString())) {
//			errorStatus = 1;
//			errMsg = "Invalid File Type !";
//		}
		return errMsg;
	}
}