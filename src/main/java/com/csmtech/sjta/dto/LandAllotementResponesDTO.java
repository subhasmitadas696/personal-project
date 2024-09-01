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
public class LandAllotementResponesDTO {

	@Id
	private BigInteger landAllotmentId;
	private String plotNo;
	private BigDecimal totalArea;
	private BigDecimal purchaseArea;
	private BigDecimal pricePerAcer;
	private BigDecimal totalPriceInPurchaseArea;
	private String fullName;
	private String from16Flag;
	private String form16Docs;
    private String registerFlag;
    private String paymentFlag;
}
