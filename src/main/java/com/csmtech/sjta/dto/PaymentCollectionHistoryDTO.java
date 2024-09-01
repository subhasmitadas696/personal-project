package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCollectionHistoryDTO {

	private String districtName;
	private String districtCode;

	private String tahasilName;
	private String tahasilCode;
	private String villageName;
	private String villageCode;
	private String khataNo;
	private String KhataCode;

	private String plotNo;
	private String plotCode;
	private BigDecimal areaAcre;
	private BigDecimal totalAmount;
	private BigDecimal january;
	private BigDecimal february;
	private BigDecimal march;
	private BigDecimal april;
	private BigDecimal may;
	private BigDecimal june;
	private BigDecimal july;
	private BigDecimal august;
	private BigDecimal september;
	private BigDecimal october;
	private BigDecimal november;
	private BigDecimal december;

	private String applicantName;
	private String landOrderNo;
	private BigDecimal totalPrice;
	private BigDecimal purchaseArea;
	private String landOrderStringDate;
	private Date landOrderDate;
}
