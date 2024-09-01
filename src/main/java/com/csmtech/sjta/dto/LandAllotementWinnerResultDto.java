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
public class LandAllotementWinnerResultDto {

	@Id
	private BigInteger landAllotementId;
	private BigInteger landId;
	private BigInteger ctreatedBy;
	private String docs;
	private String remark;
	private String fullName;
	private String plotNo;
	private BigDecimal totalArea;
	private BigDecimal purchaseArea;
	private BigDecimal pricePerAcer;
	private BigDecimal totalPricePurchaseArea;
	private String kahataNo;
	private String villageName;
	private String tahasilName;
	private String districtName;
	private String stateName;
	private String landRegistFlag;
//	private String formregisterflag;
	private String paymentFlag;
	private String registerDocs;

}
