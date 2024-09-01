package com.csmtech.sjta.mobile.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TahasilPlotDto {
	
	
	private Integer plotSurveyId;
	private String districtCode;
	private String districtName;
	private String tahasilCode;
	private String tahasilName;
	private String plotCode;
	private String plotNo;
	private String remarks;
	private String villageCode;
	private String villageName;
	private String kissam;
	private String khatianCode;
	private String khataNo;
	private String surveyDate;
	private String totalArea;
	private String areaAcre; 
	private BigInteger totalPlot;
	private String centralLatitude;
	private String centralLongitude;
	private String coordinates;
	private List<LandLatLng> landLatLong;
	private List<LandDataDto> landData;
	
	
	
	public TahasilPlotDto(Integer plotSurveyId, String tahasilCode, String plotNo, String remarks, String surveyDate) {
		super();
		this.plotSurveyId = plotSurveyId;
		this.tahasilCode = tahasilCode;
		this.plotNo = plotNo;
		this.remarks = remarks;
		this.surveyDate = surveyDate;
	}



	public TahasilPlotDto(Integer plotSurveyId, String tahasilCode, String plotNo, String remarks, String villageCode,
			String khatianCode, String surveyDate) {
	//	super();
		this.plotSurveyId = plotSurveyId;
		this.tahasilCode = tahasilCode;
		this.plotNo = plotNo;
		this.remarks = remarks;
		this.villageCode = villageCode;
		this.khatianCode = khatianCode;
		this.surveyDate = surveyDate;
	}



	public TahasilPlotDto(String tahasilCode, String tahasilName, String villageCode,
			String villageName, String khatianCode, String khataNo, String plotCode, String plotNo) {
		super();
		this.tahasilCode = tahasilCode;
		this.tahasilName = tahasilName;
		this.plotCode = plotCode;
		this.plotNo = plotNo;
		this.villageCode = villageCode;
		this.villageName = villageName;
		this.khatianCode = khatianCode;
		this.khataNo = khataNo;
	}



	public TahasilPlotDto(Integer plotSurveyId, String tahasilCode, String tahasilName, String plotCode, String plotNo,
			String remarks, String villageCode, String villageName, String khatianCode, String khataNo) {
//		super();
		this.plotSurveyId = plotSurveyId;
		this.tahasilCode = tahasilCode;
		this.tahasilName = tahasilName;
		this.plotCode = plotCode;
		this.plotNo = plotNo;
		this.remarks = remarks;
		this.villageCode = villageCode;
		this.villageName = villageName;
		this.khatianCode = khatianCode;
		this.khataNo = khataNo;
	}
	
	
	
	
	
	
	
	

}
