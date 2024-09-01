package com.csmtech.sjta.dto;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlotInformationDTO {

	private String plotCode;

	private String plotNo;

//	@Column(name = "kissam")
	private String kissam;

	private String areaAcre;

	private String khatianCode;

	private String digitalFile;
	
	//added for gis api
	private String khataNo;
	private String villageCode;
	private String villageName;
	private String availableSts;
	private String extent;

}
