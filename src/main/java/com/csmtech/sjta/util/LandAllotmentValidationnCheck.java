package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LandAllotmentValidationnCheck {

	private LandAllotmentValidationnCheck() {
		super();
	}

	private static final Logger logger = LoggerFactory.getLogger(LandAllotmentValidationnCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of LandAllertmentValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("meetingScheduleId").toString())) {
			errorStatus = 1;
			errMsg = " Meeting Schedule Id should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("applicantName").toString())) {
			errorStatus = 1;
			errMsg = " Land Application Id should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("selPlotNo").toString())) {
			errorStatus = 1;
			errMsg = "Plot No should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("totalArea").toString())) {
			errorStatus = 1;
			errMsg = " Total Area  should not  be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("purchaseArea").toString())) {
			errorStatus = 1;
			errMsg = "Purchase Area should not be empty  !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("piceInPerAcer").toString())) {
			errorStatus = 1;
			errMsg = "Price Per Acre  should not  be empty  !";
		}

		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("totalPriceInPurchaseArea").toString())) {
			errorStatus = 1;
			errMsg = " Total Price In Purchase Area   should not  be empty  !";
		}
		return errMsg;
	}
}