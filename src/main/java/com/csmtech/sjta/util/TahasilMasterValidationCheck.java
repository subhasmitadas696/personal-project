package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TahasilMasterValidationCheck {
	private static final Logger logger = LoggerFactory.getLogger(TahasilMasterValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of Tahasil_masterValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrictCode").toString())) {
			errorStatus = 1;
			errMsg = "District Code  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtTahasilCode").toString())) {
			errorStatus = 1;
			errMsg = "Tahasil Code should not  be empty!";
		}
		/*
		 * if (errorStatus == 0
		 * &&CommonValidator.isNumericKey(obj.get("txtTahasilCode").toString())){
		 * errorStatus = 1; errMsg= "Tahasil Code should be Numeric!";} if (errorStatus
		 * == 0 &&CommonValidator.validNumberCheck( obj.get("txtTahasilCode"
		 * ).toString())){ errorStatus = 1; errMsg=
		 * "Tahasil CodeGiven Number should be valid !";}
		 */
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtTahasilName").toString())) {
			errorStatus = 1;
			errMsg = "Tahasil Name should not  be empty!";
		}
		/*
		 * if (errorStatus == 0 &&
		 * CommonValidator.isCharecterKey(obj.get("txtTahasilName").toString())) {
		 * errorStatus = 1; errMsg = "Tahasil Name should be a character!"; } if
		 * (errorStatus == 0 &&
		 * CommonValidator.isSpecialCharKey(obj.get("txtTahasilName").toString())) {
		 * errorStatus = 1; errMsg = "Tahasil Nameshould be SpecialCharKey !"; }
		 */
		return errMsg;
	}
}