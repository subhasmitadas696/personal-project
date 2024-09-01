package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDTO {
    private BigInteger total_district;
    private BigInteger total_tahasil;
    private BigInteger total_village;
    private BigInteger total_khata;
    private BigInteger total_plot;
	private String total_area;
	private String district_name;
	private String tahasil_name;
	private String village_name;

}
