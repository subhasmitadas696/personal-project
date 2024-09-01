package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlotInformationValidationCheck {
	private static final Logger logger = LoggerFactory.getLogger(PlotInformationValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of Plot_informationValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selKhatianCode").toString())) {
			errorStatus = 1;
			errMsg = "Khatian Code  should not be empty !";
		}
//		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPlotCode").toString())) {
//			errorStatus = 1;
//			errMsg = "Plot Code should not  be empty!";
//		}
		/*
		 * if (errorStatus == 0 &&
		 * CommonValidator.isNumericKey(obj.get("txtPlotCode").toString())) {
		 * errorStatus = 1; errMsg = "Plot Code should be Numeric!"; } if (errorStatus
		 * == 0 && CommonValidator.validNumberCheck(obj.get("txtPlotCode").toString()))
		 * { errorStatus = 1; errMsg = "Plot CodeGiven Number should be valid !"; }
		 */

		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPlotNo").toString())) {
			errorStatus = 1;
			errMsg = "Plot No should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtrRemarks").toString())) {
			errorStatus = 1;
			errMsg = "Remarks can not be empty!";
		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtrRemarks").toString())) {
//			errorStatus = 1;
//			errMsg = "Remarks should be a character!";
//		}

		/*
		 * if (errorStatus == 0 &&
		 * CommonValidator.isAlphaNumericKey(obj.get("txtPlotNo").toString())) {
		 * errorStatus = 1; errMsg = "Plot No should be AlphaNumeric !"; } if
		 * (errorStatus == 0 &&
		 * CommonValidator.validNumberCheck(obj.get("txtPlotNo").toString())) {
		 * errorStatus = 1; errMsg = "Plot NoGiven Number should be valid !"; }
		 */
//if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtChakaNumber").toString())){
// errorStatus = 1;
//errMsg= "Chaka Number should not  be empty!";}
//if (errorStatus == 0 &&CommonValidator.isNumericKey(obj.get("txtChakaNumber").toString())){
// errorStatus = 1;
//errMsg= "Chaka Number should be Numeric!";}
//if (errorStatus == 0 &&CommonValidator.validNumberCheck( obj.get("txtChakaNumber" ).toString())){
// errorStatus = 1;
//errMsg= "Chaka NumberGiven Number should be valid !";}
//if (errorStatus == 0 &&CommonValidator.isAlphaNumericKey(obj.get("txtChakaName").toString())){
// errorStatus = 1;
//errMsg= "Chaka Name should be AlphaNumeric !";}
//if (errorStatus == 0 &&CommonValidator.isSpecialCharKey(obj.get("txtChakaName").toString())){
// errorStatus = 1;
//errMsg= "Chaka Nameshould be SpecialCharKey !";}
//if (errorStatus == 0 &&CommonValidator.isAlphaNumericKey(obj.get("txtKissam").toString())){
// errorStatus = 1;
//errMsg= "Kissam should be AlphaNumeric !";}
//if (errorStatus == 0 &&CommonValidator.isSpecialCharKey(obj.get("txtKissam").toString())){
// errorStatus = 1;
//errMsg= "Kissamshould be SpecialCharKey !";}
//if (errorStatus == 0 &&CommonValidator.isNumericKey(obj.get("txtAreaAcre").toString())){
// errorStatus = 1;
//errMsg= "Area Acre should be Numeric!";}
//if (errorStatus == 0 &&CommonValidator.validNumberCheck( obj.get("txtAreaAcre" ).toString())){
// errorStatus = 1;
//errMsg= "Area AcreGiven Number should be valid !";}

		return errMsg;
	}
}