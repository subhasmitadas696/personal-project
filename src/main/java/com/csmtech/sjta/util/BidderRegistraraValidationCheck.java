package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidderRegistraraValidationCheck {
	private BidderRegistraraValidationCheck() {
		super();
	}

	private static final Logger logger = LoggerFactory.getLogger(BidderRegistraraValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of BidderregistraraValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtContactPersonName").toString())) {
			errorStatus = 1;
			errMsg = "Contact Person Name should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPanNumber").toString())) {
			errorStatus = 1;
			errMsg = "Pan Number should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("fileUploadPANCard").toString())) {
			errorStatus = 1;
			errMsg = "Upload PAN Card file should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.validateFile(obj.get("fileUploadPANCard").toString())) {
			errorStatus = 1;
			errMsg = "Invalid File Type !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtAadharNumber").toString())) {
			errorStatus = 1;
			errMsg = "Aadhar Number  should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("fileUploadedAadharCard").toString())) {
			errorStatus = 1;
			errMsg = "Uploaded Aadhar Card file should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.validateFile(obj.get("fileUploadedAadharCard").toString())) {
			errorStatus = 1;
			errMsg = "Invalid File Type !";
		}

		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("fileUploadedResentPhotoOfTheBidder").toString())) {
			errorStatus = 1;
			errMsg = "Uploaded Resent Photo Of The Bidder   file should not be empty !";
		}
		if (errorStatus == 0
				&& CommonValidator.validateFile(obj.get("fileUploadedResentPhotoOfTheBidder").toString())) {
			errorStatus = 1;
			errMsg = "Invalid File Type !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("fileUploadedResentSignatureOfTheBidder").toString())) {
			errorStatus = 1;
			errMsg = "Uploaded Resent Signature Of The Bidder   file should not be empty !";
		}
		if (errorStatus == 0
				&& CommonValidator.validateFile(obj.get("fileUploadedResentSignatureOfTheBidder").toString())) {
			errorStatus = 1;
			errMsg = "Invalid File Type !";
		}
		return errMsg;
	}
}