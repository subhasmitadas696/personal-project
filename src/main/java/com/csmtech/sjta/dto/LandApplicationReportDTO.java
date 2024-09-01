package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandApplicationReportDTO {
	
	private String districtName;
	private String districtCode;
	private BigInteger tahasilCount;
	private BigInteger villageCount;
	private BigInteger khataCount;
	private BigInteger plotCount;
	private BigDecimal sumAreaAcer;
	private String tahasilName;
	private String tahasilCode;
	private String villageName;
	private String villageCode;
	private String khataNo;
	private String KhataCode;
	private String ownerName;
	private String marfatdarName;
	private String sotwar;
	private Date publicationDate;
	private String plotNo;
	private String plotCode;
	private String kissam;
	private BigDecimal areaAcre;
	private BigInteger total;
	private BigDecimal areaApplied;
	private BigInteger approved;
	private BigInteger forwarded;
	private BigInteger reverted;
	private BigInteger rejected;
	private BigInteger pending;
	private String applicationNo;
	private String applicantName;
	private String applicationStatus;



}
