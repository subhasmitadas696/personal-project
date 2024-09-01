package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidderRegistratatonViewMoreDTO implements Serializable {
	/**
	 * @author rashmi.jena
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger intId;
	
	private Date stmUpdatedOn;

	private BigInteger txtContactPersonName;

	private String txtPanNumber;

	private String fileUploadPANCard;

	private String txtAadharNumber;

	private String fileUploadedAadharCard;

	private String fileUploadedResentPhotoOfTheBidder;

	private String fileUploadedResentSignatureOfTheBidder;

	private String selState;

	private String selStateVal;

	private String selDistrict;

	private String selBlockULB;

	private String selGPWardNo;

	private String selVillageLocalAreaName;

	private String selVillageLocalAreaNameVal;

	private String txtPoliceStation;

	private String txtPostOffice;

	private String txtHabitationStreetNoLandmark;

	private String txtHouseNo;

	private String txtPinCode;

	private String selState17;

	private String selDistrict18;

	private String selBlockULB19;

	private String selGPWARDNumber;

	private String selVillageLocalAreaName21;

	private String txtPoliceStation22;

	private String txtPostOffice23;

	private String txtHabitationStreetNoLandmark24;
	private String txtHouseNo25;

	private String txtPinCode26;
	
	private Character status;
	
	private String applicationNo;
	
	private String applicantName;
	
	private BigInteger tenderAuctionId;

}
