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
public class PaginationInRegisterDtoResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// pageNumber :: what is the page number like 1,2,3,4,5 NEXT,Previous
	// pageSize :: how much record we want in a single page like:: 5,10,20,30
	BigInteger roleId;
	Integer createdBy;
	String applicantName;
	String fullName;
	Integer pageNumber;
	Integer pageSize;
	String pageType;
	String searchapplicantName;
	String mobileNo;
	String serarchUniqueId;
	String serachPlotNo;
	String applicationNo;
	String districtName;
	String khataNo;
	String plotNo;
	Date meetingDate;
	String selDistrictName;
	String selTahasilName;
	String plot_code;
	Short meetingLevleId;
	BigInteger meetingId;
	BigInteger landApplicantId;
	Short meetingLevleIdRe;
	BigInteger meetingIdRe;
	BigInteger meetingSheduleIdRe;
	private Integer auctionFlag;
	private String tahasilCode;
	private Integer auctionFlagCount;
	private BigInteger meetingIid;
	private String plotCcode;
	private String districtCode;
	private String villageCode;
	private String khatianCode;

}