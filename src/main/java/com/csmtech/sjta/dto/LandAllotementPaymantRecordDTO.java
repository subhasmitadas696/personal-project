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
public class LandAllotementPaymantRecordDTO {

	@Id
	private BigInteger landAllotementId;
	private BigInteger meetingId;
	private String plotNo;
	private String totalArea;
	private String purchaseArea;
	private String pricePerAcer;
	private String totalPricePerAcer;
	private BigInteger fromMid;
	private BigInteger landId;
	private BigDecimal paidAmount;
	private String registerDocs;
	private String registerFlag;
	private String paymentFlag;
	private String plotCode;
}
