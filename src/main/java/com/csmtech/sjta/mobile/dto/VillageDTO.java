package com.csmtech.sjta.mobile.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VillageDTO {
	
	private String villageCode;
	private String villageName;
	private String tahasilCode;
    private String tahasilName;
    private String districtCode;
    private String districtName;//changed from district
	private String plotNo;
	private String plotCode;// newly added
	private String kisam;
	private String khatianNo;
	private String khatianCode;
	private BigDecimal totalArea;
	private BigInteger totalPlot;
	private String latitude;
	private String longitude;
	//added for box coordinates
	private String coordinates;
	private List<LandLatLng> landLatLong;
	private List<LandDataDto> landData;

	
	
	public VillageDTO(String villageCode, String villageName, BigInteger totalPlot) {
		this.villageCode = villageCode;
		this.villageName = villageName;
		this.totalPlot = totalPlot;
	}




	public VillageDTO(String villageCode, String villageName, String tahasilCode, String tahasilName, String districtName,
			String plotNo, String kisam, String khatianNo, BigDecimal totalArea, String latitude, String longitude,String coordinates,
			String districtCode,String plotCode,String khatianCode) {
		this.villageCode = villageCode;
		this.villageName = villageName;
		this.tahasilCode = tahasilCode;
		this.tahasilName = tahasilName;
		this.districtName = districtName;
		this.plotNo = plotNo;
		this.kisam = kisam;
		this.khatianNo = khatianNo;
		this.totalArea = totalArea;
		this.latitude = latitude;
		this.longitude = longitude;
		this.coordinates = coordinates;
		this.districtCode = districtCode;
		this.plotCode = plotCode;
		this.khatianCode = khatianCode;
	}
	
	
	



	
	
	
	
	
	

}
