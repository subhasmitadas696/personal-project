package com.csmtech.sjta.dto;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SourceCreationPlotsRecord {
	
	@Id
	private BigInteger landAllotementId;
	private String plotNo;
	private String kathaNo;
	private String villageName;
	private String tahasilName;
	private String districtName;
	private String plotCode;
	private String khataCode;
	private String villageCode;
	private String tahasilCode;
	private String districtCode;
	private String kissam;
	private String areaInAcer;
	private String saveFlag;
	private String plotExtend;

}
