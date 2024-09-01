package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.mobile.dto.LandLatLng;
import com.csmtech.sjta.mobile.dto.PlotLandInspectionDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlotValuationDTO {

	private Integer plotLandInspectionId;
	private Short availableLand;
	private String landUserName;
	private String landAddress;
	private Integer typeOfLandUse;
	private String tenure;
	private Short ownershipRecord;
	private String fileUploadOwnershipDocument;
	private String pricePerAcre;
	private String totalPrice;
	private String khatianCode;
	private String plotCode;
	private String khataNo;
	private String plotNo;
	private BigInteger createdBy;
	private String landUseType;
	
}
