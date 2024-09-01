package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandApplicantDTO {

	private BigInteger landApplicantId;
	private String applicantNo;
	private String applicantName;
	private String fatherName;
	private String mobileNo;
	private String emailId;
	private String docRefNo;
	private String docsPath;
	private String totalArea;
	private String purchaseArea;
	private String blockName;
	private String gpName;
	private String plotName;
	private String stateName;
	private String districtName;
	private String tehsilName;
	private String mouzaName;
	private String khataNo;
	private String plotNo;
	private Date createdOn;
	private String docName;
	private String currPoliceStation;
	private String prePoliceStation;
	private String currPostOffice;
	private String prePostOffice;
	private String currStreetNo;
	private String preStreetNo;
	private String currPinCode;
	private String prePinCode;
	private String preHouseNo;
	private String currHouseNo;
	private String khatianCode;
	private String extent;
	private String districtCode;
	private String tahasilCode;
	private String villageCode;

	private boolean bitDeletedFlag;
	private BigInteger countint;

	private List<DocumentDTO> documents;
	private List<PlotDTO> plots;

	private String applicationStatus;
	private Date actionOn;
	private Short pendingroleid;
	private String applicationFlow;
	private String remark;
	private Integer status;
	private String coRemarks;
	private Integer assignStatus;
	private String coUploadedPhoto;
	private Date inspectionDate;

	private Short roleId;

	private String plotExtend;
	private Short rejectPendingRoleId;
	private Short approvalLevleId;
	private String roleName;
	private String fullName;
	private Short applicationStatusId;

	private String plotCode;

}
