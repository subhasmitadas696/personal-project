package com.csmtech.sjta.util;

import org.json.JSONObject;

public class LandApplicantValidationCheck {
	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtApplicantName").toString())) {
			errorStatus = 1;
			errMsg = "Applicant Name should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtApplicantName"), 3)) {
			errorStatus = 1;
			errMsg = "Applicant Name  minimum length should be 3";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtApplicantName"), 180)) {
			errorStatus = 1;
			errMsg = "Applicant Name maxmimum length should be 180";
		}
//		if (errorStatus == 0 && CommonValidator.isCharecterKey((String) obj.get("txtApplicantName"))) {
//			errorStatus = 1;
//			errMsg = "Applicant Name should be a character!";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtApplicantName"))) {
//			errorStatus = 1;
//			errMsg = "Applicant Nameshould be SpecialCharKey !";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtFatherHusbandName").toString())) {
			errorStatus = 1;
			errMsg = "Father/Husband Name should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtFatherHusbandName"), 3)) {
			errorStatus = 1;
			errMsg = "Father/Husband Name  minimum length should be 3";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtFatherHusbandName"), 180)) {
			errorStatus = 1;
			errMsg = "Father/Husband Name maxmimum length should be 180";
		}
//		if (errorStatus == 0 && CommonValidator.isCharecterKey((String) obj.get("txtFatherHusbandName"))) {
//			errorStatus = 1;
//			errMsg = "Father/Husband Name should be a character!";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtFatherHusbandName"))) {
//			errorStatus = 1;
//			errMsg = "Father/Husband Nameshould be SpecialCharKey !";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtMobileNo").toString())) {
			errorStatus = 1;
			errMsg = "Mobile No should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtMobileNo"), 10)) {
			errorStatus = 1;
			errMsg = "Mobile No  minimum length should be 10";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtMobileNo"), 10)) {
			errorStatus = 1;
			errMsg = "Mobile No. maxmimum length should be 10";
		}
//		if (errorStatus == 0 && CommonValidator.isNumericKey((String) obj.get("txtMobileNo"))) {
//			errorStatus = 1;
//			errMsg = "Mobile No. should be Numeric!";
//		}
//		if (errorStatus == 0 && CommonValidator.validTelCheck((String) obj.get("txtMobileNo"))) {
//			errorStatus = 1;
//			errMsg = "Mobile No. should be  valid !";
//		}
//		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtEmail").toString())) {
//			errorStatus = 1;
//			errMsg = "Email should not  be empty!";
//		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtEmail"), 10)) {
			errorStatus = 1;
			errMsg = "Email  minimum length should be 10";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtEmail"), 80)) {
			errorStatus = 1;
			errMsg = "Email maxmimum length should be 80";
		}
//		if (errorStatus == 0 && CommonValidator.emailCheck((String) obj.get("txtEmail"))) {
//			errorStatus = 1;
//			errMsg = "Given Email should be valid !";
//		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDocumentType").toString())) {
			errorStatus = 1;
			errMsg = "Document Type  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtDocumentNo").toString())) {
			errorStatus = 1;
			errMsg = "Document No. should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtDocumentNo"), 5)) {
			errorStatus = 1;
			errMsg = "Document No.  minimum length should be 5";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtDocumentNo"), 24)) {
			errorStatus = 1;
			errMsg = "Document No. maxmimum length should be 24";
		}
//		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey((String) obj.get("txtDocumentNo"))) {
//			errorStatus = 1;
//			errMsg = "Document No. should be AlphaNumeric !";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtDocumentNo"))) {
//			errorStatus = 1;
//			errMsg = "Document Noshould be SpecialCharKey !";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty((String) obj.get("fileUploadDocument"))) {
			errorStatus = 1;
			errMsg = "Upload Document file should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.validateFile((String) obj.get("fileUploadDocument"))) {
			errorStatus = 1;
			errMsg = "Invalid File Type !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selState").toString())) {
			errorStatus = 1;
			errMsg = "State  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrict").toString())) {
			errorStatus = 1;
			errMsg = "District  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selBlockULB").toString())) {
			errorStatus = 1;
			errMsg = "Block/ULB  should not be empty !";
		}
