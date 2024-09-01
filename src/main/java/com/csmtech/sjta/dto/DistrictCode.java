package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictCode {
	
	private String districtCode;
	private BigInteger tahasil;
	private BigInteger mouza;
	private BigInteger plot;
	private String area;
}
