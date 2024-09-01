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
public class LandInformationReportDTO {
	
	private String districtName;
	private String districtCode;
	private BigInteger tahasilCount;
	private BigInteger villageCount;
	private BigInteger khataCount;
	private BigInteger plotCount;
	private String sumAreaAcer;
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
	private String areaAcer;
	private String districtExtent;
	private String tahasilExtent;
	private String villageExtent;
	private String khataExtent;
	private String plotExtent;
	

}
