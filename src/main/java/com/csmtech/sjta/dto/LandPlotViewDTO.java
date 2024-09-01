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
public class LandPlotViewDTO {

	String plotNoId;
	String totalArea;
	String purchaseArea;
	String varietiesId;
	String extend;
	String plotCode;
	String khataNo;
	Date coInspectionDate;
	Date tahasildarInspectionDate;
	Short coStatus;
	Short tahasilStatus;
	Integer plotLandInspectionId;
	String isVerified;

}
