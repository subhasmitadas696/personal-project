package com.csmtech.sjta.util;

import org.json.JSONObject;

public class Land_plotValidationCheck {
	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrictName").toString())) {
			errorStatus = 1;
			errMsg = "District Name  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selTehsilName").toString())) {
			errorStatus = 1;
			errMsg = "Tehsil Name  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selMouza").toString())) {
			errorStatus = 1;
			errMsg = "Mouza  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selKhataNo").toString())) {
			errorStatus = 1;
			errMsg = "Khata No.  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selPlotNo").toString())) {
			errorStatus = 1;
			errMsg = "Plot No.  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtTotalRakba").toString())) {
			errorStatus = 1;
			errMsg = "Total Rakba should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtTotalRakba"), 1)) {
			errorStatus = 1;
			errMsg = "Total Rakba  minimum length should be 1";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtTotalRakba"), 45)) {
			errorStatus = 1;
			errMsg = "Total Rakba maxmimum length should be 45";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtTotalRakba"))) {
			errorStatus = 1;
			errMsg = "Total Rakbashould be SpecialCharKey !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPurchaseRakba").toString())) {
			errorStatus = 1;
			errMsg = "Purchase Rakba should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPurchaseRakba"), 1)) {
			errorStatus = 1;
			errMsg = "Purchase Rakba  minimum length should be 1";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPurchaseRakba"), 45)) {
			errorStatus = 1;
			errMsg = "Purchase Rakba maxmimum length should be 45";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtPurchaseRakba"))) {
			errorStatus = 1;
			errMsg = "Purchase Rakbashould be SpecialCharKey !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selVarieties").toString())) {
			errorStatus = 1;
			errMsg = "Varieties  should not be empty !";
		}
		return errMsg;
	}
}