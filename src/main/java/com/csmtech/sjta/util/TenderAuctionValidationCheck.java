package com.csmtech.sjta.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenderAuctionValidationCheck {
	private static final Logger logger = LoggerFactory.getLogger(TenderAuctionValidationCheck.class);

	public static String BackendValidation(JSONObject obj) {
		logger.info("Inside BackendValidation method of Tender_auctionValidationCheck");
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtAuctionName20").toString())) {
			errorStatus = 1;
			errMsg = "Auction Name should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtAuctionName20"), 1)) {
			errorStatus = 1;
			errMsg = "Auction Name  minimum length should be 1";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtAuctionName20"), 200)) {
			errorStatus = 1;
			errMsg = "Auction Name maxmimum length should be 200";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selSelectSource21").toString())) {
			errorStatus = 1;
			errMsg = "Select Source  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtLeasePeriodinYears22").toString())) {
			errorStatus = 1;
			errMsg = "Lease Period in Years should not  be empty!";
		}
//		if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtLeasePeriodinYears22"), 1)) {
//			errorStatus = 1;
//			errMsg = "Lease Period in Years  minimum length should be 1";
//		}
//		if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtLeasePeriodinYears22"), 200)) {
//			errorStatus = 1;
//			errMsg = "Lease Period in Years maxmimum length should be 200";
//		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selCommitteeMemberName23").toString())) {
			errorStatus = 1;
			errMsg = "Committee Member Name  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtRoyalityBasePriceofMineral26").toString())) {
			errorStatus = 1;
			errMsg = "Royality(Base Price of Mineral) should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtFormMSubmissionStartDate27").toString())) {
			errorStatus = 1;
			errMsg = "Form-M Submission Start Date should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtFormMSubmissionEndDate28").toString())) {
			errorStatus = 1;
			errMsg = "Form-M Submission End Date should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtSecurityDepositStartDate29").toString())) {
			errorStatus = 1;
			errMsg = "Security Deposit Start Date should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtSecurityDepositEndDate").toString())) {
			errorStatus = 1;
			errMsg = "Security Deposit End Date should not  be empty!";
		}
//		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtBidDocumentDownloadStartDate").toString())) {
//			errorStatus = 1;
//			errMsg = "Bid Document Download Start Date should not  be empty!";
//		}
//		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtBidDocumentDownloadEndDate").toString())) {
//			errorStatus = 1;
//			errMsg = "Bid Document Download End Date should not  be empty!";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtDateOfTechnicalEvaluation").toString())) {
			errorStatus = 1;
			errMsg = "Date Of Technical Evaluation should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtApplicationFeeNonRefundable").toString())) {
			errorStatus = 1;
			errMsg = "Application Fee(Non-Refundable) should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtSecurityAmount").toString())) {
			errorStatus = 1;
			errMsg = "Security Amount should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtSlotsForAuctionFromDate").toString())) {
			errorStatus = 1;
			errMsg = "Slots For Auction (From Date) should not  be empty!";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtSlotsForAuctionToDate").toString())) {
			errorStatus = 1;
			errMsg = "Slots For Auction (To Date) should not  be empty!";
		}
		return errMsg;
	}
}