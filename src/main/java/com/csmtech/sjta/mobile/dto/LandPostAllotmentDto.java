package com.csmtech.sjta.mobile.dto;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandPostAllotmentDto {

	private Integer postAllotmentInspectionId;
	private String districtCode;
	private String districtName;
	private String tahasilCode;
	private String tahasilName;
	private String villageCode;
	private String villageName;
	private String khatianCode;
	private String khataNo;
	private String plotCode;
	private String plotNo;
	private String coUploadedPhoto;
	private String imageLink;
	private String coRemarks;
	private String scheduledInspectionDate;
	private String inspectionBy;
	private String inspectionDate;
	private String totalArea;
	private String totalAreaVillage;
	private String purchaseArea;
	private String pricePerAcre;
	private String totalPrice;
	private String loRemarks;
	private String kissam;
	private BigInteger totalPlot;
	private BigInteger createdBy;
	private String centralLatitude;
	private String centralLongitude;
	private String latitude;
	private String longitude;
	private String coordinates;
	private List<LandLatLng> landLatLong;
	private String fileName;
	private String userId;

	public LandPostAllotmentDto(String villageCode, String villageName, BigInteger totalPlot) {
		// super();
		this.villageCode = villageCode;
		this.villageName = villageName;
		this.totalPlot = totalPlot;
	}

}