//		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selGPWardNo").toString())) {
//			errorStatus = 1;
//			errMsg = "GP/Ward No  should not be empty !";
//		}
//		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selVillageLocalAreaName").toString())) {
//			errorStatus = 1;
//			errMsg = "Village/Local Area Name  should not be empty !";
//		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("txtPoliceStation").toString())) {
			errorStatus = 1;
			errMsg = "Police Station should not  be empty!";
		}
//		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPostOffice").toString())) {
//			errorStatus = 1;
//			errMsg = "Post Office should not  be empty!";
//		}
//		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPostOffice"), 1)) {
//			errorStatus = 1;
//			errMsg = "Post Office  minimum length should be 1";
//		}
//		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPostOffice"), 45)) {
//			errorStatus = 1;
//			errMsg = "Post Office maxmimum length should be 45";
//		}
//		if (errorStatus == 0 && CommonValidator.isCharecterKey((String) obj.get("txtPostOffice"))) {
//			errorStatus = 1;
//			errMsg = "Post Office should be a character!";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtPostOffice"))) {
//			errorStatus = 1;
//			errMsg = "Post Officeshould be SpecialCharKey !";
//		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtHabitationStreetNoLandmark"), 1)) {
			errorStatus = 1;
			errMsg = "Address Line 1 minimum length should be 1";
		}
		if (errorStatus == 0
				&& CommonValidator.maxLengthCheck((String) obj.get("txtHabitationStreetNoLandmark"), 150)) {
			errorStatus = 1;
			errMsg = "Address Line 1 maxmimum length should be 90";
		}
//		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey((String) obj.get("txtHabitationStreetNoLandmark"))) {
//			errorStatus = 1;
//			errMsg = "Habitation/ Street No./ Landmark should be AlphaNumeric !";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtHabitationStreetNoLandmark"))) {
//			errorStatus = 1;
//			errMsg = "Habitation/ Street No./ Landmarkshould be SpecialCharKey !";
//		}
		/*
		 * if (errorStatus == 0 && CommonValidator.minLengthCheck((String)
		 * obj.get("txtHouseNo"), 1)) { errorStatus = 1; errMsg =
		 * "House No.  minimum length should be 1"; } if (errorStatus == 0 &&
		 * CommonValidator.maxLengthCheck((String) obj.get("txtHouseNo"), 45)) {
		 * errorStatus = 1; errMsg = "House No. maxmimum length should be 45"; }
		 */
//		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey((String) obj.get("txtHouseNo"))) {
//			errorStatus = 1;
//			errMsg = "House No. should be AlphaNumeric !";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtHouseNo"))) {
//			errorStatus = 1;
//			errMsg = "House No.should be SpecialCharKey !";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPinCode").toString())) {
			errorStatus = 1;
			errMsg = "Pin Code should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtPinCode").toString(), 6)) {
			errorStatus = 1;
			errMsg = "Pin Code  minimum length should be 6";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtPinCode").toString(), 6)) {
			errorStatus = 1;
			errMsg = "Pin Code maxmimum length should be 6";
		}
//		if (errorStatus == 0 && CommonValidator.isNumericKey(obj.get("txtPinCode").toString())) {
//			errorStatus = 1;
//			errMsg = "Pin Code should be Numeric!";
//		}
		if (errorStatus == 0 && CommonValidator.validNumberCheck(obj.get("txtPinCode").toString())) {
			errorStatus = 1;
			errMsg = "Pin CodeGiven Number should be valid !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selState17").toString())) {
			errorStatus = 1;
			errMsg = "State  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrict18").toString())) {
			errorStatus = 1;
			errMsg = "District  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selBlockULB19").toString())) {
			errorStatus = 1;
			errMsg = "Block/ULB  should not be empty !";
		}
