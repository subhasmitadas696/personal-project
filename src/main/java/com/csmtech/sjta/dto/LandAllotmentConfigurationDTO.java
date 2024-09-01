package com.csmtech.sjta.dto;

import java.math.BigDecimal;
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
public class LandAllotmentConfigurationDTO {

	@Id
	private BigInteger landId;
	private String selDistrictName;
	private String selTehsilName;
	private String selMouza;
	private String selKhataNo;
	private String selPlotNo;
	private BigDecimal txtTotalRakba;

}
