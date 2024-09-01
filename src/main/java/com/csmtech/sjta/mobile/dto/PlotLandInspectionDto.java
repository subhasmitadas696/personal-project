package com.csmtech.sjta.mobile.dto;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlotLandInspectionDto {

	private Integer plotLandInspectionId;
	private String districtCode;
	private String districtName;
	private String tahasilCode;
	private String tahasilName;
	private String villageCode;
	private String villageName;
	private String khatianCode;
	private String khataNo;
	private String plotCode;
	private String plotNumber;
	private String areaAcre;
	private String coRemarks;
	private String coUploadedPhoto;
	private String inspectionDate; // date to string
	private String latitude;
	private String longitude;
	private Short approveStatus;
	private String scheduledInspectionDate; // date to string
	private String createdDate;
	private String userId; // added for mobility team
	private String centralLatitude;
	private String centralLongitude;
	// added for box coordinates
	private String coordinates;
	private List<LandLatLng> landLatLong;
	private String tahasilRemarks;
	private String tahasilUploadedPhoto;
	private String tahasilLatitude;
	private String tahasilLongitude;
	private Short tahasilStatus;
	private String tahasildarInspectedBy;
	private String tahasildarInspectionDate;
	private String fileName;
	private String imageLink;
	private String tahasilImageLink;
	private String marfatdarName;
	private String sotwa;
	private String totalArea;
	private String purchaseArea;
	private String applicantName;
	private String applicationNo;
	private String applicationId;
	private String createdBy;
	private BigInteger landApplicationId;


}