//		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selGPWARDNumber").toString())) {
//			errorStatus = 1;
//			errMsg = "GP/ WARD_Number  should not be empty !";
//		}
//		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selVillageLocalAreaName21").toString())) {
//			errorStatus = 1;
//			errMsg = "Village/ Local Area Name  should not be empty !";
//		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("txtPoliceStation22").toString())) {
			errorStatus = 1;
			errMsg = "Police Station should not  be empty";
		}
//		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPostOffice23").toString())) {
//			errorStatus = 1;
//			errMsg = "Post Office should not  be empty!";
//		}
//		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPostOffice23"), 1)) {
//			errorStatus = 1;
//			errMsg = "Post Office  minimum length should be 1";
//		}
//		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPostOffice23"), 45)) {
//			errorStatus = 1;
//			errMsg = "Post Office maxmimum length should be 45";
//		}
//		if (errorStatus == 0 && CommonValidator.isCharecterKey((String) obj.get("txtPostOffice23"))) {
//			errorStatus = 1;
//			errMsg = "Post Office should be a character!";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtPostOffice23"))) {
//			errorStatus = 1;
//			errMsg = "Post Officeshould be SpecialCharKey !";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtHabitationStreetNoLandmark24").toString())) {
			errorStatus = 1;
			errMsg = "Address Line 1 should not  be empty!";
		}
		if (errorStatus == 0
				&& CommonValidator.minLengthCheck((String) obj.get("txtHabitationStreetNoLandmark24"), 1)) {
			errorStatus = 1;
			errMsg = "Address Line 1 minimum length should be 1";
		}
		if (errorStatus == 0
				&& CommonValidator.maxLengthCheck((String) obj.get("txtHabitationStreetNoLandmark24"), 150)) {
			errorStatus = 1;
			errMsg = "Address Line 1 maxmimum length should be 150";
		}
//		if (errorStatus == 0
//				&& CommonValidator.isAlphaNumericKey((String) obj.get("txtHabitationStreetNoLandmark24"))) {
//			errorStatus = 1;
//			errMsg = "Habitation/ Street No./ Landmark  should be AlphaNumeric !";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtHabitationStreetNoLandmark24"))) {
//			errorStatus = 1;
//			errMsg = "Habitation/ Street No./ Landmark should be SpecialCharKey !";
//		}
		/*
		 * if (errorStatus == 0 &&
		 * CommonValidator.isEmpty(obj.get("txtHouseNo25").toString())) { errorStatus =
		 * 1; errMsg = "House No. should not  be empty!"; }
		 */
		/*
		 * if (errorStatus == 0 && CommonValidator.minLengthCheck((String)
		 * obj.get("txtHouseNo25"), 1)) { errorStatus = 1; errMsg =
		 * "House No.  minimum length should be 1"; } if (errorStatus == 0 &&
		 * CommonValidator.maxLengthCheck((String) obj.get("txtHouseNo25"), 45)) {
		 * errorStatus = 1; errMsg = "House No. maxmimum length should be 45"; }
		 */
//		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey((String) obj.get("txtHouseNo25"))) {
//			errorStatus = 1;
//			errMsg = "House No. should be AlphaNumeric !";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey((String) obj.get("txtHouseNo25"))) {
//			errorStatus = 1;
//			errMsg = "House No.should be SpecialCharKey !";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPinCode26").toString())) {
			errorStatus = 1;
			errMsg = "Pin Code should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtPinCode26").toString(), 6)) {
			errorStatus = 1;
			errMsg = "Pin Code  minimum length should be 6";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtPinCode26").toString(), 6)) {
			errorStatus = 1;
			errMsg = "Pin Code maxmimum length should be 6";
		}
//		if (errorStatus == 0 && CommonValidator.isNumericKey( obj.get("txtPinCode26").toString())) {
//			errorStatus = 1;
//			errMsg = "Pin Code should be Numeric!";
//		}
		if (errorStatus == 0 && CommonValidator.validNumberCheck(obj.get("txtPinCode26").toString())) {
			errorStatus = 1;
			errMsg = "Pin Code should be valid !";
		}
		return errMsg;
	}
}