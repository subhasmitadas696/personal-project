package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.util.List;

import com.csmtech.sjta.mobile.dto.LandLatLng;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceMainDTO implements Serializable {

	/**
	 * @author RRJ
	 */
	private static final long serialVersionUID = 3056690420751528990L;

	private Integer grievanceId;
	private String monthId;
	private String name;
	private String fatherName;
	private String districtId;
	private String districtIdName; // new
	private String blockId;
	private String blockIdName; // new
	private String gpId;
	private String gpIdName; // new
	private String villageId;
	private String villageIdName; // new
	private String kissam; // new
	private String otherInformation;
	private String casteName;
	private String mobileNo;
	private Short discloseDetails;
	private String districtCode;
	private String districtName;
	private String tahasilCode;
	private String tahasilName;
	private String villageCode;
	private String villageName;
	private String khatianCode;
	private String plotNo;
	private String areaAcre;
	private String extentOccupied;
	private Short modeOfOccupation;
	private String otherOccupation;
	private String landmark;
	private String uploadFile;
	private String remarks;
	private String grievanceNo;
	private Short grivanceStatus;
	// added for mobile api
	private String latitude;
	private String longitude;
	private Integer monthOfUnauthorizedOccupation;
	private String landLocation;
	private String userId;
	// added for co inspection api changes
	// private Integer grievanceId;
	// added for mobile functionality
	private String scheduledInspectionDate;// from date to string
	private String inspectionBy;
	private String inspectionDate;// from date to string
	private String goFinalRemarks;
	private String goRemarks;
	private String coRemarks;
	private String coUploadedPhoto;
	private String classOfLandEncroached;
	// private String grievanceNo;
	private String coordinates;
	private String khataNumber;
	private String plotNumber;
	// newly added requirement
	private String monthName;
	private List<LandLatLng> landLatLong;
	private String postedBy;
	private String remarkByCo;

	private String plotArea;
	private String fileName;
	private String imageLink;
	private String verifiedImageLink;

	public GrievanceMainDTO(Integer grievanceId, String monthId, String name, String fatherName, String districtId,
			String districtIdName, String blockId, String blockIdName, String gpId, String gpIdName, String villageId,
			String villageIdName, String kissam, String otherInformation, String casteName, String mobileNo,
			Short discloseDetails, String districtCode, String districtName, String tahasilCode, String tahasilName,
			String villageCode, String villageName, String khatianCode, String plotNo, String areaAcre,
			String extentOccupied, Short modeOfOccupation, String otherOccupation, String landmark, String uploadFile,
			String remarks, String grievanceNo, Short grivanceStatus, String coUploadedPhoto, String remarkByCo,
			String inspectionDate) {
		this.grievanceId = grievanceId;
		this.monthId = monthId;
		this.name = name;
		this.fatherName = fatherName;
		this.districtId = districtId;
		this.districtIdName = districtIdName;
		this.blockId = blockId;
		this.blockIdName = blockIdName;
		this.gpId = gpId;
		this.gpIdName = gpIdName;
		this.villageId = villageId;
		this.villageIdName = villageIdName;
		this.kissam = kissam;
		this.otherInformation = otherInformation;
		this.casteName = casteName;
		this.mobileNo = mobileNo;
		this.discloseDetails = discloseDetails;
		this.districtCode = districtCode;
		this.districtName = districtName;
		this.tahasilCode = tahasilCode;
		this.tahasilName = tahasilName;
		this.villageCode = villageCode;
		this.villageName = villageName;
		this.khatianCode = khatianCode;
		this.plotNo = plotNo;
		this.areaAcre = areaAcre;
		this.extentOccupied = extentOccupied;
		this.modeOfOccupation = modeOfOccupation;
		this.otherOccupation = otherOccupation;
		this.landmark = landmark;
		this.uploadFile = uploadFile;
		this.remarks = remarks;
		this.grievanceNo = grievanceNo;
		this.grivanceStatus = grivanceStatus;
		this.coUploadedPhoto = coUploadedPhoto;
		this.remarkByCo = remarkByCo;
		this.inspectionDate = inspectionDate;

	}

	public GrievanceMainDTO(String plotNo, String plotNumber, String plotArea) {
		this.plotNo = plotNo;
		this.plotNumber = plotNumber;
		this.plotArea = plotArea;
	}

}
