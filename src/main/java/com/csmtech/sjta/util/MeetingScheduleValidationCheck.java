package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeetingScheduleValidationCheck {
	private static final Logger logger = LoggerFactory.getLogger(MeetingScheduleValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of Meeting_scheduleValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		/*
		 * if (errorStatus == 0 &&
		 * CommonValidator.blankCheckRdoDropChk(obj.get("selDistrict48").toString())) {
		 * errorStatus = 1; errMsg = "District  should not be empty !"; } if
		 * (errorStatus == 0 &&
		 * CommonValidator.blankCheckRdoDropChk(obj.get("selTahasil49").toString())) {
		 * errorStatus = 1; errMsg = "Tahasil  should not be empty !"; } if (errorStatus
		 * == 0 &&
		 * CommonValidator.blankCheckRdoDropChk(obj.get("selVillage50").toString())) {
		 * errorStatus = 1; errMsg = "Village  should not be empty !"; } if (errorStatus
		 * == 0 &&
		 * CommonValidator.blankCheckRdoDropChk(obj.get("selKhatian51").toString())) {
		 * errorStatus = 1; errMsg = "Khatian  should not be empty !"; } if (errorStatus
		 * == 0 &&
		 * CommonValidator.blankCheckRdoDropChk(obj.get("selPlot52").toString())) {
		 * errorStatus = 1; errMsg = "Plot  should not be empty !"; }
		 */
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtMeetingDate53").toString())) {
			errorStatus = 1;
			errMsg = "Meeting Date should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selMeetingLevel55").toString())) {
			errorStatus = 1;
			errMsg = "Meeting Level  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtrMeetingPurpose54").toString())) {
			errorStatus = 1;
			errMsg = "Meeting Purpose should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtrMeetingPurpose54"), 500)) {
			errorStatus = 1;
			errMsg = "Meeting Purpose maxmimum length should be 500";
		}
		/*
		 * if (errorStatus == 0 &&
		 * CommonValidator.isAlphaNumericKey(obj.get("txtrMeetingPurpose54").toString())
		 * ) { errorStatus = 1; errMsg = "Meeting Purpose should be AlphaNumeric !"; }
		 */
		return errMsg;
	}
}