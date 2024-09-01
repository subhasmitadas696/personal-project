package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VillageMasterValidationCheck {
	private static final Logger logger = LoggerFactory.getLogger(VillageMasterValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of Village_masterValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selTahasilCode").toString())) {
			errorStatus = 1;
			errMsg = "Tahasil Code  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtVillageCode").toString())) {
			errorStatus = 1;
			errMsg = "Village Code should not  be empty!";
		}
		/*
		 * if (errorStatus == 0 &&
		 * CommonValidator.isNumericKey(obj.get("txtVillageCode").toString())) {
		 * errorStatus = 1; errMsg = "Village Code should be Numeric!"; } if
		 * (errorStatus == 0 &&
		 * CommonValidator.validNumberCheck(obj.get("txtVillageCode").toString())) {
		 * errorStatus = 1; errMsg = "Village CodeGiven Number should be valid !"; }
		 */
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtVillageName").toString())) {
			errorStatus = 1;
			errMsg = "Village Name should not  be empty!";
		}
		/*
		 * if (errorStatus == 0 &&
		 * CommonValidator.isCharecterKey(obj.get("txtVillageName").toString())) {
		 * errorStatus = 1; errMsg = "Village Name should be a character!"; } if
		 * (errorStatus == 0 &&
		 * CommonValidator.isSpecialCharKey(obj.get("txtVillageName").toString())) {
		 * errorStatus = 1; errMsg = "Village Nameshould be SpecialCharKey !"; } if
		 * (errorStatus == 0 &&
		 * CommonValidator.isCharecterKey(obj.get("txtPSName").toString())) {
		 * errorStatus = 1; errMsg = "PS Name should be a character!"; } if (errorStatus
		 * == 0 && CommonValidator.isSpecialCharKey(obj.get("txtPSName").toString())) {
		 * errorStatus = 1; errMsg = "PS Nameshould be SpecialCharKey !"; }
		 */
		/*
		 * if (errorStatus == 0
		 * &&CommonValidator.isCharecterKey(obj.get("txtricname").toString())){
		 * errorStatus = 1; errMsg= "ric_name should be a character!";} if (errorStatus
		 * == 0 &&CommonValidator.isSpecialCharKey(obj.get("txtricname").toString())){
		 * errorStatus = 1; errMsg= "ric_nameshould be SpecialCharKey !";} if
		 * (errorStatus == 0
		 * &&CommonValidator.isAlphaNumericKey(obj.get("txtThanaNo").toString())){
		 * errorStatus = 1; errMsg= "Thana No should be AlphaNumeric !";} if
		 * (errorStatus == 0
		 * &&CommonValidator.isSpecialCharKey(obj.get("txtThanaNo").toString())){
		 * errorStatus = 1; errMsg= "Thana Noshould be SpecialCharKey !";}
		 */
		return errMsg;
	}
}